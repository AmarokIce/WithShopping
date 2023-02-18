package club.someoneice.shopping

import club.someoneice.shopping.client.ClientHelper
import club.someoneice.shopping.init.BlockEntityList
import club.someoneice.shopping.init.BlockList
import club.someoneice.shopping.init.ItemList
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(ShoppingMain.MODID)
object ShoppingMain {
    const val MODID = "go_shopping"

    val TAB: CreativeModeTab = object : CreativeModeTab("with_shopping") {
        override fun makeIcon(): ItemStack {
            return ItemList.Cheque.itemStack()
        }
    }

    init {
        MinecraftForge.EVENT_BUS.register(this)
        MinecraftForge.EVENT_BUS.register(ShoppingEvent())

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.config)

        val bus = MOD_BUS
        ItemList.ITEMS.register(bus)
        BlockList.BLOCKS.register(bus)
        BlockEntityList.BLOCK_ENTITY.register(bus)

        bus.addListener(ClientHelper::registerRenderers);
        bus.addListener(ClientHelper::layout);
    }
}