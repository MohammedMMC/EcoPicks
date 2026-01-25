package dev.moma.ecopicks.datagen;

import java.util.concurrent.CompletableFuture;

import dev.moma.ecopicks.EcoPicks;
import dev.moma.ecopicks.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.ItemTagProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModItemTagProvider extends ItemTagProvider {
    public static final TagKey<Item> ECO_PICKAXE_INGREDIENTS = TagKey.of(RegistryKeys.ITEM,
            Identifier.of(EcoPicks.MOD_ID, "eco_pickaxe_ingredients"));

    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(WrapperLookup wrapperLookup) {
        var builder = getOrCreateTagBuilder(ECO_PICKAXE_INGREDIENTS);
        for (String itemName : ModItems.ITEMS_NAMES) {
            builder.add(Registries.ITEM.get(Identifier.of(EcoPicks.MOD_ID, itemName)));
        }
        builder.add(Registries.ITEM.get(Identifier.of(EcoPicks.MOD_ID, "eco_pickaxe")));
    }

}
