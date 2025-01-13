package net.yukulab.robandpeace.network.payload

import io.netty.buffer.ByteBuf
import java.util.UUID
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Uuids
import net.yukulab.robandpeace.network.RabNetworking

data class PlayerMovementPayload(val playerUUID: UUID, val hasForwardMovement: Boolean, val movementForward: Float, val isJumping: Boolean, val isSneaking: Boolean) : CustomPayload {
    companion object {
        val CODEC: PacketCodec<ByteBuf, PlayerMovementPayload> = PacketCodec.tuple(
            Uuids.PACKET_CODEC,
            PlayerMovementPayload::playerUUID,
            PacketCodecs.BOOL,
            PlayerMovementPayload::hasForwardMovement,
            PacketCodecs.FLOAT,
            PlayerMovementPayload::movementForward,
            PacketCodecs.BOOL,
            PlayerMovementPayload::isJumping,
            PacketCodecs.BOOL,
            PlayerMovementPayload::isSneaking,
            ::PlayerMovementPayload,
        )

        val ID = CustomPayload.Id<PlayerMovementPayload>(RabNetworking.PLAYER_MOVEMENT_PACKET)
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> = ID
}
