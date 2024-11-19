package net.yukulab.robandpeace.datagen.lang

import java.util.concurrent.CompletableFuture
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.registry.RegistryWrapper

abstract class RapLangProvider(dataGenerator: FabricDataOutput, languageCode: String, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricLanguageProvider(dataGenerator, languageCode, registryLookup) {
    abstract fun TranslationBuilder.generateTranslations(registryLookup: RegistryWrapper.WrapperLookup)

    protected fun String.configToolTip(): String = "$this.@Tooltip"

    override fun generateTranslations(registryLookup: RegistryWrapper.WrapperLookup, translationBuilder: TranslationBuilder) {
        translationBuilder.generateTranslations(registryLookup)
    }
}
