package osbourn.holdyourbreath;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class HoldYourBreathClient implements ClientModInitializer {
	private static KeyBinding breatheKeyBinding;
	private static boolean isHoldingBreath = false;

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
	}

	private void startHoldingBreath(MinecraftClient client) {
		client.player.sendMessage(Text.literal("Started holding breath"));
	}

	private void stopHoldingBreath(MinecraftClient client) {
		client.player.sendMessage(Text.literal("Stopped holding breath"));
	}
}