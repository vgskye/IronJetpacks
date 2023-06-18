package com.blakebr0.ironjetpacks;

import com.blakebr0.ironjetpacks.compat.ftl.FtlCompat;
import com.blakebr0.ironjetpacks.config.ModConfigs;
import com.blakebr0.ironjetpacks.crafting.ModRecipeSerializers;
import com.blakebr0.ironjetpacks.handler.InputHandler;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.blakebr0.ironjetpacks.item.ModItems;
import com.blakebr0.ironjetpacks.network.NetworkHandler;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import com.blakebr0.ironjetpacks.sound.ModSounds;
import dev.architectury.event.events.common.PlayerEvent;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class IronJetpacks implements ModInitializer {
    public static final String MOD_ID = "iron-jetpacks";
    public static final String NAME = "Iron Jetpacks";
    
    public static final CreativeModeTab ITEM_GROUP = FabricItemGroup.builder(new ResourceLocation(MOD_ID, MOD_ID))
            .icon(() -> {
                return new ItemStack(ModItems.STRAP.get());
            })
            .displayItems((featureFlagSet, output, bl) -> {
                for (Jetpack jetpack : JetpackRegistry.getInstance().getAllJetpacks()) {
                    output.accept(jetpack.cell);
                    output.accept(jetpack.thruster);
                    output.accept(jetpack.capacitor);
                    JetpackItem item = jetpack.item.get();
                    output.accept(new ItemStack(item));
                    
                    if (!jetpack.creative) {
                        ItemStack stack = new ItemStack(item);
                        stack.getOrCreateTag().putDouble("energy", jetpack.capacity);
                        output.accept(stack);
                    }
                }
            })
            .build();
    
    @Override
    public void onInitialize() {
        ModItems.register();
        ModSounds.register();
        ModRecipeSerializers.register();
        ModRecipeSerializers.onCommonSetup();
        
        NetworkHandler.onCommonSetup();
        
        AutoConfig.register(ModConfigs.Common.class, JanksonConfigSerializer::new);
        
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            try {
                Class.forName("com.blakebr0.ironjetpacks.client.IronJetpacksClient").getDeclaredMethod("onInitializeClient").invoke(null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        FtlCompat.init();
        
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> InputHandler.clear());
        PlayerEvent.CHANGE_DIMENSION.register((player, oldLevel, newLevel) -> InputHandler.onChangeDimension(player));
        PlayerEvent.PLAYER_QUIT.register(InputHandler::onLogout);
    }
}
