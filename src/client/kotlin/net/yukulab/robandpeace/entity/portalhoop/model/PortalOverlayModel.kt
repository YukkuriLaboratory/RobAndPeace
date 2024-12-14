package net.yukulab.robandpeace.entity.portalhoop.model

import net.minecraft.client.model.Dilation
import net.minecraft.client.model.ModelData
import net.minecraft.client.model.ModelPart
import net.minecraft.client.model.ModelPartBuilder
import net.minecraft.client.model.ModelPartData
import net.minecraft.client.model.ModelTransform
import net.minecraft.client.model.TexturedModelData
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.yukulab.robandpeace.entity.ThroughHoopPortal

class PortalOverlayModel(root: ModelPart) : EntityModel<ThroughHoopPortal>() {
    private val base: ModelPart = root.getChild("base")
    private val cube_r1: ModelPart = root.getChild("cube_r1")

    override fun render(matrices: MatrixStack?, vertices: VertexConsumer?, light: Int, overlay: Int, color: Int) {
        base.render(matrices, vertices, light, overlay, color)
    }

    override fun setAngles(entity: ThroughHoopPortal?, limbAngle: Float, limbDistance: Float, animationProgress: Float, headYaw: Float, headPitch: Float) {
        // Nothing to do here
    }

    companion object {
        fun getModelData(): ModelData {
            val modelData = ModelData()
            val modelPartData: ModelPartData = modelData.root

            val base: ModelPartData = modelPartData.addChild(
                "base",
                ModelPartBuilder.create(),
                ModelTransform.of(0.0f, 16F, 0.0f, 0.0f, 0.0f, 0.0f),
            )

            val cube_r1: ModelPartData = base.addChild(
                "cube_r1",
                ModelPartBuilder.create()
                    .uv(0, 16)
                    .cuboid(
                        -8.0f,
                        -16.0f,
                        0.0f,
                        16.0f,
                        32.0f,
                        0.0f,
                        Dilation(0.0f),
                    ).mirrored(true),
                ModelTransform.of(0.0f, -16.0f, 0.0f, 0.0f, Math.PI.toFloat(), 0.0f),
            )
            return modelData
        }

        fun getTexturedModelData(): TexturedModelData = TexturedModelData.of(getModelData(), 32, 64)
    }
}
