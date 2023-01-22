package nextflow.tweet

import groovyx.gpars.dataflow.DataflowWriteChannel
import nextflow.Channel
import nextflow.Session
import nextflow.extension.CH
import nextflow.plugin.extension.Factory
import nextflow.plugin.extension.PluginExtensionPoint
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
        final channel = CH.create()
        session.addIgniter(it -> emitTweets(channel) )
        return channel
    }

    protected void emitTweets(DataflowWriteChannel channel) {
        channel.bind('Hello')
        channel.bind('world!')
        channel.bind(Channel.STOP)
    }
}
