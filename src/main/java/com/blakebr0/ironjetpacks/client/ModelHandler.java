package com.blakebr0.ironjetpacks.client;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.models.JModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Environment(EnvType.CLIENT)
public class ModelHandler {
    public static final RuntimeResourcePack RESOURCE_PACK = RuntimeResourcePack.create("iron-jetpacks:iron_jetpacks");
    private static final Logger LOGGER = LogManager.getLogger(IronJetpacks.NAME);
    
    public static void onClientSetup() {
        ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
            out.accept(new ModelResourceLocation(new ResourceLocation(IronJetpacks.MOD_ID, "cell"), "inventory"));
            out.accept(new ModelResourceLocation(new ResourceLocation(IronJetpacks.MOD_ID, "capacitor"), "inventory"));
            out.accept(new ModelResourceLocation(new ResourceLocation(IronJetpacks.MOD_ID, "thruster"), "inventory"));
            out.accept(new ModelResourceLocation(new ResourceLocation(IronJetpacks.MOD_ID, "jetpack"), "inventory"));
        });
        ResourceLocation cell = new ResourceLocation(IronJetpacks.MOD_ID, "item/cell");
        ResourceLocation capacitor = new ResourceLocation(IronJetpacks.MOD_ID, "item/capacitor");
        ResourceLocation thruster = new ResourceLocation(IronJetpacks.MOD_ID, "item/thruster");
        ResourceLocation jetpack = new ResourceLocation(IronJetpacks.MOD_ID, "item/jetpack");
        JetpackRegistry.getInstance().getAllJetpacks().forEach(pack -> {
            ResourceLocation cellLocation = BuiltInRegistries.ITEM.getKey(pack.cell);
            if (cellLocation != null) {
                ModelResourceLocation location = new ModelResourceLocation(cellLocation, "inventory");
                provideModel(location, "cell");
            }
            
            ResourceLocation capacitorLocation = BuiltInRegistries.ITEM.getKey(pack.capacitor);
            if (capacitorLocation != null) {
                ModelResourceLocation location = new ModelResourceLocation(capacitorLocation, "inventory");
                provideModel(location, "capacitor");
            }
            
            ResourceLocation thrusterLocation = BuiltInRegistries.ITEM.getKey(pack.thruster);
            if (thrusterLocation != null) {
                ModelResourceLocation location = new ModelResourceLocation(thrusterLocation, "inventory");
                provideModel(location, "thruster");
            }
            
            ResourceLocation jetpackLocation = BuiltInRegistries.ITEM.getKey(pack.item.get());
            if (jetpackLocation != null) {
                ModelResourceLocation location = new ModelResourceLocation(jetpackLocation, "inventory");
                provideModel(location, "jetpack");
            }
        });
        RRPCallback.BEFORE_VANILLA.register(a -> a.add(RESOURCE_PACK));
    }
    
    private static void provideModel(ModelResourceLocation modelIdentifier, String redirectedId) {
        if (redirectedId.equals("jetpack")) {
            RESOURCE_PACK.addModel(JModel.model().parent("item/generated")
                            .textures(JModel.textures()
                                    .layer0("iron-jetpacks:item/jetpack_strap")
                                    .layer1("iron-jetpacks:item/" + redirectedId)
                            ),
                    new ResourceLocation("iron-jetpacks:item/" + modelIdentifier.getPath()));
        } else {
            RESOURCE_PACK.addModel(JModel.model().parent("item/generated")
                            .textures(JModel.textures().layer0("iron-jetpacks:item/" + redirectedId)),
                    new ResourceLocation("iron-jetpacks:item/" + modelIdentifier.getPath()));
        }
    }
}
