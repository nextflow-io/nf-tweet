nf-tweet
==============

This repository contains a Proof-of-Concept (PoC) of a Nextflow plugin for gathering data from Twitter. It serves as a good example for developers interested in developing plugins for other data sources.

Quick overview
==============
You must obtain a Twitter Developer account so that you can request a bearer token. With that done, add your bearer token to a file named `twitter_bearer_token` in the same directory as the `tweet.nf` file.

This is a work in progress, but when it's ready, you can use it by adding the following to your `nextflow.config`:

```
plugins {
  id 'nf-tweet@0.0.1'
}
```

Currently, there is only one channel factory called `ofTweets`, where you can query tweets based on a string. By default, it considers the latest 20 tweets in the last 24 hours. For now, there is only one option implemented for this channel factory, as you can see below:

The following options are available:

| Operator option  | Description                  |
|---             |---                         |
| `exclude_retweets`              | The default value is false. Set to true to exclude pure retweets (retweet with message is still included, though).

### Getting started with plugin dev

1. Clone this repo

  ```
  git clone https://github.com/nextflow-io/nf-tweet
  ```

2. Clone Nextflow's repository as a sibling repository (if it's not already the case)

  ```
  git clone https://github.com/nextflow-io/nextflow
  ```

 3. Generate the class path

  ```
  cd nextflow && ./gradlew exportClasspath
  ```

4. Enter nf-tweet cloned repo and build the plugin

  ```
  cd ../nf-tweet && ./gradlew check
  ```


5. Run an example with the nf-tweet plugin.
> **Note**
> remember to first create the `twitter_bearer_token` file with your bearer token

  ```
  ./launch.sh run tweet.nf -plugins nf-tweet
  ```

