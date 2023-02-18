package club.someoneice.shopping

import club.someoneice.shopping.block.shop.ShopBlock
import club.someoneice.shopping.block.shop.ShopEntity
import club.someoneice.shopping.client.ClientHelper
import club.someoneice.shopping.init.ItemList
import net.minecraft.network.chat.TextComponent
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.world.BlockEvent.BreakEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.items.CapabilityItemHandler

@EventBusSubscriber
class ShoppingEvent {
    @SubscribeEvent
    fun onChatListener(event: ServerChatEvent) {
        if (Util.Helper.isNotEmpty() && Util.Helper.containsKey(event.player)) {
            try {
                if (Util.Helper[event.player] is Util.Seller) {
                    val seller: Util.Seller = Util.Helper[event.player]!! as Util.Seller
                    val price: Double = event.message.toBigDecimal().toDouble()

                    (seller.entity as ShopEntity).itemCommodity = seller.item
                    seller.entity.itemPrice = price

                    seller.entity.markDirty()
                    event.player.sendMessageInText("Success!")
                } else if (Util.Helper[event.player] is Util.Buyer) {
                    val seller = Util.Helper[event.player] as Util.Buyer
                    var num = event.message.toInt()
                    val entity = seller.entity as ShopEntity

                    if (!seller.isSellSeller) {
                        if (event.player.getMoney() < (entity.itemPrice * num)) {
                            event.player.sendMessageInTranslatable("shop.noMoney.message")
                        } else if (!seller.isAdminShop && entity.itemInvCount < num) {
                            event.player.sendMessageInTranslatable("shop.noItem.message")
                        } else {
                            event.player.deductMoney(entity.itemPrice * num)
                            if (!seller.isAdminShop) {
                                entity.money += entity.itemPrice * num
                                entity.itemInvCount -= num
                            }
                            entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent {
                                val item = it.getStackInSlot(0).copy()

                                while (num > 64) {
                                    num -= 64
                                    item.count = 64
                                    event.player.inventory.add(item)
                                }

                                if (num > 0) {
                                    item.count = num
                                    event.player.inventory.add(item)
                                }
                            }
                        }
                    } else {
                        var i = 0
                        for (item in event.player.inventory.items) {
                            if (item.sameItem(entity.itemCommodity)) {
                                i += item.count
                            }
                        }

                        if (i < num) {
                            event.player.sendMessageInTranslatable("shop.invNoItem.message")
                        } else if (!seller.isAdminShop && entity.money < num * entity.itemPrice) {
                            event.player.sendMessageInTranslatable("shop.ownerNoMoney.message")
                        } else {
                            event.player.addMoney(entity.itemPrice * num)
                            if (!seller.isAdminShop) {
                                entity.money -= entity.itemPrice * num
                                entity.itemInvCount += num
                            }

                            for (item in event.player.inventory.items) {
                                if (item.sameItem(entity.itemCommodity)) {
                                    if (num > item.count) {
                                        num -= item.count
                                        item.count = 0
                                    } else {
                                        item.count -= num
                                        num = 0
                                    }

                                    if (num <= 0) break
                                }
                            }

                            event.player.sendMessageInText(TranslatableComponent("shop.getMoney.message").string + event.player.getMoney())
                        }
                    }
                    entity.markDirty()
                    Util.Helper.remove(event.player)
                    event.isCanceled = true
                    return
                } else if (Util.Helper[event.player] is Util.Cheque) {
                    val price: Double = event.message.toBigDecimal().toDouble()
                    val tax = if (Config.isATMShouldTax.get()) price * Config.theTaxRate.get() else 0.0
                    if (price + tax > event.player.getMoney()) {
                        event.player.sendMessageInTranslatable("shop.noMoney.message")
                        Util.Helper.remove(event.player)
                        event.isCanceled = true
                        return
                    }

                    if (event.player.inventory.contains(Items.PAPER.itemStack())) {
                        for (item in event.player.inventory.items) {
                            if (item.sameItem(Items.PAPER.itemStack())) {
                                item.count -= 1
                                val itemCheque: ItemStack = ItemList.Cheque.itemStack()
                                itemCheque.orCreateTag.putDouble("eco_money", price)
                                event.player.inventory.add(itemCheque)
                                event.player.deductMoney(price + tax)
                                break
                            }
                        }
                    }
                } else if (Util.Helper[event.player] is Util.SellerShopHelper) {
                    val seller: Util.SellerShopHelper = Util.Helper[event.player] as Util.SellerShopHelper
                    (seller.entity as ShopEntity).money += event.message.toBigDecimal().toDouble()
                    event.player.sendMessageInText(TranslatableComponent("shop.shopHaveMoney.message").string + (seller.entity as ShopEntity).money)
                }

                Util.Helper.remove(event.player)
                event.isCanceled = true
            } catch (_: NumberFormatException) {
                event.player.sendMessage(TextComponent("Not int or other error."), event.player.uuid)
            } finally {
                Util.Helper.remove(event.player)
                event.isCanceled = true
            }
        }
    }

    @SubscribeEvent
    fun onPlayerBreakBlock(event: BreakEvent) {
        if (event.state.block is ShopBlock) {
            val entity = event.world.getBlockEntity(event.pos) as ShopEntity
            if (!event.player.isCreative && entity.tileData.getString("Owner") != event.player.scoreboardName.toString()) {
                event.isCanceled = true
                event.player.sendMessageInTranslatable("shop.cannotBreakShop.message")
            } else if (entity.tileData.getString("Owner") == event.player.scoreboardName.toString()) {
                event.player.addMoney(entity.money)
            }
        }
    }

    @SubscribeEvent
    fun onEndTick(event: TickEvent.ClientTickEvent?) {
        ClientHelper.tick += 1
        if (ClientHelper.tick == Int.MAX_VALUE)
            ClientHelper.tick = Int.MIN_VALUE
    }
}