package nextflow.tweet

import nextflow.plugin.BasePlugin
import org.pf4j.PluginWrapper

/**
 * Plugin entry point. This class must be specified in the resources/META-INF/MANIFEST.MF file
 *
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
class TweetPlugin extends BasePlugin {

    TweetPlugin(PluginWrapper wrapper) {
        super(wrapper)
    }
}
