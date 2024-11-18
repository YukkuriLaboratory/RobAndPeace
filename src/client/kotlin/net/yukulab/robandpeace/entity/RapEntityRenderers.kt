package net.yukulab.robandpeace.entity

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.model.EntityModelLayer
import net.minecraft.client.render.entity.model.IronGolemEntityModel
import net.minecraft.util.Identifier
import net.yukulab.robandpeace.MOD_ID
import net.yukulab.robandpeace.entity.angrygolem.AngryGolemEntityRenderer

object RapEntityRenderers {
    val MODEL_ANGRY_GOLEM_LAYER = EntityModelLayer(Identifier.of(MOD_ID, "angry_golem"), "main")

    fun init() {
        EntityRendererRegistry.register(RapEntityType.ANGRY_GOLEM, ::AngryGolemEntityRenderer)
        EntityModelLayerRegistry.registerModelLayer(MODEL_ANGRY_GOLEM_LAYER) { IronGolemEntityModel.getTexturedModelData() }
    }
}
