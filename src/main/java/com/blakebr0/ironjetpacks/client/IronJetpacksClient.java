package com.blakebr0.ironjetpacks.client;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.client.model.JetpackModel;
import com.blakebr0.ironjetpacks.config.ModConfigs;
import com.blakebr0.ironjetpacks.handler.ColorHandler;
import com.blakebr0.ironjetpacks.handler.HudHandler;
import com.blakebr0.ironjetpacks.handler.JetpackClientHandler;
import com.blakebr0.ironjetpacks.handler.KeyBindingsHandler;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class IronJetpacksClient {
    public static void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(KeyBindingsHandler::onClientTick);
        HudRenderCallback.EVENT.register(HudHandler::onRenderGameOverlay);
        ClientTickEvents.END_CLIENT_TICK.register(JetpackClientHandler::onClientTick);
        
        KeyBindingsHandler.onClientSetup();
        ColorHandler.onClientSetup();
        ModelHandler.onClientSetup();
        
        AutoConfig.register(ModConfigs.Client.class, JanksonConfigSerializer::new);
        for (Jetpack jetpack : JetpackRegistry.getInstance().getAllJetpacks()) {
            ArmorRenderer.register(new ArmorRenderer() {
                private JetpackModel model;
                
                @Override
                public void render(PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, HumanoidModel<LivingEntity> contextModel) {
                    int colorTint = jetpack.item.get().getColorTint(1);
                    float r = (float) (colorTint >> 16 & 255) / 255.0F;
                    float g = (float) (colorTint >> 8 & 255) / 255.0F;
                    float b = (float) (colorTint & 255) / 255.0F;
                    JetpackModel model = getModel();
                    contextModel.copyPropertiesTo(model);
                    model.setupAnim(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
                    VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(vertexConsumers, RenderType.armorCutoutNoCull(new ResourceLocation(IronJetpacks.MOD_ID + ":textures/armor/jetpack.png")), false, stack.hasFoil());
                    model.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, r, g, b, 1.0F);
                    vertexConsumer = ItemRenderer.getArmorFoilBuffer(vertexConsumers, RenderType.armorCutoutNoCull(new ResourceLocation(IronJetpacks.MOD_ID + ":textures/armor/jetpack_overlay.png")), false, stack.hasFoil());
                    model.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                }
                
                private JetpackModel getModel() {
                    if (model == null) {
                        model = new JetpackModel(jetpack.item.get());
                    }
                    
                    return model;
                }
            }, jetpack.item.get());
        }
    }
}
