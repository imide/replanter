package xyz.imide.replanter.platform.neoforge
//? neoforge {

/*import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import xyz.imide.replanter.Replanter

@EventBusSubscriber(modid = Replanter.modId, value = [Dist.CLIENT])
class NeoforgeClientEventSubscriber {
	@SubscribeEvent
	fun onClientSetup(event: FMLClientSetupEvent) {
		Replanter.onInitializeClient()
	}
}

*///?}
