package osbourn.holdyourbreath;

import net.minecraft.entity.player.PlayerEntity;

import java.util.*;

public class BreathingManager {
    private final Map<UUID, BreathingState> breathingStates;
    private final Set<UUID> drowningPlayers;

    public enum BreathingState {
        HOLDING_BREATH,
        NOT_HOLDING_BREATH,
        UNKNOWN
    }

    public BreathingManager() {
        this.breathingStates = new HashMap<>();
        this.drowningPlayers = new HashSet<>();
    }

    public BreathingState getBreathingState(PlayerEntity player) {
         return this.breathingStates.getOrDefault(player.getUuid(), BreathingState.UNKNOWN);
    }

    public void setBreathingState(PlayerEntity player, BreathingState state) {
        this.breathingStates.put(player.getUuid(), state);
    }

    public void setDrowning(PlayerEntity player, boolean isDrowning) {
        if (isDrowning) {
            this.drowningPlayers.add(player.getUuid());
        } else {
            this.drowningPlayers.remove(player.getUuid());
        }
    }

    public boolean isDrowning(PlayerEntity player) {
        return this.drowningPlayers.contains(player.getUuid());
    }
}
