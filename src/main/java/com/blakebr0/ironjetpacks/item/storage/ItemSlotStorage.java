package com.blakebr0.ironjetpacks.item.storage;

import net.fabricmc.fabric.api.transfer.v1.item.base.SingleStackStorage;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ItemSlotStorage extends SingleStackStorage {
    private LivingEntity entity;
    private EquipmentSlot slot;
    
    public ItemSlotStorage(LivingEntity entity, EquipmentSlot slot) {
        this.entity = entity;
        this.slot = slot;
    }
    
    @Override
    public ItemStack getStack() {
        return entity.getItemBySlot(slot);
    }
    
    @Override
    protected void setStack(ItemStack stack) {
        entity.setItemSlot(slot, stack);
    }
}
