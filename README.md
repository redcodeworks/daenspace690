# GMU DAEN 690 Capstone - Team DS9

## Background

This project relates to the body of work around the annotation of Standard Operating Procedures ("SOPs").

### Natural Language Processing

This project utilizes the Spark NLP library from John Snow Labs.

## How To Run
### Requirements
- Scala 2.12 and sbt - Most easily installed with [coursier](https://get-coursier.io/docs/cli-installation).
- OpenJDK 8 or 11 - Currently have issues with Java 17.

### Configuration
See `application.conf` in `src/main/scala/resources`. File is specified according to [HOCON (Human-Optimized Config Object Notation)](https://github.com/lightbend/config/blob/main/HOCON.md).

### Available Models
The following pipelines have been built for use in this project. Edit `pipeline.name` in the config file to chose one of these.

- `use` -- UniversalSentenceEncoder
- `glove`
- `bert`
- `elmo`

### Pipeline modes
Controlled by `pipeline.mode`. Choose from "pipeline-only", "cross-validate", or "train-validSate"
- **pipeline-only** - Runs the train job once with no parameter tuning or cross-validation.
- **cross-validate** - Cross-validate and tune parameters on each pass.
- **train-validate** - tune parameters once, then cross-validate reusing these parameters.