//? if neoforge {
/*package xyz.imide.replanter.loaders.neoforge

import com.mojang.logging.LogUtils
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent
import net.neoforged.neoforge.common.NeoForge
import org.slf4j.Logger
import xyz.imide.replanter.Replanter.initialize
import xyz.imide.replanter.events.NeoForgeCropEvent

@Mod("replanter")
class NeoforgeEntrypoint(modIEventBus: IEventBus) {
    init {
        logger.info("initializing replanter for neoforge...")

        modIEventBus.addListener(this::loadComplete)

        initialize()
    }

    private fun loadComplete(event: FMLLoadCompleteEvent) {
        NeoForge.EVENT_BUS.register(NeoForgeCropEvent::class.java)
        logger.info("replanter loaded!")
    }

    companion object {
        private val logger: Logger = LogUtils.getLogger()
    }
} *///?}