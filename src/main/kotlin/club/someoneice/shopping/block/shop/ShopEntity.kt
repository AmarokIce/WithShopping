package club.someoneice.shopping.block.shop

import club.someoneice.shopping.init.BlockEntityList
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import javax.annotation.Nullable

open class ShopEntity(pos: BlockPos, state: BlockState): BlockEntity(BlockEntityList.ShopBlock, pos, state) {
    var itemCommodity: ItemStack = ItemStack.EMPTY
    var itemInvCount: Int = 0
    var itemPrice: Double = 0.0
    var money: Double = 0.0

    override fun load(tag: CompoundTag) {
        super.load(tag)
        itemCommodity = ItemStack.of(tag.getCompound("item_commodity"))
        itemInvCount = tag.getInt("item_inv_count")
        itemPrice = tag.getDouble("price")
        money = tag.getDouble("inv_money")
    }

    override fun saveAdditional(tag: CompoundTag) {
        tag.put("item_commodity", itemCommodity.save(CompoundTag()))
        tag.putInt("item_inv_count", itemInvCount)
        tag.putDouble("price", itemPrice)
        tag.putDouble("inv_money", money)
        super.saveAdditional(tag)
    }

    @Nullable
    override fun getUpdatePacket(): Packet<ClientGamePacketListener?>? {
        return ClientboundBlockEntityDataPacket.create(this)
    }

    override fun getUpdateTag(): CompoundTag {
        val tag = CompoundTag()
        tag.put("item_commodity", itemCommodity.save(CompoundTag()))
        tag.putInt("item_inv_count", itemInvCount)
        tag.putDouble("price", itemPrice)
        tag.putDouble("inv_money", money)
        return tag
    }

    open fun markDirty() {
        this.setChanged()
        this.getLevel()!!.sendBlockUpdated(this.blockPos, blockState, blockState, 3)
    }
}