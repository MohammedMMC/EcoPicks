package dev.moma.ecopicks.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueShapelessRecipe implements CraftingRecipe {
    final CraftingRecipeCategory category;
    final ItemStack result;
    final DefaultedList<Ingredient> ingredients;

    public UniqueShapelessRecipe(CraftingRecipeCategory category, ItemStack result,
            DefaultedList<Ingredient> ingredients) {
        this.category = category;
        this.result = result;
        this.ingredients = ingredients;
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        if (input.getSize() < ingredients.size())
            return false;

        Set<ItemStack> usedStacks = new HashSet<>();
        List<Ingredient> remaining = new ArrayList<>(ingredients);

        for (int i = 0; i < input.getSize(); i++) {
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

        return remaining.isEmpty();
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.UNIQUE_SHAPELESS;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup lookup) {
        return result;
    }

    @Override
    public CraftingRecipeCategory getCategory() {
        return category;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= ingredients.size();
    }

    public static class Serializer implements RecipeSerializer<UniqueShapelessRecipe> {
        public static final MapCodec<UniqueShapelessRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                CraftingRecipeCategory.CODEC.fieldOf("category").forGetter(r -> r.category),
                ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(r -> r.result),
                Ingredient.DISALLOW_EMPTY_CODEC.listOf().fieldOf("ingredients").forGetter(r -> r.ingredients))
                .apply(inst, (cat, res, ing) -> new UniqueShapelessRecipe(cat, res,
                        DefaultedList.copyOf(Ingredient.EMPTY, ing.toArray(new Ingredient[0])))));

        public static final PacketCodec<RegistryByteBuf, UniqueShapelessRecipe> PACKET_CODEC = PacketCodec.ofStatic(
                Serializer::write, Serializer::read);

        private static void write(RegistryByteBuf buf, UniqueShapelessRecipe recipe) {
            buf.writeEnumConstant(recipe.category);
            ItemStack.PACKET_CODEC.encode(buf, recipe.result);
            buf.writeVarInt(recipe.ingredients.size());
            for (Ingredient ing : recipe.ingredients) {
                Ingredient.PACKET_CODEC.encode(buf, ing);
            }
        }

        private static UniqueShapelessRecipe read(RegistryByteBuf buf) {
            CraftingRecipeCategory cat = buf.readEnumConstant(CraftingRecipeCategory.class);
            ItemStack res = ItemStack.PACKET_CODEC.decode(buf);
            int size = buf.readVarInt();
            DefaultedList<Ingredient> ings = DefaultedList.ofSize(size, Ingredient.EMPTY);
            for (int i = 0; i < size; i++) {
                ings.set(i, Ingredient.PACKET_CODEC.decode(buf));
            }
            return new UniqueShapelessRecipe(cat, res, ings);
        }

        @Override
        public MapCodec<UniqueShapelessRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, UniqueShapelessRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }
}