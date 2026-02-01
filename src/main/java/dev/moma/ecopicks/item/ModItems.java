package dev.moma.ecopicks.item;

import dev.moma.ecopicks.EcoPicks;
import dev.moma.ecopicks.entity.ModEntities;
import dev.moma.ecopicks.item.custom.EcoPickaxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import java.util.HashMap;
import java.util.Map;

public class ModItems {
        public static final String[] ITEMS_NAMES = {
                        "acacia_pickaxe", "birch_pickaxe", "cherry_pickaxe", "dark_oak_pickaxe", "jungle_pickaxe",
                        "mangrove_pickaxe", "oak_pickaxe", "pale_oak_pickaxe", "spruce_pickaxe"
        };
        public static final Map<String, Item> ITEMS = new HashMap<>();

        public static final Item LEAVES_SHARD = registerItem("leaves_shard", new Item(new Item.Settings().registryKey(getKey("leaves_shard"))));
        public static final Item LEAFEE_SPAWN_EGG = registerItem("leafee_spawn_egg",
                        new SpawnEggItem(ModEntities.LEAFEE, new Item.Settings().registryKey(getKey("leafee_spawn_egg"))));
        public static final Item ECO_PICKAXE = registerItem("eco_pickaxe",
                        new EcoPickaxeItem(ModToolMaterial.ECO_LEAVES, new Item.Settings().registryKey(getKey("eco_pickaxe"))));

        public static void registerModItems() {
                EcoPicks.LOGGER.info("Registering Items for " + EcoPicks.MOD_ID);

                for (String itemName : ITEMS_NAMES) {
                        ITEMS.put(itemName, registerItem(itemName,
                                        new PickaxeItem(ModToolMaterial.LEAVES, 0, 0, new Item.Settings().registryKey(getKey(itemName)))));
                }
        }

        private static Item registerItem(String name, Item item) {
                return Registry.register(Registries.ITEM, Identifier.of(EcoPicks.MOD_ID, name), item);
        }
        private static RegistryKey<Item> getKey(String name) {
                RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM,
                                Identifier.of(EcoPicks.MOD_ID, name));
                return itemKey;
        }
}
