package net.yukulab.robandpeace.extension

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

val Int.ticks: Duration
    get() = (this * 50).milliseconds
