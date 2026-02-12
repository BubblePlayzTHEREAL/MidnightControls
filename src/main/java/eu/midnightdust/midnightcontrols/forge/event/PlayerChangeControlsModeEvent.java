package eu.midnightdust.midnightcontrols.forge.event;

//? forge {
/*import eu.midnightdust.midnightcontrols.ControlsMode;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

public class PlayerChangeControlsModeEvent extends Event implements IModBusEvent {
    private final Player player;
    private final ControlsMode controlsMode;

    public PlayerChangeControlsModeEvent(Player player, ControlsMode controlsMode) {
        this.player = player;
        this.controlsMode = controlsMode;
    }

    public Player getPlayer() {
        return player;
    }

    public ControlsMode getControlsMode() {
        return controlsMode;
    }
}
*///?}
