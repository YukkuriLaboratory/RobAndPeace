package net.yukulab.robandpeace.datagen

import java.util.concurrent.CompletableFuture
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.RegistryWrapper
import net.yukulab.robandpeace.item.RapItems

class RapRecipeProvider(output: FabricDataOutput, registryFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) : FabricRecipeProvider(output, registryFuture) {
    override fun generate(exporter: RecipeExporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, RapItems.SMOKE)
            .pattern("BSB")
            .pattern("SFS")
            .pattern("BSB")
            .input('B', Items.BONE_MEAL)
            .input('S', Items.STRING)
            .input('F', Items.FIREWORK_STAR)
            .criterionHaveItem(Items.BONE_MEAL, Items.STRING, Items.FIREWORK_STAR)
            .offerTo(exporter)
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, RapItems.MAGIC_HAND)
            .pattern("GRG")
            .pattern(" B ")
            .pattern(" B ")
            .input('R', Items.REDSTONE)
            .input('G', Items.GOLD_INGOT)
            .input('B', Items.BONE)
            .criterionHaveItem(Items.REDSTONE, Items.GOLD_INGOT, Items.BONE)
            .offerTo(exporter)
    }

    companion object {
        private fun ShapedRecipeJsonBuilder.criterionHaveItem(vararg items: Item): ShapedRecipeJsonBuilder = also {
            items.forEach { item ->
                criterion(hasItem(item), conditionsFromItem(item))
            }
        }
    }
}
