package osbourn.holdyourbreath;

import eu.midnightdust.lib.config.MidnightConfig;

public class HoldYourBreathConfig extends MidnightConfig {
    @Entry(category = "numbers")
    public static int airLossMultiplier = 10;
    @Entry(category = "numbers")
    public static float drowningDamageMultiplier = 2.5F;
}
