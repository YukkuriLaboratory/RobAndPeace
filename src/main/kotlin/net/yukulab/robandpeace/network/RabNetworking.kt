package net.yukulab.robandpeace.network

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.util.Identifier
import net.yukulab.robandpeace.MOD_ID
import net.yukulab.robandpeace.extension.MovementPayloadHolder
import net.yukulab.robandpeace.network.payload.PlayerMovementPayload

object RabNetworking {
    val PLAYER_MOVEMENT_PACKET: Identifier = Identifier.of(MOD_ID, "player_movement")

    fun init() {
        PayloadTypeRegistry.playC2S().register(PlayerMovementPayload.ID, PlayerMovementPayload.CODEC)
        // PayloadTypeRegistry.playS2C().register(PlayerMovementPayload.ID, PlayerMovementPayload.CODEC)

        ServerPlayNetworking.registerGlobalReceiver(PlayerMovementPayload.ID) { payload, context ->
            val player = context.player()
            if (player is MovementPayloadHolder) {
                player.`robandpeace$setPlayerMovementPayload`(payload)
            }
            // context.server().playerManager.playerList.forEach {
            //     if (it != player) {
            //         ServerPlayNetworking.send(it, payload)
            //     }
            // }
        }
    }
}
