package com.dannyrodrygues.printworth

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform