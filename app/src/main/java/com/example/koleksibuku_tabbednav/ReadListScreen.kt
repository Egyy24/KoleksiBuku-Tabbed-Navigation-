package com.example.koleksibuku_tabbednav

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun ReadListScreen(books: List<Book>) {
    if (books.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            Text("Belum ada yang ditandai sudah dibaca.")
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(books) { b ->
                Row(Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(b.coverRes),
                        contentDescription = b.judul,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(b.judul, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(b.penulis, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            b.deskripsi,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
