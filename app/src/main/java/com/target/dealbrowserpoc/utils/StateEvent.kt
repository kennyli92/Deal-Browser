package com.target.dealbrowserpoc.utils

data class StateEvent<out S, out E>(
    val state: S,
    val event: E
)
