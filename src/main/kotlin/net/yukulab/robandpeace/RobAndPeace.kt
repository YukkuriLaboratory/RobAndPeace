package net.yukulab.robandpeace

import java.util.UUID
import net.fabricmc.api.ModInitializer
import net.minecraft.util.ActionResult
import net.yukulab.robandpeace.config.RapConfigs
import net.yukulab.robandpeace.config.RapServerConfig
import net.yukulab.robandpeace.entity.RapEntityType
import net.yukulab.robandpeace.item.RapItems
import net.yukulab.robandpeace.item.component.RapComponents
import net.yukulab.robandpeace.network.RabNetworking
import net.yukulab.robandpeace.network.payload.PlayerMovementPayload

object RobAndPeace : ModInitializer {

    val EMPTY_PAYLOAD = PlayerMovementPayload(false, 0.0f, false)

    @JvmStatic
    var isDebugMode: Boolean = false

    @JvmField
    val playerMovementStatusMap = mutableMapOf<UUID, PlayerMovementPayload>()

    @JvmStatic
    fun getPlayerMovementStatus(playerUUID: UUID): PlayerMovementPayload = playerMovementStatusMap.getOrDefault(playerUUID, EMPTY_PAYLOAD)

    override fun onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        RapConfigs.init()
        RapComponents.init()
        RapItems.init()
        RapEntityType.init()
        RabNetworking.init()
    }

    fun onUpdateConfig(config: RapServerConfig): ActionResult {
        isDebugMode = config.debugSettings.enabledDebugMode
        return ActionResult.SUCCESS
    }
}
