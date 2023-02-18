package club.someoneice.shopping

import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack

class ItemDict {
    private val itemMap = HashMap<ItemStack, Int>()

    fun put(item: ItemStack, count: Int) {
        if (this.itemMap.containsKey(item))
            this.itemMap[item] = this.itemMap[item]!! + count
        else this.itemMap[item] = count
    }

    fun get(item: ItemStack): Int {
        return if (this.itemMap.containsKey(item)) this.itemMap[item]!! else -1
    }

    fun remove(item: ItemStack) {
        this.itemMap.remove(item)
    }

    fun getItem(): ItemStack {
        return this.itemMap.keys.iterator().next()
    }

    fun size(): Int {
        return this.itemMap.size
    }

    fun save(): CompoundTag {
        val tag = CompoundTag()
        var k = 0
        for (i in this.itemMap.keys) {
            tag.put("item$k", i.save(CompoundTag()))
            tag.putInt("itemCount$k", itemMap[i]!!)
            k += 1
        }

        tag.putInt("listSize", k)
        return tag
    }

    fun load(tag: CompoundTag) {
        if (tag.contains("listSize"))
            for (k in 0..tag.getInt("listSize")) this.itemMap[ItemStack.of(tag.getCompound("item$k"))] = tag.getInt("itemCount$k")
    }
}

/*
    class ItemDict {
        val itemList = LinkedList<ItemStack>()
        val itemCountList = LinkedList<Int>()

        fun put(item: ItemStack, count: Int) {
            if (itemList.contains(item)) {

            } else {
                itemList.addLast(item)
                itemCountList.addLast(count)
            }
        }

        fun getItem(invNum: Int): ItemStack {
            return itemList[invNum]
        }

        fun getItemCount(item: ItemStack): Int {
            var inv = 0
            for (it in itemList) {
                if (it.sameItem(item)) return itemCountList[inv]
                else inv += 1
            }

            return -1
        }

        fun getItemInv(item: ItemStack): Int {
            fun getItemCount(item: ItemStack): Int {
                var inv = 0
                for (it in itemList) {
                    if (it.sameItem(item)) return inv
                    else inv += 1
                }

                return -1
            }
        }

        fun remove(int: Int) {
            itemList[int] = itemList.last
            itemCountList[int] = itemCountList.last

            itemList.removeLast()
            itemCountList.removeLast()
        }

        fun getLastItem(): ItemStack {
            return itemList.last
        }

        fun getLastItemCount(): Int {
            return itemCountList.last
        }
    }
    */