package com.example.koleksibuku_tabbednav

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

//data
@Serializable
data class Book(
    val id: Int,
    val judul: String,
    val penulis: String,
    val coverRes: Int,
    val deskripsi: String
)
object SampleData {
    val books = listOf(
        Book(1, "Laut Bercerita", "Leila S. Chudori", R.drawable.buku1,
            "Novel berlatar masa kelam; kisah persahabatan dan kehilangan."),
        Book(2, "Almond", "Sohn Won-Pyung", R.drawable.buku2,
            "Remaja dengan alexithymia belajar memahami emosi dan dunia."),
        Book(3, "Sang Penebus", "Wally Lamb", R.drawable.buku3,
            "Kisah keluarga, luka, dan penebusan—pemenang berbagai penghargaan.")
    )
}

// Tabs
enum class HomeTab(val label: String) { DAFTAR("Daftar"), FAVORIT("Favorit"), SUDAH_DIBACA("Sudah Dibaca") }

@Composable
fun HomeScreen() {
    var selected by remember { mutableStateOf(HomeTab.DAFTAR) }

    val favoriteIds = rememberSaveable(
        saver = listSaver(save = { it.toList() }, restore = { it.toMutableStateList() })
    ) { mutableStateListOf<Int>() }
    val readIds = rememberSaveable(
        saver = listSaver(save = { it.toList() }, restore = { it.toMutableStateList() })
    ) { mutableStateListOf<Int>() }

    val tabNav = rememberNavController()
    val tabBackStack by tabNav.currentBackStackEntryAsState()
    val currentChild = tabBackStack?.destination?.route ?: "main"

    Column {
        TabRow(selectedTabIndex = selected.ordinal) {
            HomeTab.values().forEachIndexed { i, tab ->
                Tab(
                    selected = i == selected.ordinal,
                    onClick = {
                        when (tab) {
                            HomeTab.SUDAH_DIBACA -> {
                                val readBooks = SampleData.books.filter { it.id in readIds }
                                val payload = Uri.encode(Json.encodeToString(readBooks))
                                if (!currentChild.startsWith("read/")) {
                                    tabNav.navigate("read/$payload")
                                } else {
                                    tabNav.popBackStack()
                                    tabNav.navigate("read/$payload")
                                }
                                selected = HomeTab.SUDAH_DIBACA
                            }
                            else -> {
                                if (currentChild != "main") tabNav.popBackStack()
                                selected = tab
                            }
                        }
                    },
                    text = { Text(tab.label) }
                )
            }
        }

        NavHost(
            navController = tabNav,
            startDestination = "main",
            modifier = Modifier
                .fillMaxSize()
        ) {
            composable("main") {
                when (selected) {
                    HomeTab.DAFTAR -> BookList(
                        books = SampleData.books,
                        isFavorite = { it in favoriteIds },
                        isRead = { it in readIds },
                        onToggleFavorite = { id ->
                            if (id in favoriteIds) favoriteIds.remove(id) else favoriteIds.add(id)
                        },
                        onToggleRead = { id ->
                            if (id in readIds) readIds.remove(id) else readIds.add(id)
                        }
                    )
                    HomeTab.FAVORIT -> {
                        val fav = SampleData.books.filter { it.id in favoriteIds }
                        if (fav.isEmpty()) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("Belum ada favorit.")
                            }
                        } else {
                            BookList(
                                books = fav,
                                isFavorite = { it in favoriteIds },
                                isRead = { it in readIds },
                                onToggleFavorite = { id ->
                                    if (id in favoriteIds) favoriteIds.remove(id) else favoriteIds.add(id)
                                },
                                onToggleRead = { id ->
                                    if (id in readIds) readIds.remove(id) else readIds.add(id)
                                }
                            )
                        }
                    }
                    else -> Unit
                }
            }

            composable(
                route = "read/{booksJson}",
                arguments = listOf(navArgument("booksJson") { type = NavType.StringType })
            ) { entry ->
                val raw = entry.arguments!!.getString("booksJson")!!
                val books = Json.decodeFromString<List<Book>>(Uri.decode(raw))
                ReadListScreen(books = books)
            }
        }
    }
}

@Composable
private fun BookList(
    books: List<Book>,
    isFavorite: (Int) -> Boolean,
    isRead: (Int) -> Boolean,
    onToggleFavorite: (Int) -> Unit,
    onToggleRead: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(books) { b ->
            BookRow(
                book = b,
                favorite = isFavorite(b.id),
                read = isRead(b.id),
                onToggleFavorite = { onToggleFavorite(b.id) },
                onToggleRead = { onToggleRead(b.id) }
            )
        }
    }
}

@Composable
private fun BookRow(
    book: Book,
    favorite: Boolean,
    read: Boolean,
    onToggleFavorite: () -> Unit,
    onToggleRead: () -> Unit
) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Row(Modifier
            .fillMaxWidth()
            .padding(12.dp)
        ) {
            Image(
                painter = painterResource(book.coverRes),
                contentDescription = book.judul,
                modifier = Modifier.size(76.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(book.judul, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(book.penulis, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(6.dp))
                Text(
                    book.deskripsi,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    painter = painterResource(
                        if (favorite) android.R.drawable.btn_star_big_on
                        else android.R.drawable.btn_star_big_off
                    ),
                    contentDescription = if (favorite) "Hapus favorit" else "Tambah favorit",
                    tint = Color.Unspecified
                )
            }
            IconButton(onClick = onToggleRead) {
                Icon(
                    painter = painterResource(
                        if (read) android.R.drawable.checkbox_on_background
                        else android.R.drawable.checkbox_off_background
                    ),
                    contentDescription = if (read) "Tandai belum dibaca" else "Tandai sudah dibaca",
                    tint = Color.Unspecified
                )
            }
        }
    }
}
