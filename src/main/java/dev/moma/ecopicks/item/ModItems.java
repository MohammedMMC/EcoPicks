package dev.moma.ecopicks.item;

import dev.moma.ecopicks.EcoPicks;
import dev.moma.ecopicks.entity.ModEntities;
import dev.moma.ecopicks.item.custom.EcoPickaxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import java.util.HashMap;
import java.util.Map;

public class ModItems {
        public static final String[] ITEMS_NAMES = {
                        "acacia_pickaxe", "birch_pickaxe", "cherry_pickaxe", "dark_oak_pickaxe", "jungle_pickaxe",
                        "mangrove_pickaxe", "oak_pickaxe", "pale_oak_pickaxe", "spruce_pickaxe"
        };
        public static final Map<String, Item> ITEMS = new HashMap<>();

        public static final Item LEAVES_SHARD = registerItem("leaves_shard", new Item(new Item.Settings()));
        public static final Item LEAFEE_SPAWN_EGG = registerItem("leafee_spawn_egg",
                        new SpawnEggItem(ModEntities.LEAFEE, 0x50692c, 0xeb92c1, new Item.Settings()));
        public static final Item ECO_PICKAXE = registerItem("eco_pickaxe",
                        new EcoPickaxeItem(ModToolMaterials.LEAVES, new Item.Settings().attributeModifiers(
                                        PickaxeItem.createAttributeModifiers(ModToolMaterials.LEAVES, 0, 0))));

        public static void registerModItems() {
                EcoPicks.LOGGER.info("Registering Items for " + EcoPicks.MOD_ID);

                for (String itemName : ITEMS_NAMES) {
                        ITEMS.put(itemName, registerItem(itemName,
                                        new PickaxeItem(ModToolMaterials.LEAVES, new Item.Settings().attributeModifiers(
                                                        PickaxeItem.createAttributeModifiers(ModToolMaterials.LEAVES, 0,
                                                                        0)))));
                }
        }

        private static Item registerItem(String name, Item item) {
                return Registry.register(Registries.ITEM, Identifier.of(EcoPicks.MOD_ID, name), item);
        }
}
