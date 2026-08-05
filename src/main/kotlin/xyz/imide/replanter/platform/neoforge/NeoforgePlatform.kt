package xyz.imide.replanter.platform.neoforge
//? neoforge {

/*import net.neoforged.fml.ModList
import net.neoforged.fml.loading.FMLLoader
import xyz.imide.replanter.platform.Platform

class NeoforgePlatform : Platform {
	override fun isModLoaded(modId: String): Boolean = ModList.get().isLoaded(modId)

	override fun loader(): Platform.ModLoader = Platform.ModLoader.NEOFORGE

	override fun mcVersion(): String = ""

	override fun isDevelopmentEnvironment(): Boolean {
		return !FMLLoader/*? if > 1.21.7 {*/.getCurrent()/*?}*/.isProduction
	}
}

*///?}
