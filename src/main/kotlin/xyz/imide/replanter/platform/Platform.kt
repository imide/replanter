package xyz.imide.replanter.platform

interface Platform {
	fun isModLoaded(modId: String): Boolean

	fun loader(): ModLoader

	fun mcVersion(): String

	fun isDevelopmentEnvironment(): Boolean

	fun isDebug(): Boolean = isDevelopmentEnvironment()

	enum class ModLoader {
		FABRIC, NEOFORGE, FORGE, QUILT
	}
}
