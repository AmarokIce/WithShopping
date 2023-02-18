package club.someoneice.shopping.block.shop

import club.someoneice.shopping.*
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material
import net.minecraft.world.level.pathfinder.PathComputationType
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.phys.BlockHitResult


open class ShopBlock(): Block(Properties.of(Material.GLASS).strength(1.5F, 3600F).noOcclusion()), EntityBlock {
    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return ShopEntity(pos, state)
    }

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
                } else if (entity.itemCommodity.sameItem(holdItem)) {
                    entity.itemInvCount += holdItem.count
                    player.mainHandItem.count = 0
                    player.sendMessageInTranslatable("shop.setItemSuccessful.message")
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
                } else if (holdItem.isEmpty) {
                    player.sendMessageInText(TranslatableComponent("shop.getItem.message").string + entity.itemCommodity.displayName.string + ", " + TranslatableComponent("shop.getItemCount.message").string + entity.itemInvCount)
                    player.sendMessageInText(TranslatableComponent("shop.getMoneyFromShop.message").string + entity.money)
                    player.addMoney(entity.money)
                    entity.money = 0.0
                    entity.markDirty()
                }
            } else {
                val invCount = if (this is AdminShop.AdminShopBlock) "Admin" else entity.itemInvCount.toString()
                player.sendMessageInTranslatable("shop.messageSellShop.message")
                player.sendMessageInText(TranslatableComponent("shop.OwnerMessage.message").string + entity.tileData.getString("Owner"))
                player.sendMessageInText(TranslatableComponent("shop.getItem.message").string + entity.itemCommodity.displayName.string)
                player.sendMessageInText(TranslatableComponent("shop.getItemCount.message").string + invCount)
                player.sendMessageInText(TranslatableComponent("shop.price.message").string + entity.itemPrice)
                player.sendMessageInTranslatable("shop.buyerItemMessage.message")
                buyerEvent(player, entity)
            }
        }

        return InteractionResult.SUCCESS
    }

    open fun buyerEvent(player: Player, entity: BlockEntity) {
        Util.addSellBuyer(player, entity)
    }

    @Deprecated("Deprecated in Java")
    override fun getDrops(state: BlockState, builder: LootContext.Builder): List<ItemStack> {
        super.getDrops(state, builder)
        val list = ArrayList<ItemStack>()
        val entity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) as ShopEntity
        val item = entity.itemCommodity.copy()
        while (entity.itemInvCount > 64) {
            entity.itemInvCount -= 64
            item.count = 64
            list.add(item)
        }

        if (entity.itemInvCount > 0) {
            item.count = entity.itemInvCount
            list.add(item)
        }

        list.add(this.asItem().itemStack())
        return list
    }

    override fun setPlacedBy(world: Level, pos: BlockPos, state: BlockState, entity: LivingEntity?, item: ItemStack) {
        if (entity is Player)
            world.getBlockEntity(pos)!!.tileData.putString("Owner", entity.scoreboardName.toString())
    }

    override fun getRenderShape(blockState: BlockState): RenderShape {
        return RenderShape.MODEL
    }


    override fun isPathfindable(blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos, pathComputationType: PathComputationType): Boolean {
        return false
    }
}