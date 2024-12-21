package net.yukulab.robandpeace.entity.hiddentreasure

import kotlin.math.min
import kotlin.math.sqrt
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.ColorHelper
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.random.Random
import net.yukulab.robandpeace.entity.HiddenTreasureEntity
import org.joml.Quaternionf
import org.joml.Vector3f

class HiddenTreasureEntityRenderer(context: EntityRendererFactory.Context) : EntityRenderer<HiddenTreasureEntity>(context) {
    val HALF_SQRT_3: Float = (sqrt(3.0) / 2.0).toFloat()
    override fun render(entity: HiddenTreasureEntity, yaw: Float, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int) {
        val m: Float = (entity.currentTick + 1) / 200.0f
        matrices.push()
        // y - 1, z - 2の位置に移動させられていたのでコメントアウト
        // matrices.translate(0.0f, -1.0f, -2.0f)
        renderDeathAnimation(
            matrices,
            m,
            vertexConsumers.getBuffer(RenderLayer.getDragonRays()),
        )
        renderDeathAnimation(
            matrices,
            m,
            vertexConsumers.getBuffer(RenderLayer.getDragonRaysDepth()),
        )
        // クラッシュしたのでコメントアウト
        // renderDeathAnimation(
        //     matrices,
        //     m,
        //     vertexConsumers.getBuffer(RenderLayer.getFastClouds()),
        // )
        matrices.pop()
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light)
    }

    override fun getTexture(entity: HiddenTreasureEntity): Identifier = Identifier.ofVanilla("textures/entity/elytra.png")

    /**
     * Render death animation
     * From Enderdragon renderer
     * @param matrices
     * @param animationProgress
     * @param vertexConsumer
     */
    private fun renderDeathAnimation(matrices: MatrixStack, animationProgress: Float, vertexConsumer: VertexConsumer) {
        matrices.push()
        val f = min((if (animationProgress > 0.8f) (animationProgress - 0.8f) / 0.2f else 0.0f).toDouble(), 1.0)
            .toFloat()
        val i = ColorHelper.Argb.fromFloats(1.0f - f, 1.0f, 1.0f, 1.0f)
        val j = 16711935
        val random = Random.create(432L)
        val vector3f = Vector3f()
        val vector3f2 = Vector3f()
        val vector3f3 = Vector3f()
        val vector3f4 = Vector3f()
        val quaternionf = Quaternionf()
        val k = MathHelper.floor((animationProgress + animationProgress * animationProgress) / 2.0f * 60.0f)

        for (l in 0 until k) {
            quaternionf.rotationXYZ(
                random.nextFloat() * (Math.PI * 2).toFloat(),
                random.nextFloat() * (Math.PI * 2).toFloat(),
                random.nextFloat() * (Math.PI * 2).toFloat(),
            )
                .rotateXYZ(
                    random.nextFloat() * (Math.PI * 2).toFloat(),
                    random.nextFloat() * (Math.PI * 2).toFloat(),
                    random.nextFloat() * (Math.PI * 2).toFloat() + animationProgress * (Math.PI / 2).toFloat(),
                )
            matrices.multiply(quaternionf)
            val g = random.nextFloat() * 20.0f + 5.0f + f * 10.0f
            val h = random.nextFloat() * 2.0f + 1.0f + f * 2.0f
            vector3f2[-HALF_SQRT_3 * h, g] = -0.5f * h
            vector3f3[HALF_SQRT_3 * h, g] = -0.5f * h
            vector3f4[0.0f, g] = h
            val entry = matrices.peek()
            vertexConsumer.vertex(entry, vector3f).color(i)
            vertexConsumer.vertex(entry, vector3f2).color(16711935)
            vertexConsumer.vertex(entry, vector3f3).color(16711935)
            vertexConsumer.vertex(entry, vector3f).color(i)
            vertexConsumer.vertex(entry, vector3f3).color(16711935)
            vertexConsumer.vertex(entry, vector3f4).color(16711935)
            vertexConsumer.vertex(entry, vector3f).color(i)
            vertexConsumer.vertex(entry, vector3f4).color(16711935)
            vertexConsumer.vertex(entry, vector3f2).color(16711935)
        }

        matrices.pop()
    }
}
