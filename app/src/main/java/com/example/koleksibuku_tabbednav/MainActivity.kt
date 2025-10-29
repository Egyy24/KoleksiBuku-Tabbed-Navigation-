package com.example.koleksibuku_tabbednav

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                val nav = rememberNavController()
                Scaffold(
                    topBar = { TopAppBar(title = { Text("Koleksi Buku") }) }
                ) { inner ->
                    NavHost(
                        navController = nav,
                        startDestination = Routes.Home.route,
                        modifier = Modifier.padding(inner)
                    ) {
                        composable(Routes.Home.route) {
                            HomeScreen()
                        }
                    }
                }
            }
        }
    }
}
