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
import net.yukulab.robandpeace.item.RapItems

class RapModelProvider(generator: FabricDataOutput) : FabricModelProvider(generator) {
    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {
    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {
        itemModelGenerator.register(RapItems.SMOKE, Models.GENERATED)
        itemModelGenerator.register(RapItems.MAGIC_HAND, Models.GENERATED)
        itemModelGenerator.register(RapItems.PICKING_TOOL, Models.GENERATED) {
            "overrides" array listOf {
                "predicate" {
                    "tooting"(1)
                }
                "model"(ModelIds.getItemSubModelId(RapItems.PICKING_TOOL, "_tooting"))
            }
        }
        itemModelGenerator.register(RapItems.PICKING_TOOL, "_tooting", Models.GENERATED) {
            "display" {
                ModelTransformationMode.FIRST_PERSON_RIGHT_HAND(
                    rotation = Vec3i(0, -55, -5),
                    translation = Vec3d(-1.0, -2.5, -7.5),
                )
                ModelTransformationMode.FIRST_PERSON_LEFT_HAND(
                    rotation = Vec3i(0, 115, 5),
                    translation = Vec3d(0.0, -2.5, -7.5),
                )
            }
        }
    }

    companion object {
        private fun ItemModelGenerator.register(item: Item, model: Model, jsonFactory: JsonObjectWrapper.() -> Unit): Identifier = model.upload(ModelIds.getItemModelId(item), TextureMap.layer0(item), writer) { id, textures ->
            model.createJson(
                id,
                textures,
            ).let(::JsonObjectWrapper).apply(jsonFactory).jsonObject
        }

        private fun ItemModelGenerator.register(item: Item, suffix: String, model: Model, jsonFactory: JsonObjectWrapper.() -> Unit): Identifier = model.upload(ModelIds.getItemSubModelId(item, suffix), TextureMap.layer0(item), writer) { id, textures ->
            model.createJson(
                id,
                textures,
            ).let(::JsonObjectWrapper).apply(jsonFactory).jsonObject
        }
    }

    class JsonObjectWrapper(val jsonObject: JsonObject) {
        operator fun String.invoke(value: String) {
            jsonObject.addProperty(this, value)
        }

        operator fun String.invoke(value: Int) {
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

        operator fun ModelTransformationMode.invoke(
            rotation: Vec3i = Vec3i(0, 0, 0),
            translation: Vec3d = Vec3d(0.0, 0.0, 0.0),
            scale: Vec3d = Vec3d(1.0, 1.0, 1.0),
        ) {
            val name = asString()
            name {
                "rotation"(rotation)
                "translation"(translation)
                "scale"(scale)
            }
        }
    }
}
