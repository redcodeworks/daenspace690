package edu.gmu.daenspace690

import com.typesafe.config.{Config, ConfigFactory}
import com.johnsnowlabs.nlp.SparkNLP
import com.johnsnowlabs.nlp.annotator.Normalizer
import com.johnsnowlabs.nlp.annotators._
import com.johnsnowlabs.nlp.annotators.classifier.dl.ClassifierDLApproach
import com.johnsnowlabs.nlp.base._
import com.johnsnowlabs.nlp.embeddings._
import org.apache.spark.ml.{Estimator, Pipeline, Transformer}
import com.johnsnowlabs.nlp.pretrained.ResourceDownloader
import org.apache.spark.ml.tuning.{
  ParamGridBuilder,
  CrossValidator,
  TrainValidationSplit
}

abstract class NlpPipeline {
  val config: Config = ConfigFactory.load()
  val lang = config.getString("input-data.language")
  val docAssembler =
    new DocumentAssembler() setInputCol ("description") setOutputCol ("document")
  val sentenceEmbeddings: Transformer
  val classifier: ClassifierDLApproach = new ClassifierDLApproach()
    .setInputCols("sentence_embeddings")
    .setOutputCol("class")
    .setLabelColumn("label")
    .setMaxEpochs(5)

  val pipeline: Pipeline
//  TODO: Implement paramgrids for cross validators
//  val cv: CrossValidator
}

abstract class StandardPipeline extends NlpPipeline {
  lazy val tokenizer =
    new Tokenizer() setInputCols (docAssembler.getOutputCol) setOutputCol ("token")
  lazy val normalizer =
    new Normalizer() setInputCols (tokenizer.getOutputCol) setOutputCol ("normalized")
  lazy val stopwordsCleaner =
    new StopWordsCleaner() setInputCols (normalizer.getOutputCol) setOutputCol ("cleanedTokens")
  lazy val lemma = new LemmatizerModel(
    config.getString("pipeline.lemmatizerModel")
  ) setInputCols (stopwordsCleaner.getOutputCol) setOutputCol ("lemma")

  val wordEmbeddings: Transformer
  val sentenceEmbeddings: Transformer

  val pipeline: Pipeline = new Pipeline().setStages(
    Array(
      docAssembler,
      tokenizer,
      normalizer,
      stopwordsCleaner,
      lemma,
      wordEmbeddings,
      sentenceEmbeddings,
      classifier
    )
  )
}

object UsePipeline extends NlpPipeline {
  val sentenceEmbeddings = UniversalSentenceEncoder
    .pretrained(config.getString("use.model"), lang)
    .setInputCols(docAssembler.getOutputCol)
    .setOutputCol("sentence_embeddings")

  val pipeline: Pipeline = new Pipeline().setStages(
    Array(docAssembler, sentenceEmbeddings, classifier)
  )
}

object GlovePipeline extends StandardPipeline {
  lazy val wordEmbeddings: WordEmbeddingsModel = WordEmbeddingsModel
    .pretrained()
    .setInputCols(docAssembler.getOutputCol, lemma.getOutputCol)
    .setOutputCol("embeddings")

  lazy val sentenceEmbeddings = new SentenceEmbeddings()
    .setInputCols(docAssembler.getOutputCol, "embeddings")
    .setOutputCol("sentence_embeddings")
    .setPoolingStrategy(config.getString("glove.pooling"))
}

object BertPipeline extends StandardPipeline {
  lazy val wordEmbeddings = BertEmbeddings
    .pretrained(config.getString("bert.model"), lang)
    .setInputCols(docAssembler.getOutputCol, lemma.getOutputCol)
    .setOutputCol("embeddings")

  lazy val sentenceEmbeddings = new SentenceEmbeddings()
    .setInputCols(docAssembler.getOutputCol, "embeddings")
    .setOutputCol("sentence_embeddings")
    .setPoolingStrategy(config.getString("bert.pooling"))
}

object ElmoPipeline extends StandardPipeline {
  lazy val wordEmbeddings = ElmoEmbeddings
    .pretrained(config.getString("elmo.model"))
    .setInputCols(docAssembler.getOutputCol, lemma.getOutputCol)
    .setOutputCol("embeddings")

  lazy val sentenceEmbeddings =
    new SentenceEmbeddings()
      .setInputCols(docAssembler.getOutputCol, "embeddings")
      .setOutputCol("sentence_embeddings")
      .setPoolingStrategy(config.getString("elmo.pooling"))
}
