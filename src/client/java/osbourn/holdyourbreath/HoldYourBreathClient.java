package osbourn.holdyourbreath;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class HoldYourBreathClient implements ClientModInitializer {
	private static KeyBinding breatheKeyBinding;

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		breatheKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.holdyourbreath.breathe",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_R,
				"category.holdyourbreath.keybindings"
		));
	}
}