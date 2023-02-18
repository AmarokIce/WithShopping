package club.someoneice.shopping.block.shop

import club.someoneice.shopping.*
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

open class SellerShopBlock: ShopBlock() {
    @Deprecated("Deprecated in Java")
    override fun use(blockState: BlockState, world: Level, pos: BlockPos, player: Player, hand: InteractionHand, hit: BlockHitResult): InteractionResult {
        if (!world.isClientSide) {
            val entity: ShopEntity = world.getBlockEntity(pos)!! as ShopEntity
            if (entity.tileData.contains("Owner") && player.scoreboardName.toString() == entity.tileData.getString("Owner")) {
                val holdItem: ItemStack = player.mainHandItem.copy()
                if (entity.itemCommodity.isEmpty && !holdItem.isEmpty) {
                    Util.addSeller(player, holdItem, entity)
                    player.sendMessageInTranslatable("shop.createNewShop.message")
                    entity.markDirty()
                } else if (player.isShiftKeyDown && !entity.itemCommodity.sameItem(ItemStack.EMPTY)) {
                    val count = entity.itemInvCount
                    if (count > 64) {
                        val item = entity.itemCommodity.copy()
                        item.count = 64

                        player.inventory.add(item)
                        entity.itemInvCount -= 64
                    } else {
                        val item = entity.itemCommodity.copy()
                        item.count = count
                        player.inventory.add(item)
                        entity.itemInvCount = 0
                    }
                    player.addMoney(entity.money)
                    entity.markDirty()
                } else if (holdItem.isEmpty) {
                    Util.Helper[player] = Util.SellerShopHelper(entity)
                    player.sendMessageInText(TranslatableComponent("shop.getItem.message").string + entity.itemCommodity.displayName.string + ", " + TranslatableComponent("shop.getItemCount.message").string + entity.itemInvCount)
                    player.sendMessageInText(TranslatableComponent("shop.shopHaveMoney.message").string + entity.money)
                    player.sendMessageInTranslatable("shop.addMoneyToShop.message")
                }
            } else {
                val owner = if (this is AdminShop.AdminShopSellBlock) "Admin" else entity.tileData.getString("Owner")
                player.sendMessageInTranslatable("shop.messageBuyShop.message")
                player.sendMessageInText(TranslatableComponent("shop.OwnerMessage.message").string + owner)
                player.sendMessageInText(TranslatableComponent("shop.getItem.message").string + entity.itemCommodity.displayName.string)
                player.sendMessageInText(TranslatableComponent("shop.price.message").string + entity.itemPrice)
                player.sendMessageInText(TranslatableComponent("shop.shopHaveMoney.message").string + entity.money)
                player.sendMessageInTranslatable("shop.buyerItemMessage.message")
                buyerEvent(player, entity)
            }
        }

        return InteractionResult.SUCCESS
    }

    override fun buyerEvent(player: Player, entity: BlockEntity) {
        Util.addSellSeller(player, entity)
    }
}