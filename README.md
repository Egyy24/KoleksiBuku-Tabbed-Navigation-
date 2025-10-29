# Koleksi Buku - Dokumentasi Navigasi

## Pola Navigasi
Menggunakan **3 tab**: Daftar, Favorit, Sudah Dibaca.

## Mekanisme Pengiriman Data
**Data yang dikirim**: `List<Book>` dengan class `Book` yang sudah diberi anotasi `@Serializable`.

### Alur Pengiriman Data saat User Memilih Tab "Sudah Dibaca"

1. **Mengambil data**: Sistem mengambil daftar buku yang sudah ditandai sebagai "sudah dibaca" dari sumber data.
2. **Serialisasi**: Daftar buku (`List<Book>`) dikonversi menjadi format JSON string.
3. **Encoding**: JSON string tersebut di-encode menggunakan URI encoding agar aman untuk dikirim melalui navigation route.
4. **Navigasi**: Aplikasi melakukan navigasi ke route `"read/{booksJson}"` dengan membawa JSON yang sudah di-encode sebagai parameter.
5. **Deserialisasi di layar tujuan**:
   - JSON string di-decode kembali dari URI encoding
   - Dikonversi kembali menjadi `List<Book>`
   - Data ditampilkan di layar
