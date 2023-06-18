package com.blakebr0.ironjetpacks.crafting;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.config.ModConfigs;
import com.blakebr0.ironjetpacks.crafting.recipe.JetpackUpgradeRecipe;
import com.blakebr0.ironjetpacks.item.ModItems;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.function.BiConsumer;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

public class JetpackDynamicRecipeManager {
    public static void appendRecipes(BiConsumer<ResourceLocation, Recipe<?>> appender) {
        JetpackRegistry.getInstance().getAllJetpacks().forEach(jetpack -> {
            ShapedRecipe cell = makeCellRecipe(jetpack);
            ShapedRecipe thruster = makeThrusterRecipe(jetpack);
            ShapedRecipe capacitor = makeCapacitorRecipe(jetpack);
            ShapedRecipe jetpackSelf = makeJetpackRecipe(jetpack);
            JetpackUpgradeRecipe jetpackUpgrade = makeJetpackUpgradeRecipe(jetpack);
            if (cell != null)
                appender.accept(cell.getId(), cell);
            if (thruster != null)
                appender.accept(thruster.getId(), thruster);
            if (capacitor != null)
                appender.accept(capacitor.getId(), capacitor);
            if (jetpackSelf != null)
                appender.accept(jetpackSelf.getId(), jetpackSelf);
            if (jetpackUpgrade != null)
                appender.accept(jetpackUpgrade.getId(), jetpackUpgrade);
        });
    }
    
    private static ShapedRecipe makeCellRecipe(Jetpack jetpack) {
        if (!ModConfigs.get().recipe.enableCellRecipes)
            return null;
        
        JetpackRegistry jetpacks = JetpackRegistry.getInstance();
        
        Ingredient material = jetpack.getCraftingMaterial();
        Item coilItem = jetpacks.getCoilForTier(jetpack.tier);
        if (material == Ingredient.EMPTY || coilItem == null)
            return null;
        
        Ingredient coil = Ingredient.of(coilItem);
        Ingredient redstone = Ingredient.of(Items.REDSTONE);
        NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY,
                Ingredient.EMPTY, redstone, Ingredient.EMPTY,
                material, coil, material,
                Ingredient.EMPTY, redstone, Ingredient.EMPTY
        );
        
        ResourceLocation name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_cell");
        ItemStack output = new ItemStack(jetpack.cell);
        
        return new ShapedRecipe(name, "iron-jetpacks:cells", CraftingBookCategory.MISC, 3, 3, inputs, output);
    }
    
    private static ShapedRecipe makeThrusterRecipe(Jetpack jetpack) {
        if (!ModConfigs.get().recipe.enableThrusterRecipes)
            return null;
        
        JetpackRegistry jetpacks = JetpackRegistry.getInstance();
        
        Ingredient material = jetpack.getCraftingMaterial();
        Item coilItem = jetpacks.getCoilForTier(jetpack.tier);
        if (material == Ingredient.EMPTY || coilItem == null)
            return null;
        
        Ingredient coil = Ingredient.of(coilItem);
        Ingredient cell = Ingredient.of(jetpack.cell);
        Ingredient furnace = Ingredient.of(Blocks.FURNACE);
        NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY,
                material, coil, material,
                coil, cell, coil,
                material, furnace, material
        );
        
        ResourceLocation name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_thruster");
        ItemStack output = new ItemStack(jetpack.thruster);
        
        return new ShapedRecipe(name, "iron-jetpacks:thrusters", CraftingBookCategory.MISC, 3, 3, inputs, output);
    }
    
    private static ShapedRecipe makeCapacitorRecipe(Jetpack jetpack) {
        if (!ModConfigs.get().recipe.enableCapacitorRecipes)
            return null;
        
        Ingredient material = jetpack.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;
        
        Ingredient cell = Ingredient.of(jetpack.cell);
        NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY,
                material, cell, material,
                material, cell, material,
                material, cell, material
        );
        
        ResourceLocation name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_capacitor");
        ItemStack output = new ItemStack(jetpack.capacitor);
        
        return new ShapedRecipe(name, "iron-jetpacks:capacitors", CraftingBookCategory.MISC, 3, 3, inputs, output);
    }
    
    private static ShapedRecipe makeJetpackRecipe(Jetpack jetpack) {
        if (!ModConfigs.get().recipe.enableJetpackRecipes)
            return null;
        
        JetpackRegistry jetpacks = JetpackRegistry.getInstance();
        if (jetpack.tier != jetpacks.getLowestTier())
            return null;
        
        Ingredient material = jetpack.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;
        
        Ingredient capacitor = Ingredient.of(jetpack.capacitor);
        Ingredient thruster = Ingredient.of(jetpack.thruster);
        Ingredient strap = Ingredient.of(ModItems.STRAP.get());
        NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY,
                material, capacitor, material,
                material, strap, material,
                thruster, Ingredient.EMPTY, thruster
        );
        
        ResourceLocation name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_jetpack");
        ItemStack output = new ItemStack(jetpack.item.get());
        
        return new ShapedRecipe(name, "iron-jetpacks:jetpacks", CraftingBookCategory.MISC, 3, 3, inputs, output);
    }
    
    private static JetpackUpgradeRecipe makeJetpackUpgradeRecipe(Jetpack jetpack) {
        if (!ModConfigs.get().recipe.enableJetpackRecipes)
            return null;
        
        JetpackRegistry jetpacks = JetpackRegistry.getInstance();
        if (jetpack.tier == jetpacks.getLowestTier())
            return null;
        
        Ingredient material = jetpack.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;
        
        Ingredient capacitor = Ingredient.of(jetpack.capacitor);
        Ingredient thruster = Ingredient.of(jetpack.thruster);
        Ingredient jetpackTier = Ingredient.of(ModRecipeSerializers.ALL_JETPACKS.stream()
                .filter(item -> item.getJetpack().tier == jetpack.tier - 1)
                .toArray(ItemLike[]::new));
        NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY,
                material, capacitor, material,
                material, jetpackTier, material,
                thruster, Ingredient.EMPTY, thruster
        );
        
        ResourceLocation name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_jetpack");
        ItemStack output = new ItemStack(jetpack.item.get());
        
        return new JetpackUpgradeRecipe(name, "iron-jetpacks:jetpacks", 3, 3, inputs, output);
    }
}
