package net.yukulab.robandpeace

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.MinecraftClient
import net.yukulab.robandpeace.entity.RapEntityRenderers
import net.yukulab.robandpeace.item.RapItemModelProvider
import net.yukulab.robandpeace.network.payload.PlayerMovementPayload

object RobPeaceClient : ClientModInitializer {
    override fun onInitializeClient() {
        RapEntityRenderers.init()
        RapItemModelProvider.registerModelPredicateProviders()
        ClientPlayNetworking.registerGlobalReceiver(PlayerMovementPayload.ID) { payload, context ->
            logger.info(
                "Player movement updated! uuid:{} myuuid:{}",
                payload.playerUUID,
                MinecraftClient.getInstance().player?.uuid,
            )
            RobAndPeace.playerMovementStatusMap[payload.playerUUID] = payload
        }
    }

    private val logger by DelegatedLogger()
}
