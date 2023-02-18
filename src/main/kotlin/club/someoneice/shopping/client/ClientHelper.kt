package club.someoneice.shopping.client

import club.someoneice.shopping.init.BlockEntityList
import club.someoneice.shopping.init.BlockList
import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.client.renderer.RenderType
import net.minecraftforge.client.event.EntityRenderersEvent
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent


object ClientHelper {
    var tick: Int = 0

    fun registerRenderers(event: EntityRenderersEvent.RegisterRenderers) {
        event.registerBlockEntityRenderer(BlockEntityList.ShopBlock, ShopRanderer())
    }

    fun layout(event: FMLClientSetupEvent) {
        ItemBlockRenderTypes.setRenderLayer(BlockList.ShopBlock,            RenderType.cutout())
        ItemBlockRenderTypes.setRenderLayer(BlockList.AdminShopBlock,       RenderType.cutout())
        ItemBlockRenderTypes.setRenderLayer(BlockList.BuyerShopBlock,       RenderType.cutout())
        ItemBlockRenderTypes.setRenderLayer(BlockList.AdminBuyerShopBlock,  RenderType.cutout())
    }

}