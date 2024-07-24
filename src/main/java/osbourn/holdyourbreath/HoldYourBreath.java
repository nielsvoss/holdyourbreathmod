package osbourn.holdyourbreath;

import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HoldYourBreath implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MODID = "holdyourbreath";
    public static final Logger LOGGER = LoggerFactory.getLogger("holdyourbreath");

	public static BreathingManager breathingManager = new BreathingManager();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		PayloadTypeRegistry.playC2S().register(HoldBreathPayload.ID, HoldBreathPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(DrowningPayload.ID, DrowningPayload.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(HoldBreathPayload.ID, this::receivePacket);

		MidnightConfig.init(MODID, HoldYourBreathConfig.class);
	}

	private void receivePacket(HoldBreathPayload payload, ServerPlayNetworking.Context context) {
		context.server().execute(() -> {
			if (payload.isHoldingBreath()) {
				breathingManager.setBreathingState(context.player(), BreathingManager.BreathingState.HOLDING_BREATH);
			} else {
				breathingManager.setBreathingState(context.player(), BreathingManager.BreathingState.NOT_HOLDING_BREATH);
			}
		});
	}

	public static void sendDrowningPacket(ServerPlayerEntity player, boolean isPlayerDrowning) {
		ServerPlayNetworking.send(player, new DrowningPayload(isPlayerDrowning));
	}
}