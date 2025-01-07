package net.yukulab.robandpeace

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.yukulab.robandpeace.entity.RapEntityRenderers
import net.yukulab.robandpeace.item.RapItemModelProvider
import net.yukulab.robandpeace.network.payload.PlayerMovementPayload

object RobPeaceClient : ClientModInitializer {
    override fun onInitializeClient() {
        RapEntityRenderers.init()
        RapItemModelProvider.registerModelPredicateProviders()
        ClientPlayNetworking.registerGlobalReceiver(PlayerMovementPayload.ID) { payload, context ->
            RobAndPeace.playerMovementStatusMap[context.player().uuid] = payload
        }
    }
}
