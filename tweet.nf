include { ofTweets } from 'plugin/nf-tweet'


Channel
  .ofTweets('nextflow', exclude_retweets: true)
  .map { author, date_tweet -> author }
  .collect()
  .view()
