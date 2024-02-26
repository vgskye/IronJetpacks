package com.blakebr0.ironjetpacks.crafting;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.config.ModConfigs;
import com.blakebr0.ironjetpacks.item.ModItems;
import com.blakebr0.ironjetpacks.mixins.JRecipeAccessor;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import net.devtech.arrp.json.recipe.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.function.BiConsumer;

public class JetpackDynamicRecipeManager {
    public static void appendRecipes(BiConsumer<ResourceLocation, JRecipe> appender) {
        JetpackRegistry.getInstance().getAllJetpacks().forEach(jetpack -> {
            Tuple<ResourceLocation, JRecipe> cell = makeCellRecipe(jetpack);
            Tuple<ResourceLocation, JRecipe> thruster = makeThrusterRecipe(jetpack);
            Tuple<ResourceLocation, JRecipe> capacitor = makeCapacitorRecipe(jetpack);
            Tuple<ResourceLocation, JRecipe> jetpackSelf = makeJetpackRecipe(jetpack);
            Tuple<ResourceLocation, JRecipe> jetpackUpgrade = makeJetpackUpgradeRecipe(jetpack);
            if (cell != null)
                appender.accept(cell.getA(), cell.getB());
            if (thruster != null)
                appender.accept(thruster.getA(), thruster.getB());
            if (capacitor != null)
                appender.accept(capacitor.getA(), capacitor.getB());
            if (jetpackSelf != null)
                appender.accept(jetpackSelf.getA(), jetpackSelf.getB());
            if (jetpackUpgrade != null)
                appender.accept(jetpackUpgrade.getA(), jetpackUpgrade.getB());
        });
    }

    private static Tuple<ResourceLocation, JRecipe> makeCellRecipe(Jetpack jetpack) {
        if (!ModConfigs.get().recipe.enableCellRecipes)
            return null;

        JetpackRegistry jetpacks = JetpackRegistry.getInstance();

        JIngredient material = jetpack.getCraftingMaterial();
        Item coilItem = jetpacks.getCoilForTier(jetpack.tier);
        if (material == null || coilItem == null)
            return null;

        JPattern pattern = JPattern.pattern(
                " R ",
                "MCM",
                " R "
        );
        JKeys keys = JKeys
                .keys()
                .key("R", JIngredient.ingredient().item(Items.REDSTONE))
                .key("M", material)
                .key("C", JIngredient.ingredient().item(coilItem));

        ResourceLocation name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_cell");

        return new Tuple<>(name, JRecipe.shaped(pattern, keys, JResult.item(jetpack.cell)));
    }

    private static Tuple<ResourceLocation, JRecipe> makeThrusterRecipe(Jetpack jetpack) {
        if (!ModConfigs.get().recipe.enableThrusterRecipes)
            return null;

        JetpackRegistry jetpacks = JetpackRegistry.getInstance();

        JIngredient material = jetpack.getCraftingMaterial();
        Item coilItem = jetpacks.getCoilForTier(jetpack.tier);
        if (material == null || coilItem == null)
            return null;

        JPattern pattern = JPattern.pattern(
                "MCM",
                "CcC",
                "MFM"
        );
        JKeys keys = JKeys
                .keys()
                .key("M", material)
                .key("C", JIngredient.ingredient().item(coilItem))
                .key("c", JIngredient.ingredient().item(jetpack.cell))
                .key("F", JIngredient.ingredient().item(Items.FURNACE));

        ResourceLocation name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_thruster");
        return new Tuple<>(name, JRecipe.shaped(pattern, keys, JResult.item(jetpack.thruster)));
    }

    private static Tuple<ResourceLocation, JRecipe> makeCapacitorRecipe(Jetpack jetpack) {
        if (!ModConfigs.get().recipe.enableCapacitorRecipes)
            return null;

        JIngredient material = jetpack.getCraftingMaterial();
        if (material == null)
            return null;

        JPattern pattern = JPattern.pattern(
                "McM",
                "McM",
                "McM"
        );
        JKeys keys = JKeys
                .keys()
                .key("M", material)
                .key("c", JIngredient.ingredient().item(jetpack.cell));

        ResourceLocation name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_capacitor");

        return new Tuple<>(name, JRecipe.shaped(pattern, keys, JResult.item(jetpack.capacitor)));
    }

    private static Tuple<ResourceLocation, JRecipe> makeJetpackRecipe(Jetpack jetpack) {
        if (!ModConfigs.get().recipe.enableJetpackRecipes)
            return null;

        JetpackRegistry jetpacks = JetpackRegistry.getInstance();
        if (jetpack.tier != jetpacks.getLowestTier())
            return null;

        JIngredient material = jetpack.getCraftingMaterial();
        if (material == null)
            return null;

        JPattern pattern = JPattern.pattern(
                "MfM",
                "MSM",
                "T T"
        );
        JKeys keys = JKeys
                .keys()
                .key("M", material)
                .key("f", JIngredient.ingredient().item(jetpack.capacitor))
                .key("S", JIngredient.ingredient().item(ModItems.STRAP.get()))
                .key("T", JIngredient.ingredient().item(jetpack.thruster));

        ResourceLocation name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_jetpack");

        return new Tuple<>(name, JRecipe.shaped(pattern, keys, JResult.item(jetpack.item.get())));
    }

    private static Tuple<ResourceLocation, JRecipe> makeJetpackUpgradeRecipe(Jetpack jetpack) {
        if (!ModConfigs.get().recipe.enableJetpackRecipes)
            return null;

        JetpackRegistry jetpacks = JetpackRegistry.getInstance();
        if (jetpack.tier == jetpacks.getLowestTier())
            return null;

        JIngredient material = jetpack.getCraftingMaterial();
        if (material == null)
            return null;

        JIngredient jetpackTier = ModRecipeSerializers.ALL_JETPACKS.stream()
                .filter(item -> item.getJetpack().tier == jetpack.tier - 1)
                .map(item -> JIngredient.ingredient().item(item))
                .reduce(JIngredient.ingredient(), JIngredient::add);
        JPattern pattern = JPattern.pattern(
                "MfM",
                "MJM",
                "T T"
        );
        JKeys keys = JKeys
                .keys()
                .key("M", material)
                .key("f", JIngredient.ingredient().item(jetpack.capacitor))
                .key("J", jetpackTier)
                .key("T", JIngredient.ingredient().item(jetpack.thruster));

        ResourceLocation name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_jetpack");

        JRecipe recipe = JRecipe.shaped(pattern, keys, JResult.item(jetpack.item.get()));
        ((JRecipeAccessor) recipe).setType("iron-jetpacks:crafting_jetpack_upgrade");

        return new Tuple<>(name, recipe);
    }
}