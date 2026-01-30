package dev.moma.ecopicks.recipe.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import dev.moma.ecopicks.recipe.ModRecipes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EcoPickaxeRecipes implements CraftingRecipe {
    final CraftingRecipeCategory category;
    final ItemStack result;
    final DefaultedList<Ingredient> ingredients;

    public EcoPickaxeRecipes(CraftingRecipeCategory category, ItemStack result,
            DefaultedList<Ingredient> ingredients) {
        this.category = category;
        this.result = result;
        this.ingredients = ingredients;
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        if (input.getStackCount() < ingredients.size())
            return false;

        Set<ItemStack> usedStacks = new HashSet<>();
        List<Ingredient> remaining = new ArrayList<>(ingredients);

        for (int i = 0; i < input.getStackCount(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (stack.isEmpty())
                continue;

            if (usedStacks.stream().anyMatch(s -> ItemStack.areItemsEqual(s, stack))) {
                return false;
            }

            boolean matched = false;
            for (int j = 0; j < remaining.size(); j++) {
                if (remaining.get(j).test(stack)) {
                    usedStacks.add(stack.copy());
                    remaining.remove(j);
                    matched = true;
                    break;
                }
            }
            if (!matched && !stack.isEmpty())
                return false;
        }

        if (!remaining.isEmpty()) {
            return false;
        }

        ItemStack ecoStack = ItemStack.EMPTY;
        for (int i = 0; i < input.getStackCount(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (!stack.isEmpty() && isEcoPickaxe(stack)) {
                ecoStack = stack;
                break;
            }
        }

        if (ecoStack.isEmpty()) {
            return true;
        }

        NbtList existingCombined = getCombinedList(ecoStack);
        if (existingCombined == null) {
            return true;
        }

        for (int i = 0; i < input.getStackCount(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (stack.isEmpty() || isEcoPickaxe(stack)) {
                continue;
            }

            if (containsPickaxe(existingCombined, stack)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        ItemStack existingEco = getEcoPickaxe(input);
        ItemStack crafted = existingEco.isEmpty() ? result.copy() : existingEco.copy();

        NbtComponent customData = crafted.getOrDefault(DataComponentTypes.CUSTOM_DATA,
                NbtComponent.of(new NbtCompound()));
        net.minecraft.nbt.NbtCompound nbt = customData.copyNbt();
        net.minecraft.nbt.NbtList combined = nbt.getList("CombinedPickaxes",
                net.minecraft.nbt.NbtElement.STRING_TYPE);

        for (int i = 0; i < input.getStackCount(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (stack.isEmpty() || isEcoPickaxe(stack)) {
                continue;
            }
            net.minecraft.util.Identifier id = net.minecraft.registry.Registries.ITEM.getId(stack.getItem());
            if (!containsPickaxe(combined, stack)) {
                combined.add(net.minecraft.nbt.NbtString.of(id.toString()));
            }
        }

        nbt.put("CombinedPickaxes", combined);
        crafted.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
        return crafted;
    }

    @Override
    public RecipeSerializer getSerializer() {
        return ModRecipes.UNIQUE_SHAPELESS;
    }

    @Override
    public RecipeType getType() {
        return RecipeType.CRAFTING;
    }

    public DefaultedList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ItemStack getResult() {
        return result;
    }

    @Override
    public CraftingRecipeCategory getCategory() {
        return category;
    }

    public boolean fits(int width, int height) {
        return width * height >= ingredients.size();
    }

    private ItemStack getEcoPickaxe(CraftingRecipeInput input) {
        for (int i = 0; i < input.getStackCount(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (!stack.isEmpty() && isEcoPickaxe(stack)) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    private boolean isEcoPickaxe(ItemStack stack) {
        return net.minecraft.registry.Registries.ITEM.getId(stack.getItem())
                .equals(Identifier.of(dev.moma.ecopicks.EcoPicks.MOD_ID, "eco_pickaxe"));
    }

    private NbtList getCombinedList(ItemStack stack) {
        NbtComponent customData = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (customData == null) {
            return null;
        }
        NbtList combinedList = customData.copyNbt().getList("CombinedPickaxes",
                net.minecraft.nbt.NbtElement.STRING_TYPE);
        return combinedList.isEmpty() ? null : combinedList;
    }

    private boolean containsPickaxe(NbtList combinedList, ItemStack stack) {
        net.minecraft.util.Identifier id = net.minecraft.registry.Registries.ITEM.getId(stack.getItem());
        return combinedList.stream().anyMatch(element -> element.asString().equals(id.toString()));
    }

    public static class Serializer implements RecipeSerializer<EcoPickaxeRecipes> {
        public static final MapCodec<EcoPickaxeRecipes> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                CraftingRecipeCategory.CODEC.fieldOf("category").forGetter(r -> r.category),
                ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(r -> r.result),
                Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(r -> r.ingredients))
                .apply(inst, (cat, res, ing) -> new EcoPickaxeRecipes(cat, res,
                        DefaultedList.copyOf(null, ing.toArray(new Ingredient[0])))));

        public static final PacketCodec<RegistryByteBuf, EcoPickaxeRecipes> PACKET_CODEC = PacketCodec.ofStatic(
                Serializer::write, Serializer::read);

        private static void write(RegistryByteBuf buf, EcoPickaxeRecipes recipe) {
            buf.writeEnumConstant(recipe.category);
            ItemStack.PACKET_CODEC.encode(buf, recipe.result);
            buf.writeVarInt(recipe.ingredients.size());
            for (Ingredient ing : recipe.ingredients) {
                Ingredient.PACKET_CODEC.encode(buf, ing);
            }
        }

        private static EcoPickaxeRecipes read(RegistryByteBuf buf) {
            CraftingRecipeCategory cat = buf.readEnumConstant(CraftingRecipeCategory.class);
            ItemStack res = ItemStack.PACKET_CODEC.decode(buf);
            int size = buf.readVarInt();
            DefaultedList<Ingredient> ings = DefaultedList.ofSize(size, null);
            for (int i = 0; i < size; i++) {
                ings.set(i, Ingredient.PACKET_CODEC.decode(buf));
            }
            return new EcoPickaxeRecipes(cat, res, ings);
        }

        @Override
        public MapCodec<EcoPickaxeRecipes> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, EcoPickaxeRecipes> packetCodec() {
            return PACKET_CODEC;
        }
    }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        return IngredientPlacement.forShapeless(ingredients);
    }
}