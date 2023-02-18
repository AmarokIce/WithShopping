package club.someoneice.shopping.init

import club.someoneice.shopping.ShoppingMain
import club.someoneice.shopping.block.ATMBlock
import club.someoneice.shopping.block.shop.AdminShop
import club.someoneice.shopping.block.shop.SellerShopBlock
import club.someoneice.shopping.block.shop.ShopBlock
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.registerObject

object BlockList {
    val BLOCKS: DeferredRegister<Block> = DeferredRegister.create(ForgeRegistries.BLOCKS, ShoppingMain.MODID)
    private val ITEMS: DeferredRegister<Item> = ItemList.ITEMS

    val ShopBlock by BLOCKS.registerObject("glass_shop") { ShopBlock() }
    val AdminShopBlock by BLOCKS.registerObject("admin_shop") { AdminShop.AdminShopBlock() }

    val BuyerShopBlock by BLOCKS.registerObject("glass_shop_buyer") { SellerShopBlock() }
    val AdminBuyerShopBlock by BLOCKS.registerObject("admin_shop_buyer") { AdminShop.AdminShopSellBlock() }

    val ATMBlock by BLOCKS.registerObject("atm_block") {ATMBlock()}

    val ShopBlockItem by ITEMS.registerObject("glass_shop_item") { BlockItem(ShopBlock, Item.Properties().tab(ShoppingMain.TAB))}
    val AdminShopBlockItem by ITEMS.registerObject("admin_shop_item") { BlockItem(AdminShopBlock, Item.Properties().tab(ShoppingMain.TAB))}
    val BuyerShopBlockItem by ITEMS.registerObject("glass_shop_buyer_item") { BlockItem(BuyerShopBlock, Item.Properties().tab(ShoppingMain.TAB)) }
    val AdminBuyerShopBlockItem by ITEMS.registerObject("admin_shop_buyer_item") { BlockItem(AdminBuyerShopBlock, Item.Properties().tab(ShoppingMain.TAB)) }

    val ATMBlockItem by ITEMS.registerObject("atm_block_item") { BlockItem(ATMBlock, Item.Properties().tab(ShoppingMain.TAB))}
}