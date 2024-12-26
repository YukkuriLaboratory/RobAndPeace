package net.yukulab.robandpeace.entity

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.model.EntityModelLayer
import net.minecraft.client.render.entity.model.IronGolemEntityModel
import net.minecraft.util.Identifier
import net.yukulab.robandpeace.MOD_ID
import net.yukulab.robandpeace.entity.angrygolem.AngryGolemEntityRenderer
import net.yukulab.robandpeace.entity.hiddentreasure.HiddenTreasureEntityRenderer
import net.yukulab.robandpeace.entity.portalhoop.PortalHoopEntityRenderer
import net.yukulab.robandpeace.entity.portalhoop.model.PortalOverlayModel

object RapEntityRenderers {
    val MODEL_ANGRY_GOLEM_LAYER = EntityModelLayer(Identifier.of(MOD_ID, "angry_golem"), "main")
    val MODEL_PORTAL_LAYER = EntityModelLayer(Identifier.of(MOD_ID, "portal_overlay"), "main")

    fun init() {
        EntityRendererRegistry.register(RapEntityType.ANGRY_GOLEM, ::AngryGolemEntityRenderer)
        EntityModelLayerRegistry.registerModelLayer(MODEL_ANGRY_GOLEM_LAYER) { IronGolemEntityModel.getTexturedModelData() }

        // Portal Hoop entities
        EntityRendererRegistry.register(RapEntityType.THROUGH_HOOP_PORTAL, ::PortalHoopEntityRenderer)
        EntityModelLayerRegistry.registerModelLayer(MODEL_PORTAL_LAYER) { PortalOverlayModel.getTexturedModelData() }

        // Hidden Treasure's fake entities
        EntityRendererRegistry.register(RapEntityType.HIDDEN_TREASURE_ENTITY, ::HiddenTreasureEntityRenderer)
    }
}
