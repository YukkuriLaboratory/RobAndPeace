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
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, RapItems.ADVANCED_MAGIC_HAND)
            .pattern("DGD")
            .pattern(" B ")
            .pattern(" B ")
            .input('D', Items.DIAMOND)
            .input('G', Items.GHAST_TEAR)
            .input('B', Items.BLAZE_ROD)
            .criterionHaveItem(Items.DIAMOND, Items.GHAST_TEAR, Items.BLAZE_ROD)
            .offerTo(exporter)
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, RapItems.PICKING_TOOL)
            .pattern("I I")
            .pattern(" S ")
            .pattern("S S")
            .input('I', Items.IRON_NUGGET)
            .input('S', Items.STICK)
            .criterionHaveItem(Items.IRON_NUGGET, Items.STICK)
            .offerTo(exporter)
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, RapItems.TRIAL_PICKING_TOOL)
            .pattern("DGD")
            .pattern(" P ")
            .pattern(" K ")
            .input('D', Items.DIAMOND)
            .input('G', Items.GHAST_TEAR)
            .input('P', RapItems.PICKING_TOOL)
            .input('K', Items.TRIAL_KEY)
            .criterionHaveItem(Items.DIAMOND, RapItems.PICKING_TOOL, Items.TRIAL_KEY)
            .offerTo(exporter)
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, RapItems.SPIDER_WALKER)
            .pattern("III")
            .pattern("OWO")
            .pattern("LEL")
            .input('I', Items.IRON_INGOT)
            .input('O', Items.OBSIDIAN)
            .input('W', Items.COBWEB)
            .input('L', Items.LEATHER)
            .input('E', Items.ENDER_PEARL)
            .criterionHaveItem(Items.GOLD_INGOT, Items.OBSIDIAN, Items.ENDER_PEARL)
            .offerTo(exporter)
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, RapItems.PORTAL_HOOP)
            .pattern("DCD")
            .pattern("WGW")
            .pattern("DCD")
            .input('D', Items.DIAMOND)
            .input('C', Items.COPPER_BLOCK)
            .input('W', Items.WEATHERED_COPPER)
            .input('G', Items.GHAST_TEAR)
            .criterionHaveItem(Items.DIAMOND, Items.COPPER_BLOCK, Items.WEATHERED_COPPER, Items.GHAST_TEAR)
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
