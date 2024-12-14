package net.yukulab.robandpeace

import net.minecraft.util.Identifier

const val MOD_ID = "robandpeace"

const val NAMESPACE = "Rob&Peace"

fun id(path: String): Identifier = Identifier.of(MOD_ID, path)

fun getTextureId(path: String): Identifier = id("textures/$path")
