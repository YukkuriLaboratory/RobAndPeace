package net.yukulab.robandpeace

import net.fabricmc.api.ClientModInitializer
import net.yukulab.robandpeace.entity.RapEntityRenderers
import net.yukulab.robandpeace.item.RapItemModelProvider

object RobPeaceClient : ClientModInitializer {
    override fun onInitializeClient() {
        RapEntityRenderers.init()
        RapItemModelProvider.registerModelPredicateProviders()
    }
}
