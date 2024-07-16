package osbourn.holdyourbreath;

import eu.midnightdust.lib.config.MidnightConfig;

public class HoldYourBreathConfig extends MidnightConfig {
    @Comment(category = "server")
    @SuppressWarnings("unused")
    public static Comment serverSectionComment;
    @Comment(category = "server")
    @SuppressWarnings("unused")
    public static Comment breathHoldingEnabledComment;
    @Entry(category = "server")
    public static boolean breathHoldingEnabled = true;
    @Entry(category = "server")
    public static int airLossMultiplier = 10;
    @Entry(category = "server")
    public static float drowningDamageMultiplier = 2.5F;
    @Entry(category = "server")
    public static int safeTicksAfterLoginOrRespawn = 100;
    @Entry(category = "server", min = -Double.MAX_VALUE)
    public static double drowningDamageDownwardForce = 0.0;
    @Entry(category = "server")
    public static boolean allowRecoveringBreathUnderwater = false;
    @Entry(category = "server")
    public static boolean preventPlacingDoorsUnderwater = false;
    @Comment(category = "client")
    @SuppressWarnings("unused")
    public static Comment clientSectionComment;
    @Comment(category = "client")
    @SuppressWarnings("unused")
    public static Comment toggleBreathingComment;
    @Entry(category = "client")
    public static boolean toggleBreathing;
}
