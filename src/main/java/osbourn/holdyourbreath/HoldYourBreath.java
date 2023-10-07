package osbourn.holdyourbreath;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HoldYourBreath implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MODID = "holdyourbreath";
    public static final Logger LOGGER = LoggerFactory.getLogger("holdyourbreath");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");

		ServerPlayNetworking.registerGlobalReceiver(HoldYourBreathNetworkingConstants.HOLD_BREATH_PACKET_ID,
			this::receivePacket);
	}

	private void receivePacket(MinecraftServer server,
							   ServerPlayerEntity player,
							   ServerPlayNetworkHandler handler,
							   PacketByteBuf buf,
							   PacketSender responseSender) {
		boolean isHoldingBreath = buf.readBoolean();

		server.execute(() -> {
			player.sendMessage(Text.literal(isHoldingBreath ? "Started holding breath" : "Stopped holding breath"));
		});
	}
}