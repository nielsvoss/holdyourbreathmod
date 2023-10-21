package osbourn.holdyourbreath;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import org.lwjgl.glfw.GLFW;

public class HoldYourBreathClient implements ClientModInitializer {
	private static KeyBinding breatheKeyBinding;
	private static boolean isHoldingBreath = false;

	/**
	 * False until the server says we are drowning
	 */
	public static boolean isDrowning = false;

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		breatheKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.holdyourbreath.breathe",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_R,
				"category.holdyourbreath.keybindings"
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (breatheKeyBinding.isPressed() && !isHoldingBreath) {
				startHoldingBreath(client);
			}
			if (!breatheKeyBinding.isPressed() && isHoldingBreath) {
				stopHoldingBreath(client);
			}
			isHoldingBreath = breatheKeyBinding.isPressed();
		});

		ClientPlayNetworking.registerGlobalReceiver(HoldYourBreathNetworkingConstants.DROWNING_PACKET_ID,
				(client, handler, buf, responseSender) -> {
					isDrowning = buf.readBoolean();
				});
	}

	private void startHoldingBreath(MinecraftClient client) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBoolean(true);
		ClientPlayNetworking.send(HoldYourBreathNetworkingConstants.HOLD_BREATH_PACKET_ID, buf);
	}

	private void stopHoldingBreath(MinecraftClient client) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBoolean(false);
		ClientPlayNetworking.send(HoldYourBreathNetworkingConstants.HOLD_BREATH_PACKET_ID, buf);
	}
}
