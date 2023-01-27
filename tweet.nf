include { ofTweets } from 'plugin/nf-tweet'


Channel
  .ofTweets('Nextflow')
  .map { author, date_tweet -> author }
  .collect()
  .view()
