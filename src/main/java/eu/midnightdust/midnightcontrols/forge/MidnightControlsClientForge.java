package eu.midnightdust.midnightcontrols.forge;

//? forge {
/*import eu.midnightdust.midnightcontrols.client.MidnightControlsClient;
import eu.midnightdust.midnightcontrols.client.MidnightControlsConfig;
import eu.midnightdust.midnightcontrols.client.MidnightControlsReloadListener;
import eu.midnightdust.midnightcontrols.client.util.platform.NetworkUtil;
import eu.midnightdust.midnightcontrols.packet.ControlsModePayload;
import eu.midnightdust.midnightcontrols.packet.HelloPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static eu.midnightdust.midnightcontrols.MidnightControls.id;
import static eu.midnightdust.midnightcontrols.MidnightControlsConstants.NAMESPACE;
import static eu.midnightdust.midnightcontrols.client.MidnightControlsClient.BINDING_LOOK_DOWN;
import static eu.midnightdust.midnightcontrols.client.MidnightControlsClient.BINDING_LOOK_LEFT;
import static eu.midnightdust.midnightcontrols.client.MidnightControlsClient.BINDING_LOOK_RIGHT;
import static eu.midnightdust.midnightcontrols.client.MidnightControlsClient.BINDING_LOOK_UP;
import static eu.midnightdust.midnightcontrols.client.MidnightControlsClient.BINDING_RING;
import static eu.midnightdust.midnightcontrols.client.MidnightControlsClient.client;
import static eu.midnightdust.midnightcontrols.client.MidnightControlsClient.clickInterceptor;


@Mod(value = NAMESPACE, dist = Dist.CLIENT)
public class MidnightControlsClientForge {
    public MidnightControlsClientForge() {
        MidnightControlsClient.initClient();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        MinecraftForge.EVENT_BUS.register(new ForgeClientEvents());
    }

    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // Any client-side initialization can go here
        });
    }

    public static class ForgeClientEvents {
        @SubscribeEvent
        public void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(BINDING_RING);
            event.register(BINDING_LOOK_UP);
            event.register(BINDING_LOOK_DOWN);
            event.register(BINDING_LOOK_LEFT);
            event.register(BINDING_LOOK_RIGHT);
        }

        @SubscribeEvent
        public void onAddPackFinders(AddPackFindersEvent event) {
            if (event.getPackType() == PackType.CLIENT_RESOURCES) {
                if (false) { // TODO: Switch to stonecutter build system to be able to test packs in dev environment
                    event.addRepositorySource((consumer) -> {
                        consumer.accept(Pack.readMetaAndCreate(
                                id("bedrock").toString(), 
                                Component.literal("midnightcontrols/bedrock"),
                                false,
                                (s) -> null,
                                PackType.CLIENT_RESOURCES,
                                Pack.Position.TOP,
                                PackSource.BUILT_IN
                        ));
                        consumer.accept(Pack.readMetaAndCreate(
                                id("legacy").toString(),
                                Component.literal("midnightcontrols/legacy"),
                                false,
                                (s) -> null,
                                PackType.CLIENT_RESOURCES,
                                Pack.Position.TOP,
                                PackSource.BUILT_IN
                        ));
                    });
                }
            }
        }

        @SubscribeEvent
        public void onResourceReload(RegisterClientReloadListenersEvent event) {
            event.registerReloadListener(MidnightControlsReloadListener.INSTANCE);
        }

        @SubscribeEvent
        public void onClientPlayerLogin(net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent event) {
            if (event.getEntity().level().isClientSide) {
                var version = ModList.get().getModFileById(NAMESPACE).versionString();
                var controlsMode = MidnightControlsConfig.controlsMode.getName();
                NetworkUtil.sendPayloadC2S(new HelloPayload(version, controlsMode));
                NetworkUtil.sendPayloadC2S(new ControlsModePayload(controlsMode));
                MidnightControlsClient.onJoinServer();
            }
        }

        @SubscribeEvent
        public void onClientPlayerLogout(net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent event) {
            if (event.getEntity().level().isClientSide) {
                MidnightControlsClient.onLeaveServer();
            }
        }

        @SubscribeEvent
        public void onScreenInit(ScreenEvent.Init.Post event) {
            Screen screen = event.getScreen();
            MidnightControlsClient.onScreenInit(client, screen);
        }

        @SubscribeEvent
        public void onScreenRender(ScreenEvent.Render.Post event) {
            MidnightControlsClient.onScreenRenderPost(event.getScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick());
        }

        @SubscribeEvent
        public void onClientTick(net.minecraftforge.event.TickEvent.ClientTickEvent event) {
            if (event.phase == net.minecraftforge.event.TickEvent.Phase.START) {
                clickInterceptor.tick();
                MidnightControlsClient.onTick(client);
            }
        }

        @SubscribeEvent
        public void onRenderTick(net.minecraftforge.event.TickEvent.RenderTickEvent event) {
            if (event.phase == net.minecraftforge.event.TickEvent.Phase.END) {
                if (MidnightControlsClient.BINDING_LOOK_UP.isDown()) MidnightControlsClient.handleLookUpKey(client);
                if (MidnightControlsClient.BINDING_LOOK_LEFT.isDown()) MidnightControlsClient.handleLookLeftKey(client);
                if (MidnightControlsClient.BINDING_LOOK_RIGHT.isDown()) MidnightControlsClient.handleLookRightKey(client);
                if (MidnightControlsClient.BINDING_LOOK_DOWN.isDown()) MidnightControlsClient.handleLookDownKey(client);
            }
        }
    }
}
*///?}
