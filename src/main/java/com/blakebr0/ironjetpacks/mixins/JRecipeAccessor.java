package com.blakebr0.ironjetpacks.mixins;

import net.devtech.arrp.json.recipe.JRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = JRecipe.class, remap = false)
public interface JRecipeAccessor {
    @Mutable
    @Accessor(value = "type", remap = false)
    void setType(String type);
}
