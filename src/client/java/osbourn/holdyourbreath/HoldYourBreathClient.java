package osbourn.holdyourbreath;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
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
			if (HoldYourBreathConfig.toggleBreathing) {
				if (breatheKeyBinding.wasPressed()) {
					isHoldingBreath = !isHoldingBreath;
					if (isHoldingBreath) {
						startHoldingBreath(client);
					} else {
						stopHoldingBreath(client);
					}
				}
			} else {
				if (breatheKeyBinding.isPressed() && !isHoldingBreath) {
					startHoldingBreath(client);
				}
				if (!breatheKeyBinding.isPressed() && isHoldingBreath) {
					stopHoldingBreath(client);
				}
				isHoldingBreath = breatheKeyBinding.isPressed();
			}
		});

		ClientPlayNetworking.registerGlobalReceiver(DrowningPayload.ID,
				(payload, context) -> {
					isDrowning = payload.isDrowning();
				});
	}

	private void startHoldingBreath(MinecraftClient client) {
		ClientPlayNetworking.send(new HoldBreathPayload(true));
	}

	private void stopHoldingBreath(MinecraftClient client) {
		ClientPlayNetworking.send(new HoldBreathPayload(false));
	}
}
