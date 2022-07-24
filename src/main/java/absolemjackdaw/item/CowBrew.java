package absolemjackdaw.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import org.jetbrains.annotations.NotNull;

public class CowBrew implements IBrewingRecipe {
    @Override
    public boolean isInput(ItemStack input) {
        return input.getItem().equals(Items.POTION) && PotionUtils.getPotion(input).equals(Potions.AWKWARD);
    }

    @Override
    public boolean isIngredient(ItemStack ingredient) {
        return ingredient.getItem().equals(Items.MILK_BUCKET);
    }

    @Override
    public @NotNull ItemStack getOutput(ItemStack input, @NotNull ItemStack ingredient) {

        return !input.isEmpty()
                && !ingredient.isEmpty()
                && isInput(input)
                && isIngredient(ingredient)
                ? PotionUtils.setPotion(new ItemStack(Items.POTION, 1), RegisterItem.potion.get())
                : ItemStack.EMPTY;
    }
}
