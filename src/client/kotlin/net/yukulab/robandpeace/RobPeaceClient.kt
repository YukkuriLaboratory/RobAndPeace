package net.yukulab.robandpeace

import net.fabricmc.api.ClientModInitializer
import net.yukulab.robandpeace.entity.RapEntityRenderers

object RobPeaceClient : ClientModInitializer {
    override fun onInitializeClient() {
        RapEntityRenderers.init()
    }
}
