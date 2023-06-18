package com.blakebr0.ironjetpacks.handler;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.client.util.HudHelper;
import com.blakebr0.ironjetpacks.client.util.HudHelper.HudPos;
import com.blakebr0.ironjetpacks.config.ModConfigs;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class HudHandler {
    private static final ResourceLocation HUD_TEXTURE = new ResourceLocation(IronJetpacks.MOD_ID, "textures/gui/hud.png");
    
    public static void onRenderGameOverlay(GuiGraphics graphics, float delta) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            if (ModConfigs.getClient().hud.enableHud && (ModConfigs.getClient().hud.showHudOverChat || !ModConfigs.getClient().hud.showHudOverChat && !(mc.screen instanceof ChatScreen)) && !mc.options.hideGui && !mc.options.renderDebug) {
                ItemStack chest = mc.player.getItemBySlot(EquipmentSlot.CHEST);
                Item item = chest.getItem();
                if (!chest.isEmpty() && item instanceof JetpackItem) {
                    JetpackItem jetpack = (JetpackItem) item;
                    HudPos pos = HudHelper.getHudPos();
                    if (pos != null) {
                        int xPos = (int) (pos.x / 0.33) - 18;
                        int yPos = (int) (pos.y / 0.33) - 78;
                        
                        RenderSystem.setShaderTexture(0, HUD_TEXTURE);
                        
                        graphics.pose().pushPose();
                        graphics.pose().scale(0.33f, 0.33f, 1.0f);
                        graphics.blit(HUD_TEXTURE, xPos, yPos, 0, 0, 0, 28, 156, 256, 256);
                        int i2 = HudHelper.getEnergyBarScaled(jetpack, chest);
                        graphics.blit(HUD_TEXTURE, xPos, 166 - i2 + yPos - 10, 0, 28, 156 - i2, 28, i2, 256, 256);
                        graphics.pose().popPose();
                        
                        String fuel = ChatFormatting.GRAY + HudHelper.getFuel(jetpack, chest);
                        String engine = ChatFormatting.GRAY + "E: " + HudHelper.getOn(jetpack.isEngineOn(chest));
                        String hover = ChatFormatting.GRAY + "H: " + HudHelper.getOn(jetpack.isHovering(chest));
                        
                        if (pos.side == 1) {
                            graphics.drawString(mc.font, fuel, pos.x - 8 - mc.font.width(fuel), pos.y - 21, 16383998);
                            graphics.drawString(mc.font, engine, pos.x - 8 - mc.font.width(engine), pos.y + 4, 16383998);
                            graphics.drawString(mc.font, hover, pos.x - 8 - mc.font.width(hover), pos.y + 14, 16383998);
                        } else {
                            graphics.drawString(mc.font, fuel, pos.x + 6, pos.y - 21, 16383998);
                            graphics.drawString(mc.font, engine, pos.x + 6, pos.y + 4, 16383998);
                            graphics.drawString(mc.font, hover, pos.x + 6, pos.y + 14, 16383998);
                        }
                    }
                }
            }
        }
    }
}
