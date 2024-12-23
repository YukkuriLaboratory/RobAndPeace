package net.yukulab.robandpeace.network.payload

import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.yukulab.robandpeace.network.RabNetworking

data class ForwardMovingPayload(val isForwardMoving: Boolean) : CustomPayload {
    companion object {
        val CODEC: PacketCodec<ByteBuf, ForwardMovingPayload> = PacketCodec.tuple(PacketCodecs.BOOL, ForwardMovingPayload::isForwardMoving, ::ForwardMovingPayload)

        val ID = CustomPayload.Id<ForwardMovingPayload>(RabNetworking.FORWARD_MOVING_PACKET)
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> = ID
}
