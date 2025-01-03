package net.yukulab.robandpeace

import java.util.UUID
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.runBlocking
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.util.ActionResult
import net.yukulab.robandpeace.config.RapConfigs
import net.yukulab.robandpeace.config.RapServerConfig
import net.yukulab.robandpeace.entity.RapEntityType
import net.yukulab.robandpeace.item.RapItems
import net.yukulab.robandpeace.item.component.RapComponents
import net.yukulab.robandpeace.network.RabNetworking
import net.yukulab.robandpeace.network.payload.PlayerMovementPayload

object RobAndPeace : ModInitializer {
    private val job = SupervisorJob()
    val coroutineScope = CoroutineScope(Dispatchers.Default + job)
    lateinit var serverDispatcher: CoroutineDispatcher
        private set

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
        initCoroutines()
    }

    fun onUpdateConfig(config: RapServerConfig): ActionResult {
        isDebugMode = config.debugSettings.enabledDebugMode
        return ActionResult.SUCCESS
    }

    private fun initCoroutines() {
        ServerLifecycleEvents.SERVER_STARTED.register { server ->
            serverDispatcher = server.asCoroutineDispatcher()
        }
        ServerLifecycleEvents.SERVER_STOPPED.register {
            runBlocking {
                job.cancelAndJoin()
            }
        }
    }
}
