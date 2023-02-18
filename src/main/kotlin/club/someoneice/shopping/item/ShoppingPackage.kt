package club.someoneice.shopping.item

import club.someoneice.shopping.ItemDict
import club.someoneice.shopping.ShoppingMain
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class ShoppingPackage: Item(Properties().tab(ShoppingMain.TAB)) {
    override fun use(world: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        if (!world.isClientSide && hand == InteractionHand.MAIN_HAND) {
            val item = player.mainHandItem
            val itemList = ItemDict()
            if (item.orCreateTag.contains("ItemDict")) itemList.load(item.orCreateTag.getCompound("ItemDict"))

            if (player.offhandItem.sameItem(ItemStack.EMPTY) || player.offhandItem.isEmpty) {
                val output = itemList.getItem().copy()
                if (itemList.get(output) > 64) {
                    output.count = 64
                    itemList.put(output, itemList.get(output) - 64)
                } else {
                    output.count = itemList.get(output)
                    itemList.remove(output)
                }
                item.orCreateTag.put("ItemDict", itemList.save())
                player.setItemInHand(InteractionHand.OFF_HAND, output)
            } else {
                if (itemList.size() > 3)
                    return InteractionResultHolder(InteractionResult.SUCCESS, item)

                itemList.put(player.offhandItem.copy(), player.offhandItem.count)
                item.orCreateTag.put("ItemDict", itemList.save())
                player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY)
            }
        }

        return InteractionResultHolder(InteractionResult.SUCCESS, player.getItemInHand(hand))
    }

    override fun appendHoverText(item: ItemStack, world: Level?, list: MutableList<Component>, flag: TooltipFlag) {
        val itemList = ItemDict()
        if (item.orCreateTag.contains("ItemDict")) itemList.load(item.orCreateTag.getCompound("ItemDict"))

        for (i in 1 .. itemList.size()) {
            val item = itemList.getItem()
            list.add(TextComponent(item.displayName.string + ": " + itemList.get(item)))
        }
    }
}