package com.blakebr0.ironjetpacks.util;

import com.blakebr0.ironjetpacks.handler.InputHandler;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.blakebr0.ironjetpacks.item.storage.ItemSlotStorage;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import team.reborn.energy.api.EnergyStorage;

public class JetpackUtils {
    public static boolean isFlying(Player player) {
        ItemStack stack = player.getItemBySlot(EquipmentSlot.CHEST);
        if (!stack.isEmpty()) {
            Item item = stack.getItem();
            if (item instanceof JetpackItem jetpack) {
                ItemSlotStorage storage = new ItemSlotStorage(player, EquipmentSlot.CHEST);
                if (jetpack.isEngineOn(stack) && (EnergyStorage.ITEM.find(stack, ContainerItemContext.ofSingleSlot(storage)).getAmount() > 0 || player.isCreative() || jetpack.getJetpack().creative)) {
                    if (jetpack.isHovering(stack)) {
                        return !player.onGround();
                    } else {
                        return InputHandler.isHoldingUp(player);
                    }
                }
            }
        }
        
        return false;
    }
    
    public static ArmorMaterial makeArmorMaterial(Jetpack jetpack) {
        return new ArmorMaterial() {
            @Override
            public int getDurabilityForType(ArmorItem.Type type) {
                return 0;
            }
            
            @Override
            public int getDefenseForType(ArmorItem.Type type) {
                return jetpack.armorPoints;
            }
            
            @Override
            public int getEnchantmentValue() {
                return jetpack.enchantablilty;
            }
            
            @Override
            public SoundEvent getEquipSound() {
                return SoundEvents.ARMOR_EQUIP_GENERIC;
            }
            
            @Override
            public Ingredient getRepairIngredient() {
                return null;
            }
            
            @Override
            public String getName() {
                return "iron-jetpacks:jetpack";
            }
            
            @Override
            public float getToughness() {
                return 0;
            }
            
            @Override
            public float getKnockbackResistance() {
                return 0;
            }
        };
    }
}
