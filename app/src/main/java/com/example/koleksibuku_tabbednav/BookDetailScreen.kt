package com.example.koleksibuku_tabbednav

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BookDetailScreen(book: Book) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Image(
            painter = painterResource(book.coverRes),
            contentDescription = book.judul,
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(book.judul, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Penulis: ${book.penulis}", style = MaterialTheme.typography.bodyMedium)
        Divider()
        Text(book.deskripsi, style = MaterialTheme.typography.bodyLarge)
    }
}
