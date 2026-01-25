package dev.moma.ecopicks.datagen;

import java.util.concurrent.CompletableFuture;
import dev.moma.ecopicks.EcoPicks;
import dev.moma.ecopicks.recipe.custom.EcoPickaxeRecipes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ItemStack ecoPickaxe = new ItemStack(Registries.ITEM.get(Identifier.of(EcoPicks.MOD_ID, "eco_pickaxe")));

        for (int count = 2; count <= 9; count++) {
            DefaultedList<Ingredient> ingredients = DefaultedList.of();
            for (int i = 0; i < count; i++) {
                ingredients.add(Ingredient.fromTag(ModItemTagProvider.ECO_PICKAXE_INGREDIENTS));
            }

            exporter.accept(
                    Identifier.of(EcoPicks.MOD_ID, "eco_pickaxe_from_" + count + "_pickaxes"),
                    new EcoPickaxeRecipes(
                            CraftingRecipeCategory.EQUIPMENT,
                            ecoPickaxe,
                            ingredients),
                    null);
        }
    }
}
