package net.yukulab.robandpeace.network

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.util.Identifier
import net.yukulab.robandpeace.MOD_ID
import net.yukulab.robandpeace.network.payload.ForwardMovingPayload
import net.yukulab.robandpeace.network.payload.JumpingPayload

object RabNetworking {
    val FORWARD_MOVING_PACKET: Identifier = Identifier.of(MOD_ID, "forward_moving")
    val JUMPING_PACKET: Identifier = Identifier.of(MOD_ID, "jumping")

    // TODO: add receiver
    fun init() {
        PayloadTypeRegistry.playC2S().register(ForwardMovingPayload.ID, ForwardMovingPayload.CODEC)
        PayloadTypeRegistry.playC2S().register(JumpingPayload.ID, JumpingPayload.CODEC)

        ServerPlayNetworking.registerGlobalReceiver(ForwardMovingPayload.ID) { payload, context ->
        }
    }
}
