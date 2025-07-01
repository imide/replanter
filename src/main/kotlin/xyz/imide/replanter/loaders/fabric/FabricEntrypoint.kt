//? if fabric {
package xyz.imide.replanter.loaders.fabric

import com.mojang.logging.LogUtils
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import org.slf4j.Logger
import xyz.imide.replanter.Replanter.initialize
import xyz.imide.replanter.events.CropEvent


class FabricEntrypoint : ModInitializer {
    override fun onInitialize() {
        logger.info("initializing replanter")
        initialize()

        loadEvents()
        logger.info("replanter loaded!")
    }

    private fun loadEvents() {
        PlayerBlockBreakEvents.BEFORE.register(PlayerBlockBreakEvents.Before { world: Level, player: Player?, pos: BlockPos, state: BlockState, entity: BlockEntity? ->
            CropEvent.onHarvest(
                world,
                player,
                pos,
                state,
                entity
            )
        })

        ServerEntityEvents.ENTITY_LOAD.register(ServerEntityEvents.Load { entity: Entity, world: ServerLevel ->
            CropEvent.onCropEntity(world, entity)
        })
    }

    companion object {
        private val logger: Logger = LogUtils.getLogger()
    }
} //?}