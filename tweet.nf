include { ofTweets } from 'plugin/nf-tweet'


Channel
  .ofTweets('nextflow', excludeRetweets: true)
  .map { author, date_tweet -> author }
  .collect()
  .view()
