package xyz.imide.replanter.platform.fabric
//? fabric {
import net.fabricmc.loader.api.FabricLoader
import xyz.imide.replanter.platform.Platform

class FabricPlatform : Platform {
	override fun isModLoaded(modId: String): Boolean = FabricLoader.getInstance().isModLoaded(modId)

	override fun loader(): Platform.ModLoader = Platform.ModLoader.FABRIC

	override fun mcVersion(): String = FabricLoader.getInstance().rawGameVersion

	override fun isDevelopmentEnvironment(): Boolean = FabricLoader.getInstance().isDevelopmentEnvironment

}
//?}
