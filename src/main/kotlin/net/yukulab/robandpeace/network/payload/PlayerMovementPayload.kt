package net.yukulab.robandpeace.network.payload

import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.yukulab.robandpeace.network.RabNetworking

data class PlayerMovementPayload(val hasForwardMovement: Boolean, val movementForward: Float, val isJumping: Boolean) : CustomPayload {
    companion object {
        val CODEC: PacketCodec<ByteBuf, PlayerMovementPayload> = PacketCodec.tuple(
            PacketCodecs.BOOL,
            PlayerMovementPayload::hasForwardMovement,
            PacketCodecs.FLOAT,
            PlayerMovementPayload::movementForward,
            PacketCodecs.BOOL,
            PlayerMovementPayload::isJumping,
            ::PlayerMovementPayload,
        )

        val ID = CustomPayload.Id<PlayerMovementPayload>(RabNetworking.PLAYER_MOVEMENT_PACKET)
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> = ID
}
