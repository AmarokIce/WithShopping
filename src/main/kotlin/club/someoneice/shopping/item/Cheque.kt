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

class Cheque: Item(Properties().tab(ShoppingMain.TAB)) {
    override fun use(world: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        super.use(world, player, hand)
        val item: ItemStack = player.getItemInHand(hand)

        if (!world.isClientSide) {
            if (item.orCreateTag.contains("eco_money"))
                player.addMoney(item.orCreateTag.getInt("eco_money").toDouble())
            else player.addMoney(0.0)
            player.sendMessageInText(TranslatableComponent("shop.getMoney.message").string + player.getMoney())
            item.count -= 1
        }

        return InteractionResultHolder.success(item)
    }

    override fun appendHoverText(item: ItemStack, world: Level?, list: MutableList<Component>, flag: TooltipFlag) {
        val eco: Double = if (item.orCreateTag.contains("eco_money")) item.orCreateTag.getInt("eco_money").toDouble() else 0.0
        list.add(TextComponent(TranslatableComponent("shop.chequeEco.text").string + eco))

    }
}