package xyz.imide.replanter.utils

import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.stats.Stats
import net.minecraft.tags.ItemTags
import net.minecraft.world.InteractionHand
import net.minecraft.world.item.HoeItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import java.util.function.Consumer


fun isHoe(itemStack: ItemStack): Boolean {
    return itemStack.item is HoeItem || itemStack.`is`(ItemTags.HOES)
}

fun itemHurtBreak(itemStack: ItemStack, player: ServerPlayer, hand: InteractionHand, damage: Int) {
    val level: Level = player.level()

    if (level.isClientSide) return

    if (!player.abilities.instabuild && itemStack.isDamageableItem) {
        itemStack.hurtAndBreak(damage, level as ServerLevel, player, Consumer { item: Item ->
            itemStack.shrink(1)
            itemStack.damageValue = 0

            player.awardStat(Stats.ITEM_BROKEN.get(item))

        })
    }
}
