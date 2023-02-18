package club.someoneice.shopping

import committee.nova.lighteco.util.EcoUtils
import net.minecraft.network.chat.TextComponent
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import java.math.BigDecimal


object Util {
    val Helper: HashMap<Player, Any> = HashMap()

    data class Seller(
        val item: ItemStack,
        val entity: BlockEntity
    )

    data class Buyer(
        val entity: BlockEntity,
        val isAdminShop: Boolean,
        val isSellSeller: Boolean
    )

    data class SellerShopHelper(
        val entity: BlockEntity
    )

    data class Cheque(
        val nul: Any?
    )

}

fun Player.sendMessageInText(str: String) {
    sendMessage(TextComponent(str), uuid)
}

fun Player.sendMessageInTranslatable(str: String) {
    sendMessage(TranslatableComponent(str), uuid)
}

fun Player.addMoney(value: Double) {
    EcoUtils.debt(this, BigDecimal(value))
}

fun Player.deductMoney(value: Double) {
    EcoUtils.credit(this, BigDecimal(value))
}

fun Player.getMoney(): Double {
    return EcoUtils.getBalance(this).map(BigDecimal::toDouble).orElse(0.0);
}

fun Item.itemStack(): ItemStack {
    return ItemStack(this)
}

// For Shop Seller.
fun Util.addSeller(player: Player, item: ItemStack, entity: BlockEntity) {
    Helper[player] = Util.Seller(item, entity)
}

// If shop is sell mode, add a buyer.
fun Util.addSellBuyer(player: Player, entity: BlockEntity) {
    Helper[player] = Util.Buyer(entity, false, false)
}

// If shop is buy mode, add a seller.
fun Util.addSellSeller(player: Player, entity: BlockEntity) {
    Helper[player] = Util.Buyer(entity, false, true)
}

// On AdminShopMode.
fun Util.addBuyerFromAdminShop(player: Player, entity: BlockEntity) {
    Helper[player] = Util.Buyer(entity, true, false)
}

fun Util.addSellFromAdminShop(player: Player, entity: BlockEntity) {
    Helper[player] = Util.Buyer(entity, true, true)
}

fun Util.addCheque(player: Player) {
    Helper[player] = Util.Cheque(null)
}