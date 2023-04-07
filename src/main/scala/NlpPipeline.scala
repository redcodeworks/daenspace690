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
    new DocumentAssembler() setInputCol "text" setOutputCol "document"

  val sentenceEmbeddings: Transformer

  val classifier: ClassifierDLApproach = new ClassifierDLApproach()
    .setInputCols("sentence_embeddings")
    .setOutputCol("class")
    .setLabelColumn("label")
    .setMaxEpochs(config.getInt("classifier.epochs"))

  val pipeline: Pipeline
  //  TODO: Implement paramgrids for cross validators
  //  val cv: CrossValidator
}



object UsePipeline extends NlpPipeline {
  val useModel = config.getString("use.model")
  val sentenceEmbeddings = UniversalSentenceEncoder
    .pretrained(useModel, lang)
    .setInputCols(docAssembler.getOutputCol)
    .setOutputCol("sentence_embeddings")

  val pipeline: Pipeline = new Pipeline().setStages(
    Array(
      docAssembler,
      sentenceEmbeddings,
      classifier
    )
  )
}

abstract class StandardPipeline extends NlpPipeline {
  lazy val tokenizer = new Tokenizer()
    .setInputCols(docAssembler.getOutputCol)
    .setOutputCol("token")

  lazy val normalizer = new Normalizer()
    .setInputCols(tokenizer.getOutputCol)
    .setOutputCol("normalized")

  lazy val stopwordsCleaner = new StopWordsCleaner()
    .setInputCols(normalizer.getOutputCol)
    .setOutputCol("cleanedTokens")

  lazy val lemma = LemmatizerModel
    .pretrained(lemmatizerModel)
    .setInputCols(stopwordsCleaner.getOutputCol)
    .setOutputCol("lemma")

  lazy val sentenceEmbeddings = new SentenceEmbeddings()
    .setInputCols(docAssembler.getOutputCol, "embeddings")
    .setOutputCol("sentence_embeddings")
    .setPoolingStrategy(poolingStrategy)

  val lemmatizerModel = config.getString("pipeline.lemmatizerModel")

  val wordEmbeddings: Transformer

  val poolingStrategy: String

}

object GlovePipeline extends StandardPipeline {
  val gloveModel = config.getString("glove.model")
  val poolingStrategy = config.getString("glove.pooling")
  val wordEmbeddings: WordEmbeddingsModel = WordEmbeddingsModel
    .pretrained(gloveModel, lang)
    .setInputCols(docAssembler.getOutputCol, lemma.getOutputCol)
    .setOutputCol("embeddings")

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

object BertPipeline extends StandardPipeline {
  val bertModel = config.getString("bert.model")
  val poolingStrategy = config.getString("bert.pooling")
  val wordEmbeddings = BertEmbeddings
    .pretrained(bertModel, lang)
    .setInputCols(docAssembler.getOutputCol, lemma.getOutputCol)
    .setOutputCol("embeddings")

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

object ElmoPipeline extends StandardPipeline {
  val elmoModel = config.getString("elmo.model")
  val poolingStrategy = config.getString("elmo.pooling")
  val wordEmbeddings = ElmoEmbeddings
    .pretrained(elmoModel)
    .setInputCols(docAssembler.getOutputCol, lemma.getOutputCol)
    .setOutputCol("embeddings")

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
