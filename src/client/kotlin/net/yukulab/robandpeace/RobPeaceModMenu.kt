package net.yukulab.robandpeace

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import me.shedaniel.autoconfig.AutoConfig
import net.yukulab.robandpeace.config.RapServerConfig

object RobPeaceModMenu : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> = ConfigScreenFactory {
        AutoConfig.getConfigScreen(RapServerConfig::class.java, it).get()
    }
}
