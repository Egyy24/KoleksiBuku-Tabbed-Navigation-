package com.example.koleksibuku_tabbednav

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import androidx.compose.foundation.layout.padding


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                val nav = rememberNavController()
                val backStack by nav.currentBackStackEntryAsState()
                val current = backStack?.destination?.route.orEmpty()
                val showBack = current.startsWith("detail/") || current == Routes.Detail.route

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(if (showBack) "Detail Buku" else "Koleksi Buku") },
                            navigationIcon = {
                                if (showBack) {
                                    IconButton(onClick = { nav.popBackStack() }) {
                                        Icon(
                                            painter = painterResource(android.R.drawable.ic_media_previous),
                                            contentDescription = "Kembali"
                                        )
                                    }
                                }
                            }
                        )
                    }
                ) { inner ->
                    NavHost(
                        navController = nav,
                        startDestination = Routes.Home.route,
                        modifier = Modifier.padding(inner)
                    ) {
                        composable(Routes.Home.route) {
                            HomeScreen(onOpenDetail = { id ->
                                nav.navigate(Routes.Detail.path(id))
                            })
                        }
                        composable(
                            route = Routes.Detail.route,
                            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
                        ) { entry ->
                            val id = entry.arguments!!.getInt("bookId")
                            val book = remember(id) { SampleData.books.first { it.id == id } }
                            BookDetailScreen(book = book)
                        }
                    }
                }
            }
        }
    }
}