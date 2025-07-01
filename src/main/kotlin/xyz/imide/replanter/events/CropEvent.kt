package xyz.imide.replanter.events

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.CocoaBlock
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import xyz.imide.replanter.config.ReplanterConfig
import xyz.imide.replanter.utils.isHoe
import xyz.imide.replanter.utils.itemHurtBreak

class CropEvent {

    companion object {
        private val checkReplant: HashMap<BlockPos, Block> = HashMap()
        private val cocoaStates: HashMap<BlockPos, BlockState> = HashMap()

        fun onHarvest(world: Level, player: Player?, hpos: BlockPos, state: BlockState, blockEntity: BlockEntity?): Boolean {
            if (world.isClientSide || player == null) return true;

            var hand: InteractionHand? = null
            if (ReplanterConfig.mustHoldHoe) {
                hand = InteractionHand.MAIN_HAND
                if (!isHoe(player.mainHandItem)) {
                    hand = InteractionHand.OFF_HAND
                    if (!isHoe(player.offhandItem)) {
                        return true
                    }
                }
            }

            if (player.isShiftKeyDown) return true

            val block: Block = state.block

            when (block) {
                is CropBlock -> checkReplant.put(hpos, block)
                Blocks.NETHER_WART -> checkReplant.put(hpos, block)
                Blocks.COCOA -> {
                    cocoaStates.put(hpos, state)
                    checkReplant.put(hpos, block)
                }
                else -> return true
            }

            if (hand != null && !player.isCreative) itemHurtBreak(player.getItemInHand(hand), player as ServerPlayer, hand, 1)

            return true
        }

        fun onCropEntity(world: Level, entity: Entity): Boolean {
            if (world.isClientSide || entity !is ItemEntity || !ReplanterConfig.enabled) return true

            val ipos: BlockPos = entity.blockPosition()
            if (!checkReplant.containsKey(ipos)) return true

            val preblock: Block? = checkReplant.get(ipos)

            var compareItem: Item? = null
            if (preblock is CropBlock) compareItem = preblock.asItem()

            val itemEntity: ItemEntity = entity
            val itemStack: ItemStack = itemEntity.item
            val item: Item = itemStack.item

            when (item) {
                compareItem -> {
                    world.setBlockAndUpdate(ipos, preblock!!.defaultBlockState())
                }
                Items.NETHER_WART -> {
                    world.setBlockAndUpdate(ipos, Blocks.NETHER_WART.defaultBlockState())
                }
                Items.COCOA_BEANS -> {
                    if (!cocoaStates.containsKey(ipos)) {
                        checkReplant.remove(ipos)
                        return true
                    }
                    world.setBlockAndUpdate(ipos, cocoaStates[ipos]!!.setValue(CocoaBlock.AGE, 0))
                }
                else -> return true
            }

            checkReplant.remove(ipos)

            if (itemStack.count > 1) itemStack.shrink(1) else { entity.remove(Entity.RemovalReason.DISCARDED); return false }
            return true
        }
    }
}