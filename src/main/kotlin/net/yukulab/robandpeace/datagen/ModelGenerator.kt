package net.yukulab.robandpeace.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.Models
import net.yukulab.robandpeace.item.RapItems

class ModelGenerator(generator: FabricDataOutput) : FabricModelProvider(generator) {
    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {
    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {
        itemModelGenerator.register(RapItems.SMOKE, Models.GENERATED)
    }
}
