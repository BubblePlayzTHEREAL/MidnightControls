package eu.midnightdust.midnightcontrols.forge;

//? forge {
/*import eu.midnightdust.midnightcontrols.ControlsMode;
import eu.midnightdust.midnightcontrols.MidnightControls;
import eu.midnightdust.midnightcontrols.MidnightControlsFeature;
import eu.midnightdust.midnightcontrols.forge.event.PlayerChangeControlsModeEvent;
import eu.midnightdust.midnightcontrols.packet.ControlsModePayload;
import eu.midnightdust.midnightcontrols.packet.FeaturePayload;
import eu.midnightdust.midnightcontrols.packet.HelloPayload;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

import static eu.midnightdust.midnightcontrols.MidnightControlsConstants.NAMESPACE;

@Mod(value = NAMESPACE)
public class MidnightControlsForge {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            HelloPayload.PACKET_ID,
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public MidnightControlsForge() {
        MidnightControls.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            int id = 0;
            
            // Register Hello Payload
            CHANNEL.registerMessage(id++, HelloPayload.class,
                    (payload, buffer) -> {
                        buffer.writeUtf(payload.version());
                        buffer.writeUtf(payload.controlsMode());
                    },
                    buffer -> new HelloPayload(buffer.readUtf(), buffer.readUtf()),
                    (payload, ctx) -> {
                        ctx.get().enqueueWork(() -> {
                            ControlsMode.byId(payload.controlsMode()).ifPresent(controlsMode -> 
                                MinecraftForge.EVENT_BUS.post(new PlayerChangeControlsModeEvent(ctx.get().getSender(), controlsMode))
                            );
                            ctx.get().getSender().connection.send(new ClientboundCustomPayloadPacket(
                                    new FeaturePayload(MidnightControlsFeature.HORIZONTAL_REACHAROUND)
                            ));
                        });
                        ctx.get().setPacketHandled(true);
                    },
                    Optional.of(NetworkDirection.PLAY_TO_SERVER)
            );

            // Register ControlsMode Payload
            CHANNEL.registerMessage(id++, ControlsModePayload.class,
                    (payload, buffer) -> buffer.writeUtf(payload.controlsMode()),
                    buffer -> new ControlsModePayload(buffer.readUtf()),
                    (payload, ctx) -> {
                        ctx.get().enqueueWork(() -> {
                            ControlsMode.byId(payload.controlsMode()).ifPresent(controlsMode -> 
                                MinecraftForge.EVENT_BUS.post(new PlayerChangeControlsModeEvent(ctx.get().getSender(), controlsMode))
                            );
                        });
                        ctx.get().setPacketHandled(true);
                    }
            );

            // Register Feature Payload
            CHANNEL.registerMessage(id++, FeaturePayload.class,
                    (payload, buffer) -> buffer.writeResourceLocation(payload.feature().id()),
                    buffer -> new FeaturePayload(MidnightControlsFeature.REGISTRY.get(buffer.readResourceLocation())),
                    (payload, ctx) -> {
                        ctx.get().setPacketHandled(true);
                    },
                    Optional.of(NetworkDirection.PLAY_TO_CLIENT)
            );
        });
    }
}
*///?}
