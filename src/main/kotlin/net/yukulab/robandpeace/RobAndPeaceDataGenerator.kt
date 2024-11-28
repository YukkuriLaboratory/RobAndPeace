package net.yukulab.robandpeace

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.yukulab.robandpeace.datagen.RapModelProvider
import net.yukulab.robandpeace.datagen.lang.RapEnglishLangProvider
import net.yukulab.robandpeace.datagen.lang.RapJapaneseLangProvider

object RobAndPeaceDataGenerator : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
        val pack = fabricDataGenerator.createPack()
        pack.addProvider(::RapEnglishLangProvider)
        pack.addProvider(::RapJapaneseLangProvider)
        pack.addProvider(::RapModelProvider)
    }
}
