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
    @Entry
    public static int safeTicksAfterLoginOrRespawn = 100;
    @Entry(min = -Double.MAX_VALUE)
    public static double drowningDamageDownwardForce = 0.0;
    @Entry
    public static boolean allowRecoveringBreathUnderwater = false;
    @Entry
    public static boolean preventPlacingDoorsUnderwater = false;
    @Comment
    @SuppressWarnings("unused")
    public static Comment toggleBreathingComment;
    @Entry
    public static boolean toggleBreathing;
}
