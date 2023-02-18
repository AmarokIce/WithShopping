package club.someoneice.shopping.item

import club.someoneice.shopping.ShoppingMain
import club.someoneice.shopping.addMoney
import club.someoneice.shopping.getMoney
import club.someoneice.shopping.sendMessageInText
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class ChequeInCrivate: Item(Properties().tab(ShoppingMain.TAB)) {
    override fun use(world: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        super.use(world, player, hand)
        val item: ItemStack = player.getItemInHand(hand)

        if (!world.isClientSide) {
            player.addMoney(100.0)
            player.sendMessageInText(TranslatableComponent("shop.getMoney.message").string + player.getMoney())
            item.count -= 1
        }

        return InteractionResultHolder.success(item)
    }

    override fun appendHoverText(item: ItemStack, world: Level?, list: MutableList<Component>, flag: TooltipFlag) {
        list.add(TextComponent(TranslatableComponent("shop.chequeEco.text").string + 100))
    }
}