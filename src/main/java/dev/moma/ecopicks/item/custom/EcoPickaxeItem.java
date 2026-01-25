package dev.moma.ecopicks.item.custom;

import java.util.Arrays;
import java.util.List;

import dev.moma.ecopicks.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;

public class EcoPickaxeItem extends PickaxeItem {
    public EcoPickaxeItem(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);

        NbtList combinedList = getDataList(stack);
        if (combinedList == null) {
            return;
        }

        tooltip.add(Text.translatable("tooltip.ecopicks.eco_pickaxe.level", combinedList.size()));

        Arrays.stream(ModItems.ITEMS_NAMES)
                .sorted((a, b) -> Boolean.compare(containsPickaxe(combinedList, b), containsPickaxe(combinedList, a)))
                .forEach(itemName -> {
                    tooltip.add(Text.literal((containsPickaxe(combinedList, itemName) ? "§7" : "§8")
                            + Text.translatable("item.ecopicks." + itemName).getString()));
                });
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        stack.set(DataComponentTypes.MAX_DAMAGE, 200 * getPickaxeLevel(stack));
        return super.getItemBarStep(stack);
    }

    @Override
    public float getMiningSpeed(ItemStack stack, BlockState state) {
        ToolComponent toolComponent = (ToolComponent) stack.get(DataComponentTypes.TOOL);
        return (toolComponent != null ? toolComponent.getSpeed(state) : 1.0F) * getPickaxeLevel(stack);
    }

    private boolean containsPickaxe(NbtList combinedList, String pickaxeId) {
        return combinedList.stream().anyMatch(element -> element.asString().contains(pickaxeId));
    }

    private int getPickaxeLevel(ItemStack stack) {
        NbtList combinedList = getDataList(stack);
        return combinedList != null ? combinedList.size() : 0;
    }

    private NbtList getDataList(ItemStack stack) {
        NbtComponent customData = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (customData == null) {
            return null;
        }

        NbtList combinedList = customData.copyNbt().getList("CombinedPickaxes", NbtElement.STRING_TYPE);
        if (combinedList.isEmpty()) {
            return null;
        }

        return combinedList;
    }
}
