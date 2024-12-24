package net.yukulab.robandpeace.network

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.util.Identifier
import net.yukulab.robandpeace.MOD_ID
import net.yukulab.robandpeace.RobAndPeace
import net.yukulab.robandpeace.network.payload.PlayerMovementPayload

object RabNetworking {
    val PLAYER_MOVEMENT_PACKET: Identifier = Identifier.of(MOD_ID, "player_movement")

    // TODO: add receiver
    fun init() {
        PayloadTypeRegistry.playC2S().register(PlayerMovementPayload.ID, PlayerMovementPayload.CODEC)

        ServerPlayNetworking.registerGlobalReceiver(PlayerMovementPayload.ID) { payload, context ->
            RobAndPeace.playerMovementStatusMap[context.player().uuid] = payload
        }
    }
}
