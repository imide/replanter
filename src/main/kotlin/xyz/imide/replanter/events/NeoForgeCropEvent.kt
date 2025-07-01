//? if neoforge {
/*package xyz.imide.replanter.events

import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent
import net.neoforged.neoforge.event.level.BlockEvent

class NeoForgeCropEvent {
    companion object {
        internal fun _getWorld(levelAccessor: LevelAccessor): Level? {
            return if (levelAccessor.isClientSide && levelAccessor !is Level) null else levelAccessor as Level
        }

        @SubscribeEvent
        fun onHarvest(event: BlockEvent.BreakEvent) {
            val level: Level? = _getWorld(event.level)
            if (level == null) return

            CropEvent.onHarvest(level, event.player, event.pos, event.state, null)
        }

        @SubscribeEvent
        fun onCropEntity(event: EntityJoinLevelEvent) {
            if (!CropEvent.onCropEntity(event.level, event.entity)) event.isCanceled = true
        }

    }
}
*///?}