package com.example.koleksibuku_tabbednav

sealed class Routes(val route: String) {
    data object Home : Routes("home")
}
