package net.yukulab.robandpeace

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import net.minecraft.util.Identifier

const val MOD_ID = "robandpeace"

const val NAMESPACE = "Rob&Peace"

val coroutineScope: CoroutineScope
    get() = RobAndPeace.coroutineScope

val serverDispatcher: CoroutineDispatcher
    get() = RobAndPeace.serverDispatcher

fun id(path: String): Identifier = Identifier.of(MOD_ID, path)

fun getTextureId(path: String): Identifier = id("textures/$path")
