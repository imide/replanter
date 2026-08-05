package xyz.imide.replanter.platform.fabric

//? if fabric {

import dev.kikugie.fletching_table.annotation.fabric.Entrypoint
import net.fabricmc.api.ClientModInitializer
import xyz.imide.replanter.Replanter

@Entrypoint("client")
class FabricClientEntrypoint : ClientModInitializer {
	override fun onInitializeClient() {
		Replanter.onInitializeClient()
	}
}
//?}
