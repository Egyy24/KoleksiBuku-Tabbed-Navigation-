package com.example.koleksibuku_tabbednav

sealed class Routes(val route: String) {
    data object Home : Routes("home")
    data object Detail : Routes("detail/{bookId}") {
        fun path(bookId: Int) = "detail/$bookId"
    }
}
