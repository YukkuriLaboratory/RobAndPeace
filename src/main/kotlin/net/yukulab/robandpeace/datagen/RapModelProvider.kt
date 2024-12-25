package net.yukulab.robandpeace.datagen

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.Model
import net.minecraft.data.client.ModelIds
import net.minecraft.data.client.Models
import net.minecraft.data.client.TextureMap
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
import net.yukulab.robandpeace.item.PickingToolItem
import net.yukulab.robandpeace.item.RapItems

class RapModelProvider(generator: FabricDataOutput) : FabricModelProvider(generator) {
    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) = Unit

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {
        itemModelGenerator.register(RapItems.SMOKE, Models.GENERATED)
        itemModelGenerator.register(RapItems.MAGIC_HAND, Models.GENERATED)
        itemModelGenerator.register(RapItems.ADVANCED_MAGIC_HAND, Models.GENERATED)
        itemModelGenerator.register(RapItems.PICKING_TOOL, Models.GENERATED) {
            overrides(
                Override(
                    ModelIds.getItemSubModelId(RapItems.PICKING_TOOL, PickingToolItem.SUFFIX_PICKING),
                    listOf(PickingToolItem.KEY_PICKING to 1.0),
                ),
            )
        }
        itemModelGenerator.register(RapItems.PICKING_TOOL, PickingToolItem.SUFFIX_PICKING, null, Models.GENERATED) {
            display(
                Display(
                    ModelTransformationMode.FIRST_PERSON_RIGHT_HAND,
                    rotation = Vec3i(-90, 0, -90),
                ),
                Display(
                    ModelTransformationMode.FIRST_PERSON_LEFT_HAND,
                    rotation = Vec3i(-90, 0, -90),
                ),
            )
        }
        itemModelGenerator.register(RapItems.TRIAL_PICKING_TOOL, Models.GENERATED) {
            overrides(
                Override(
                    ModelIds.getItemSubModelId(RapItems.TRIAL_PICKING_TOOL, PickingToolItem.SUFFIX_OMINOUS),
                    listOf(PickingToolItem.KEY_OMINOUS to 1.0),
                ),
                Override(
                    ModelIds.getItemSubModelId(RapItems.TRIAL_PICKING_TOOL, PickingToolItem.SUFFIX_PICKING),
                    listOf(PickingToolItem.KEY_PICKING to 1.0),
                ),
                Override(
                    ModelIds.getItemSubModelId(RapItems.TRIAL_PICKING_TOOL, PickingToolItem.SUFFIX_OMINOUS + PickingToolItem.SUFFIX_PICKING),
                    listOf(PickingToolItem.KEY_OMINOUS to 1.0, PickingToolItem.KEY_PICKING to 1.0),
                ),
            )
        }
        itemModelGenerator.register(RapItems.TRIAL_PICKING_TOOL, PickingToolItem.SUFFIX_OMINOUS, Models.GENERATED)
        itemModelGenerator.register(RapItems.TRIAL_PICKING_TOOL, PickingToolItem.SUFFIX_PICKING, null, Models.GENERATED) {
            display(
                Display(
                    ModelTransformationMode.FIRST_PERSON_RIGHT_HAND,
                    rotation = Vec3i(-90, 0, -90),
                ),
                Display(
                    ModelTransformationMode.FIRST_PERSON_LEFT_HAND,
                    rotation = Vec3i(-90, 0, -90),
                ),
            )
        }
        itemModelGenerator.register(
            RapItems.TRIAL_PICKING_TOOL,
            PickingToolItem.SUFFIX_OMINOUS + PickingToolItem.SUFFIX_PICKING,
            PickingToolItem.SUFFIX_OMINOUS,
            Models.GENERATED,
        ) {
            display(
                Display(
                    ModelTransformationMode.FIRST_PERSON_RIGHT_HAND,
                    rotation = Vec3i(-90, 0, -90),
                ),
                Display(
                    ModelTransformationMode.FIRST_PERSON_LEFT_HAND,
                    rotation = Vec3i(-90, 0, -90),
                ),
            )
        }
        itemModelGenerator.register(RapItems.SPIDER_WALKER, Models.GENERATED)
        itemModelGenerator.register(RapItems.PORTAL_HOOP, Models.GENERATED)
    }

    companion object {
        private fun ItemModelGenerator.register(item: Item, model: Model, jsonFactory: JsonObjectWrapper.() -> Unit): Identifier = model.upload(ModelIds.getItemModelId(item), TextureMap.layer0(item), writer) { id, textures ->
            model.createJson(
                id,
                textures,
            ).let(::JsonObjectWrapper).apply(jsonFactory).jsonObject
        }

        private fun ItemModelGenerator.register(item: Item, suffix: String, textureSuffix: String? = suffix, model: Model, jsonFactory: JsonObjectWrapper.() -> Unit): Identifier {
            val texture = if (textureSuffix != null) {
                TextureMap.layer0(TextureMap.getSubId(item, textureSuffix))
            } else {
                TextureMap.layer0(item)
            }
            return model.upload(ModelIds.getItemSubModelId(item, suffix), texture, writer) { id, textures ->
                model.createJson(
                    id,
                    textures,
                ).let(::JsonObjectWrapper).apply(jsonFactory).jsonObject
            }
        }
    }

    class JsonObjectWrapper(val jsonObject: JsonObject) {
        operator fun String.invoke(value: String) {
            jsonObject.addProperty(this, value)
        }

        operator fun String.invoke(value: Int) {
            jsonObject.addProperty(this, value)
        }

        operator fun String.invoke(value: Double) {
            jsonObject.addProperty(this, value)
        }

        operator fun String.invoke(value: Vec3i) {
            val array = JsonArray()
            array.add(value.x)
            array.add(value.y)
            array.add(value.z)
            jsonObject.add(this, array)
        }

        operator fun String.invoke(value: Vec3d) {
            val array = JsonArray()
            array.add(value.x)
            array.add(value.y)
            array.add(value.z)
            jsonObject.add(this, array)
        }

        operator fun String.invoke(value: Identifier) {
            jsonObject.addProperty(this, value.toString())
        }

        operator fun String.invoke(value: JsonObjectWrapper.() -> Unit) {
            jsonObject.add(this, JsonObjectWrapper(JsonObject()).apply(value).jsonObject)
        }

        infix fun String.array(values: List<JsonObjectWrapper.() -> Unit>) {
            val array = JsonArray()
            values.forEach {
                array.add(JsonObjectWrapper(JsonObject()).apply(it).jsonObject)
            }
            jsonObject.add(this, array)
        }

        operator fun Identifier.invoke(value: Int) {
            this.toString()(value)
        }

        operator fun Identifier.invoke(value: Double) {
            this.toString()(value)
        }

        fun overrides(vararg values: Override) {
            "overrides" array values.map { override ->
                (
                    {
                        "predicate" {
                            override.predicate.forEach { (key, value) ->
                                key(value)
                            }
                        }
                        "model"(override.model)
                    }
                    )
            }
        }

        fun display(vararg displays: Display) {
            "display" {
                displays.forEach { display ->
                    val name = display.transformation.asString()
                    name {
                        if (display.rotation != null) {
                            "rotation"(display.rotation)
                        }
                        if (display.translation != null) {
                            "translation"(display.translation)
                        }
                        if (display.scale != null) {
                            "scale"(display.scale)
                        }
                    }
                }
            }
        }
    }

    data class Display(
        val transformation: ModelTransformationMode,
        val rotation: Vec3i? = null,
        val translation: Vec3d? = null,
        val scale: Vec3d? = null,
    )

    data class Override(val model: Identifier, val predicate: List<Pair<Identifier, Double>>)
}
