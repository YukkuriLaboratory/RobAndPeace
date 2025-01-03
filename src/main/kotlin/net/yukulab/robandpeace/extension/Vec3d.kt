package net.yukulab.robandpeace.extension

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

fun Vec3d.floor() = Vec3d(kotlin.math.floor(x), kotlin.math.floor(y), kotlin.math.floor(z))

fun Vec3d.ceil() = Vec3d(kotlin.math.ceil(x), kotlin.math.ceil(y), kotlin.math.ceil(z))

fun Vec3d.round() = Vec3d(kotlin.math.round(x), kotlin.math.round(y), kotlin.math.round(z))

fun Vec3d.toCenterPos() = Vec3d(x + 0.5, y + 0.5, z + 0.5)

fun Vec3d.toBlockPos() = BlockPos(x.toCeilInt(), y.toCeilInt(), z.toCeilInt())
