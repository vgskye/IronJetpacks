package com.blakebr0.ironjetpacks.compat.emi;

import com.blakebr0.ironjetpacks.crafting.JetpackDynamicRecipeManager;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.recipe.EmiShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class EmiCompat implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        JetpackDynamicRecipeManager.appendRecipes((id, recipe) -> {
            registry.addRecipe(new EmiShapedRecipe((ShapedRecipe) recipe));
        });
    }
}
