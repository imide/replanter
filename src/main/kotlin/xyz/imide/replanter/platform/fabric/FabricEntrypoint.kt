package xyz.imide.replanter.platform.fabric
//? fabric {

import dev.kikugie.fletching_table.annotation.fabric.Entrypoint
import net.fabricmc.api.ModInitializer
import xyz.imide.replanter.Replanter

@Entrypoint("main")
class FabricEntrypoint: ModInitializer {
	override fun onInitialize() {
		Replanter.onInitialize()
		FabricEventSubscriber.registerEvents()
	}
}

//?}
