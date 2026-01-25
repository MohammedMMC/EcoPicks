package dev.moma.ecopicks.recipe;

import dev.moma.ecopicks.EcoPicks;
import dev.moma.ecopicks.recipe.custom.EcoPickaxeRecipes;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {
    public static final RecipeSerializer<EcoPickaxeRecipes> UNIQUE_SHAPELESS = Registry.register(
            Registries.RECIPE_SERIALIZER,
            Identifier.of(EcoPicks.MOD_ID, "unique_shapeless"),
            new EcoPickaxeRecipes.Serializer());

    public static void registerModRecipes() {
        EcoPicks.LOGGER.info("Registering Recipes for " + EcoPicks.MOD_ID);
    }
}