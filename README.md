# GMU DAEN 690 Capstone - Team DS9

## Background

This project relates to the body of work around the annotation of Standard Operating Procedures ("SOPs"). The contents of `src/` represents the Scala-based spark project, intended to be run with `spark-submit`. Meanwhile, the contents of `notebooks/` is the locally developed research and can be run with the environment specified in `pyproject.toml`.

This repo also uses GitHub Packages and GitHub Actions. GitHub packages is used to host compiled artifacts and can be used in other projects using the Maven package manager. GitHub actions is used for CI/CD. There is a Continuous Integration job, which checks if the project successfully compiles, and a Continuous Deployment job, which packages the source code into a jar and pushes the resulting artifact to GitHub Packages.

### Natural Language Processing

This project utilizes the Spark NLP library from John Snow Labs. Specifically, we are using transformer-based embeddings during the pipeline stages to vectorize text before feeding it into an optimized deep learning classifier.

## Contents
-	**built.sbt** – this is the Scala build definition for the project, which includes the appropriate dependencies and versions.
-	**LICENSE** – license file. Uses the MIT License
-	**notebooks/** – contributions from local development. This contains the machine learning insights and presentation layer.
-	**project/** – compiled Scala project.
-	**pyproject.toml** – Python project definition for notebooks/ contents. Use `poetry install` to install required python dependencies when executing notebooks.
-	**src/** – contains Scala source code for Spark project.


## How To Run

### Requirements

- Scala 2.12 and sbt - Most easily installed with [coursier](https://get-coursier.io/docs/cli-installation).
- OpenJDK 8 or 11 - Currently have issues with Java 17.

The contents of `src/` represents the Scala-based spark project, intended to be run with `spark-submit`. An example command would look something like this. Note the dependencies specified with the `--packages` flag.

`spark-submit --master local[*] --deploy-mode client --class edu.gmu.daenspace690.ModelFit --name daenspace690 --packages org.apache.hadoop:hadoop-client:3.3.5,org.apache.hadoop:hadoop-aws:3.3.5,com.amazonaws:aws-java-sdk:1.12.316,com.johnsnowlabs.nlp:spark-nlp_2.12:4.3.1 target/scala-2.12/daenspace690-assembly-0.1.0-SNAPSHOT.jar`


### Configuration

See `application.conf` in `src/main/scala/resources`. File is specified according
to [HOCON (Human-Optimized Config Object Notation)](https://github.com/lightbend/config/blob/main/HOCON.md). Many functions will first check for an environment variable. If an environment varible is not found, then the project will fallback on `application.conf`.

### Available Models

The following pipelines have been built for use in this project. Edit `pipeline.name` in the config file to chose one of
these.

- `use`
- `glove`
- `bert`
- `elmo`

### Pipeline modes

Controlled by `pipeline.mode`. Choose from "pipeline-only", "cross-validate", or "train-validSate"

- **pipeline-only** - Runs the train job once with no parameter tuning or cross-validation.
- **cross-validate** - Cross-validate and tune parameters on each pass.
- **train-validate** - tune parameters once, then cross-validate reusing these parameters.
