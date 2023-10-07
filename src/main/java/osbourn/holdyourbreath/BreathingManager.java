package osbourn.holdyourbreath;

import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BreathingManager {
    private final Map<UUID, BreathingState> breathingStates;

    public enum BreathingState {
        HOLDING_BREATH,
        NOT_HOLDING_BREATH,
        UNKNOWN
    }

    public BreathingManager() {
        this.breathingStates = new HashMap<>();
    }

    public BreathingState getBreathingState(PlayerEntity player) {
         return this.breathingStates.getOrDefault(player.getUuid(), BreathingState.UNKNOWN);
    }

    public void setBreathingState(PlayerEntity player, BreathingState state) {
        this.breathingStates.put(player.getUuid(), state);
    }
}
