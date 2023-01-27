include { ofTweets } from 'plugin/nf-tweet'


Channel
  .ofTweets('Nextflow')
  .collect()
  .view()
