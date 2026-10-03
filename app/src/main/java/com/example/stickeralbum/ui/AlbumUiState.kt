package com.example.stickeralbum.ui

import com.example.stickeralbum.model.Sticker

data class AlbumUiState(
    val searchText: String = "",
    val selectedTeam: String = "All",
    val odabranoSortiranje: String = "Jersey number ↑",
    val nacinPrikaza: String = "List",
    val sveSlicice: List<Sticker> = emptyList(),
    val prikazaneSlicice: List<Sticker> = emptyList(), //rezultat nakon filtera i sortiranja
    val ucitavanje: Boolean = false,
    val porukaGreske: String = "",
    val coins: Int = 10,
    val cijenaPaketa: Int = 10,
    val zadnjiPaket: List<Sticker> = emptyList(),
    val paketUcitavanje: Boolean = false,
    val paketGreska: String = ""
)