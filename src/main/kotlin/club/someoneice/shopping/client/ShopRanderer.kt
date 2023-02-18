package club.someoneice.shopping.client

import club.someoneice.shopping.block.shop.ShopEntity
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Vector3f
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity


class ShopRanderer<T : ShopEntity>: BlockEntityRenderer<T>, BlockEntityRendererProvider<T> {
    private var item: ItemEntity? = null

    override fun create(context: BlockEntityRendererProvider.Context): BlockEntityRenderer<T> {
        return this
    }

    override fun render(entity: T, f: Float, pose: PoseStack, buffer: MultiBufferSource, i: Int, k: Int) {
        if (item == null) item = ItemEntity(entity.level!!, entity.blockPos.x.toDouble(), entity.blockPos.y.toDouble(), entity.blockPos.z.toDouble(), entity.itemCommodity)
        pose.pushPose()
        pose.translate(0.5, 0.25, 0.5)
        pose.scale(0.8f, 0.8f, 0.8f)
        item?.item = entity.itemCommodity
        if (item != null)
            Minecraft.getInstance().entityRenderDispatcher.getRenderer<Entity>(item!!).render(item, f, ClientHelper.tick.toFloat(), pose, buffer, i)

        pose.pushPose()
        pose.translate(-0.350, -0.55, -0.74)
        pose.mulPose(Vector3f.XP.rotation(22.4f))
        pose.mulPose(Vector3f.ZP.rotation(22f))
        pose.scale(0.4f, 0.4f, 0.1f)

        pose.popPose()
        pose.popPose()
    }
}