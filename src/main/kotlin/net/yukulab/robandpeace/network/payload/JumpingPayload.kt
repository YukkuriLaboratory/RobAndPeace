package net.yukulab.robandpeace.network.payload

import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.yukulab.robandpeace.network.RabNetworking

data class JumpingPayload(val isJumping: Boolean) : CustomPayload {
    companion object {
        val CODEC = PacketCodec.tuple(PacketCodecs.BOOL, JumpingPayload::isJumping, ::JumpingPayload)

        val ID = CustomPayload.Id<JumpingPayload>(RabNetworking.JUMPING_PACKET)
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> = ID
}
