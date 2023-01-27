package nextflow.tweet

import groovyx.gpars.dataflow.DataflowWriteChannel
import nextflow.Channel
import nextflow.Session
import nextflow.extension.CH
import nextflow.plugin.extension.Factory
import nextflow.plugin.extension.PluginExtensionPoint

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import groovy.json.*;
import java.text.SimpleDateFormat;
import java.nio.charset.StandardCharsets

/**
 * Implements Tweet extensions.
 *
 * Note: this class name must be specified in the resources/META-INF/extension.idx file
 *
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
class TweetExtension extends PluginExtensionPoint {

    private Session session

    @Override
    protected void init(Session session) {
        // take a reference to the nextflow session
        this.session = session
    }

    @Factory
    DataflowWriteChannel ofTweets(String query) {
        String bearerToken = new File("twitter_bearer_token").text.strip()
        final channel = CH.create()
        session.addIgniter(it -> emitTweets(channel, query, bearerToken) )
        return channel
    }

    protected void emitTweets(DataflowWriteChannel channel, query, bearerToken) {
        def end_time = new Date().getTime()
        def start_time = end_time - (24 * 60 * 60 * 1000)
        def exclude_retweets=true
        def max_results=20
        // Formating dates adequately
        String start_time_str = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(start_time);
        String end_time_str = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(end_time);
        // Formating query adequately
        def query_string = query.replace(' ', '%20')
        String optional_query = ''
        if (exclude_retweets) {
            optional_query = '%20-is:retweet'
        }

        String url = "https://api.twitter.com/2/tweets/search/recent?query=" +\
                        query_string + optional_query +\
                        "&start_time=${start_time_str}" +\
                        "&end_time=${end_time_str}" +\
                        "&tweet.fields=created_at,author_id" +\
                        "&max_results=${max_results}"
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer " + bearerToken);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        def json = new JsonSlurper().parseText(response.toString())
        List<Map<String, String>> list_of_tweets = []
        json.data.each { tweet ->
            String url2 = "https://api.twitter.com/2/users?ids=${tweet.author_id}"
            URL obj2 = new URL(url2);
            HttpURLConnection con2 = (HttpURLConnection) obj2.openConnection();
            con2.setRequestMethod("GET");
            con2.setRequestProperty("Authorization", "Bearer " + bearerToken);
            BufferedReader in2 = new BufferedReader(new InputStreamReader(con2.getInputStream()));
            String inputLine2;
            StringBuffer response2 = new StringBuffer();
            while ((inputLine2 = in2.readLine()) != null) {
                def json2 = new JsonSlurper().parseText(inputLine2.toString())
                json2.data.each { tweet2 ->
                    Map<String, String> tweet_map = [:]
                    tweet_map.put('created_at', tweet.created_at)
                    tweet_map.put('author_id', tweet.author_id)
                    tweet_map.put('author_handle', tweet2.username)
                    tweet_map.put('tweet_text', tweet.text)
                    list_of_tweets.add(tweet_map)
                }
            }
        }
        list_of_tweets.each { it ->
            channel.bind([it['author_handle'], [it['created_at'], it['tweet_text']]])
        }
        channel.bind(Channel.STOP)
    }
}
