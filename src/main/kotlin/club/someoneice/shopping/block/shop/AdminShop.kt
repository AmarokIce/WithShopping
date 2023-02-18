package club.someoneice.shopping.block.shop

import club.someoneice.shopping.Util
import club.someoneice.shopping.addBuyerFromAdminShop
import club.someoneice.shopping.addSellFromAdminShop
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.entity.BlockEntity

object AdminShop {
    class AdminShopBlock : ShopBlock() {
        override fun buyerEvent(player: Player, entity: BlockEntity) {
            Util.addBuyerFromAdminShop(player, entity)
        }
    }

    class AdminShopSellBlock : SellerShopBlock() {
        override fun buyerEvent(player: Player, entity: BlockEntity) {
            Util.addSellFromAdminShop(player, entity)
        }
    }
}