package osbourn.holdyourbreath;

import eu.midnightdust.lib.config.MidnightConfig;

public class HoldYourBreathConfig extends MidnightConfig {
    @Comment
    @SuppressWarnings("unused")
    public static Comment breathHoldingEnabledComment;
    @Entry
    public static boolean breathHoldingEnabled = true;
    @Entry
    public static int airLossMultiplier = 10;
    @Entry
    public static float drowningDamageMultiplier = 2.5F;
}
