package club.someoneice.shopping.init

import club.someoneice.shopping.ShoppingMain
import club.someoneice.shopping.block.shop.ShopEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.registerObject

object BlockEntityList {
    val BLOCK_ENTITY: DeferredRegister<BlockEntityType<*>> = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, ShoppingMain.MODID)

    val ShopBlock: BlockEntityType<ShopEntity> by BLOCK_ENTITY.registerObject("shop_block_entity") {
        BlockEntityType.Builder.of({ pos: BlockPos, state: BlockState -> ShopEntity(pos, state) }, BlockList.ShopBlock, BlockList.AdminShopBlock, BlockList.BuyerShopBlock, BlockList.AdminBuyerShopBlock).build(null)
    }
}