package osbourn.holdyourbreath;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record HoldBreathPayload(boolean isHoldingBreath) implements CustomPayload {
    public static final CustomPayload.Id<HoldBreathPayload> ID = new CustomPayload.Id<>(HoldYourBreathNetworkingConstants.HOLD_BREATH_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, HoldBreathPayload> CODEC = PacketCodec.tuple(PacketCodecs.BOOL, HoldBreathPayload::isHoldingBreath, HoldBreathPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
