package dev.moma.ecopicks.item;

import dev.moma.ecopicks.datagen.ModItemTagProvider;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;

public class ModToolMaterial {
    public static final ToolMaterial LEAVES = new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, 250, 6.0F, 2.0F, 14,
            ModItemTagProvider.ECO_SHARD);
    public static final ToolMaterial ECO_LEAVES = new ToolMaterial(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 1561, 8.0F,
            3.0F, 10, ModItemTagProvider.ECO_SHARD);
}