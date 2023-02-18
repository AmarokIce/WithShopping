package club.someoneice.shopping.init

import club.someoneice.shopping.ShoppingMain
import club.someoneice.shopping.item.Cheque
import club.someoneice.shopping.item.ChequeInCrivate
import club.someoneice.shopping.item.ShoppingPackage
import net.minecraft.world.item.Item
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.registerObject

object ItemList {
    val ITEMS: DeferredRegister<Item> = DeferredRegister.create(ForgeRegistries.ITEMS, ShoppingMain.MODID)

    val Cheque by ITEMS.registerObject("cheque") { Cheque() }
    val CrivateCheque by ITEMS.registerObject("creative_cheque") { ChequeInCrivate() }

    // val ShoppingPackage by ITEMS.registerObject("shopping_package") { ShoppingPackage() }
}