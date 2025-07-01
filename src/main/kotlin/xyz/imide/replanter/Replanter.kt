package xyz.imide.replanter

import com.mojang.logging.LogUtils
import com.teamresourceful.resourcefulconfig.api.loader.Configurator
import org.slf4j.Logger
import xyz.imide.replanter.config.ReplanterConfig

object Replanter {

    // Config
    val config: Configurator = Configurator("replanter")
    val logger: Logger = LogUtils.getLogger()

    fun initialize() {
        logger.info("Initializing replanter")

        config.register(ReplanterConfig::class.java)
    }
}
