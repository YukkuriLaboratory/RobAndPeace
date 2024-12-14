package net.yukulab.robandpeace.entity.portalhoop

import net.minecraft.client.render.LightmapTextureManager
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import net.yukulab.robandpeace.entity.RapEntityRenderers
import net.yukulab.robandpeace.entity.ThroughHoopPortal
import net.yukulab.robandpeace.entity.portalhoop.model.PortalOverlayModel
import net.yukulab.robandpeace.getTextureId
import qouteall.imm_ptl.core.CHelper
import qouteall.q_misc_util.my_util.DQuaternion

class PortalHoopEntityRenderer(context: EntityRendererFactory.Context) : EntityRenderer<ThroughHoopPortal>(context) {
    private val model: PortalOverlayModel = PortalOverlayModel(context.getPart(RapEntityRenderers.MODEL_PORTAL_LAYER))

    override fun render(entity: ThroughHoopPortal, yaw: Float, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light)

        // don't render overlay from back side
        if (!entity.isInFrontOfPortal(CHelper.getCurrentCameraPos())) {
            return
        }

        matrices.push()

        matrices.peek().normalMatrix.rotate(DQuaternion.rotationByDegrees(Vec3d(1.0, 0.0, 0.0), -90.0).toMcQuaternion())
        matrices.peek().positionMatrix.rotate(entity.orientationRotation.toMcQuaternion())

        matrices.translate(0.0, 0.0, PORTAL_OVERLAY_OFFSET)

        model.render(
            matrices,
            vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(getTexture(entity))),
            LightmapTextureManager.pack(15, 15),
            OverlayTexture.DEFAULT_UV,
            PORTAL_COLOR,
        )

        matrices.pop()
    }

    override fun getTexture(entity: ThroughHoopPortal): Identifier = if (entity.isVisible) {
        OVERLAY_FRAME
    } else OVERLAY_FILLED

    companion object {
        private val OVERLAY_FRAME = getTextureId("entity/overlay_frame.png")
        private val OVERLAY_FILLED = getTextureId("entity/overlay_filled.png")

        private val PORTAL_OVERLAY_OFFSET = 0.001 // this is bad hard code.
        private val PORTAL_COLOR = 0xE5E328
    }
}
