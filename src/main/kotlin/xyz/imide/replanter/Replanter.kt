package xyz.imide.replanter

import net.minecraft.resources.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import xyz.imide.replanter.platform.Platform

//? fabric {
import xyz.imide.replanter.platform.fabric.FabricPlatform
//?} neoforge {
/*import xyz.imide.replanter.platform.neoforge.NeoforgePlatform;
 *///?}



@SuppressWarnings("LoggingSimilarMessage")
object Replanter {
    const val modId: String = /*$ mod_id*/ "replanter";
	const val modVersion: String =  /*$ mod_version*/"2.0.0";
	const val modFriendlyName: String =  /*$ mod_name*/"Replanter";
	val logger: Logger = LoggerFactory.getLogger(modId)

	private val platform: Platform = createPlatformInstance()

	fun onInitialize() {
		logger.info("Initializing {} on {}", modId, xplat().loader())
		logger.debug("{}: { version: {}; friendly_name: {}}", modId, modVersion, modFriendlyName)
	}

	fun onInitializeClient() {
		logger.info("Initializing {} Client on {}", modId, xplat().loader())
		logger.debug("{}: { version: {}; friendly_name: {}}", modId, modVersion, modFriendlyName)
	}

	fun xplat(): Platform = platform

	private fun createPlatformInstance(): Platform {
		//? fabric {
		return FabricPlatform()
		//?} neoforge {
		 /*return NeoforgePlatform()
		*///?}
	}

	private fun id(path: String): Identifier {
		return Identifier.fromNamespaceAndPath(modId, path)
	}

	private fun id(namespace: String, path: String): Identifier {
		return Identifier.fromNamespaceAndPath(namespace, path)
	}
}
