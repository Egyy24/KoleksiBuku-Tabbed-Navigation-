package com.example.koleksibuku_tabbednav

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

data class Book(
    val id: Int,
    val judul: String,
    val penulis: String,
    val coverRes: Int,
    val deskripsi: String
)

object SampleData {
    val books = listOf(
        Book(
            id = 1,
            judul = "Laut Bercerita",
            penulis = "Leila S. Chudori",
            coverRes = R.drawable.buku1,
            deskripsi = "Novel berlatar masa kelam; kisah persahabatan dan kehilangan."
        ),
        Book(
            id = 2,
            judul = "Almond",
            penulis = "Sohn Won-Pyung",
            coverRes = R.drawable.buku2,
            deskripsi = "Remaja dengan alexithymia belajar memahami emosi dan dunia."
        ),
        Book(
            id = 3,
            judul = "Sang Penebus",
            penulis = "Wally Lamb",
            coverRes = R.drawable.buku3,
            deskripsi = "Kisah keluarga, luka, dan penebusan—pemenang berbagai penghargaan."
        )
    )
}

enum class HomeTab(val label: String) { DAFTAR("Daftar"), FAVORIT("Favorit"), STATISTIK("Statistik") }

@Composable
fun HomeScreen(onOpenDetail: (Int) -> Unit) {
    var selected by remember { mutableStateOf(HomeTab.DAFTAR) }
    val favoriteIds = remember { mutableStateListOf<Int>() }

    Column {
        TabRow(selectedTabIndex = selected.ordinal) {
            HomeTab.values().forEachIndexed { i, tab ->
                Tab(
                    selected = i == selected.ordinal,
                    onClick = { selected = tab },
                    text = { Text(tab.label) }
                )
            }
        }

        when (selected) {
            HomeTab.DAFTAR -> BookList(
                books = SampleData.books,
                isFavorite = { it in favoriteIds },
                onToggleFavorite = { id ->
                    if (id in favoriteIds) favoriteIds.remove(id) else favoriteIds.add(id)
                },
                onOpenDetail = onOpenDetail
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
                        onToggleFavorite = { id ->
                            if (id in favoriteIds) favoriteIds.remove(id) else favoriteIds.add(id)
                        },
                        onOpenDetail = onOpenDetail
                    )
                }
            }

            HomeTab.STATISTIK -> StatsScreen(
                total = SampleData.books.size,
                fav = favoriteIds.size
            )
        }
    }
}

@Composable
private fun BookList(
    books: List<Book>,
    isFavorite: (Int) -> Boolean,
    onToggleFavorite: (Int) -> Unit,
    onOpenDetail: (Int) -> Unit
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
                onToggleFavorite = { onToggleFavorite(b.id) },
                onClick = { onOpenDetail(b.id) }
            )
        }
    }
}

@Composable
private fun BookRow(
    book: Book,
    favorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
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
        }
    }
}

@Composable
private fun StatsScreen(total: Int, fav: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Statistik", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("Total Buku: $total", style = MaterialTheme.typography.bodyLarge)
        Text("Jumlah Favorit: $fav", style = MaterialTheme.typography.bodyLarge)
        Divider()
        Text("Tips: tap bintang di daftar untuk menambah/menghapus favorit.",
            style = MaterialTheme.typography.bodyMedium)
    }
}
