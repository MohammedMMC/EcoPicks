package dev.moma.ecopicks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.moma.ecopicks.entity.ModEntities;
import dev.moma.ecopicks.entity.custom.LeafeeEntity;
import dev.moma.ecopicks.item.ModItems;
import dev.moma.ecopicks.screen.ModScreenHandlers;
import dev.moma.ecopicks.recipe.ModRecipes;

public class EcoPicks implements ModInitializer {
	public static final String MOD_ID = "ecopicks";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static ItemGroup ECO_PICKS_GROUP = Registry.register(Registries.ITEM_GROUP,
			Identifier.of(MOD_ID, "eco_picks_group"),
			FabricItemGroup.builder()
					.icon(() -> new ItemStack(ModItems.ITEMS.get("cherry_pickaxe")))
					.displayName(Text.translatable("itemGroup.ecopicks.eco_picks_group"))
					.entries((ctx, entries) -> {
						for (String item : ModItems.ITEMS.keySet()) {
							entries.add(ModItems.ITEMS.get(item));
						}
						entries.add(ModItems.LEAVES_SHARD);
						entries.add(ModItems.ECO_PICKAXE);
						entries.add(ModItems.LEAFEE_SPAWN_EGG);
					})
					.build());

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing EcoPicks Mod!");

		ModScreenHandlers.registerScreenHandlers();
		ModEntities.registerModEntities();
		ModItems.registerModItems();
		ModRecipes.registerModRecipes();

		FabricDefaultAttributeRegistry.register(ModEntities.LEAFEE, LeafeeEntity.createAttributes());
	}
}