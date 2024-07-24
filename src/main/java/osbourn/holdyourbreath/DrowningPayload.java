package osbourn.holdyourbreath;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record DrowningPayload(boolean isDrowning) implements CustomPayload {
    public static final CustomPayload.Id<DrowningPayload> ID = new CustomPayload.Id<>(HoldYourBreathNetworkingConstants.DROWNING_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, DrowningPayload> CODEC = PacketCodec.tuple(PacketCodecs.BOOL, DrowningPayload::isDrowning, DrowningPayload::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
