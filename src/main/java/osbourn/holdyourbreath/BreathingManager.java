package osbourn.holdyourbreath;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.*;

public class BreathingManager {
    private final Map<UUID, BreathingState> breathingStates;

    /**
     * Tracks the players that have released their breath since going underwater.
     */
    private final Set<UUID> drowningPlayers;

    public enum BreathingState {
        HOLDING_BREATH,
        NOT_HOLDING_BREATH
    }

    public BreathingManager() {
        this.breathingStates = new HashMap<>();
        this.drowningPlayers = new HashSet<>();
    }

    /**
     * In most cases, you should use isHoldingBreath instead
     */
    public BreathingState getBreathingState(PlayerEntity player) {
         return this.breathingStates.getOrDefault(player.getUuid(), BreathingState.NOT_HOLDING_BREATH);
    }

    public void setBreathingState(PlayerEntity player, BreathingState state) {
        this.breathingStates.put(player.getUuid(), state);
    }

    /**
     * Like getBreathingState, but accounts for more situations (like players that recently joined).
     */
    public boolean isHoldingBreath(PlayerEntity player) {
        boolean activelyHoldingBreath = this.getBreathingState(player) == BreathingState.HOLDING_BREATH;
        boolean joinedOrRespawnedRecently = player.age < HoldYourBreathConfig.safeTicksAfterLoginOrRespawn;
        return activelyHoldingBreath || joinedOrRespawnedRecently;
    }

    public void setDrowning(PlayerEntity player, boolean isDrowning) {
        boolean didUpdate;
        if (isDrowning) {
            didUpdate = this.drowningPlayers.add(player.getUuid());
        } else {
            didUpdate = this.drowningPlayers.remove(player.getUuid());
        }
        if (didUpdate) {
            HoldYourBreath.sendDrowningPacket((ServerPlayerEntity) player, isDrowning);
        }
    }

    public boolean isDrowning(PlayerEntity player) {
        return this.drowningPlayers.contains(player.getUuid());
    }
}
