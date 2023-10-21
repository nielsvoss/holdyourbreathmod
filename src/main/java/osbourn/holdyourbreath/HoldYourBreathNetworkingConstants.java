package osbourn.holdyourbreath;

import net.minecraft.util.Identifier;

public final class HoldYourBreathNetworkingConstants {
    private HoldYourBreathNetworkingConstants() {
    }

    public static final Identifier HOLD_BREATH_PACKET_ID =
            new Identifier(HoldYourBreath.MODID, "hold_breath");

    public static final Identifier DROWNING_PACKET_ID =
            new Identifier(HoldYourBreath.MODID, "drowning");
}
