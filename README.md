*nf-tweet*

This repository contains a Proof-of-Concept (POC) of a Nextflow plugin for gathering data from Twitter. It serves as a good example for developers interested in developing plugins for other data sources.

Quick overview
==============
You must obtain a Twitter Developer account so that you can request a bearer token. With that done, add your bearer token to a file named `twitter_bearer_token` in the same directory as the `main.nf` file.

You can run the current version of the `main.nf` script with:

```
nextflow run main.nf
```

### Get started with plugin dev 

1. Clone this repo 

  ```
  git clone https://github.com/nextflow-io/nf-tweet
  ```
  
  
2. Build the plugin 

  ```
  ./gradlew check
  ```
  
  
3. Run it 

  ```
  ./launch.sh run tweet.nf -plugins nf-tweet
  ```
   
   
  note: this requires a clone of [Nextflow source tree](https://github.com/nextflow-io/nextflow) in a sibling directory 
 
