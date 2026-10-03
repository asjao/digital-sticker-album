package com.example.stickeralbum.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stickeralbum.StickerAlbumApplication
import com.example.stickeralbum.data.CoinsRepository
import com.example.stickeralbum.data.StickerRepository
import com.example.stickeralbum.model.Sticker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlbumViewModel(
    private val stickerRepository: StickerRepository,
    private val coinsRepository: CoinsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(AlbumUiState())
    //interna verzija

    val uiState: StateFlow<AlbumUiState> = _uiState.asStateFlow()
    //verzija koju dajemo ostatku aplikacije

    init {
        //pogledaj u SavedStateHandle da li postoji vrijednost sa kljucem "searchText".
        val sacuvanaPretraga = savedStateHandle.get<String>("searchText") ?: ""

        _uiState.update { trenutnoStanje ->
            trenutnoStanje.copy(
                searchText = sacuvanaPretraga
            )
        }
        ucitajSlicice()
        pratiCoins()
    }

    private fun ucitajSlicice() {
        _uiState.update { trenutnoStanje ->
            trenutnoStanje.copy(
                ucitavanje = true,
                porukaGreske = ""
            )
        }
        viewModelScope.launch {
            //za suspend funkcije
            try {
                val slicice = stickerRepository.getAllStickers()

                _uiState.update { trenutnoStanje ->
                    trenutnoStanje.copy(
                        sveSlicice = slicice,
                        prikazaneSlicice = slicice,
                        ucitavanje = false
                    )
                }
                azurirajPrikazaneSlicice()
            } catch (e: Exception) {
                _uiState.update { trenutnoStanje ->
                    trenutnoStanje.copy(
                        ucitavanje = false,
                        porukaGreske =
                            e.message ?: "Unknown error."
                    )
                }
            }
        }
    }

    private fun pratiCoins() {
        viewModelScope.launch {
            coinsRepository.coins.collect { noviCoins ->
                _uiState.update { stanje->
                    stanje.copy(
                        coins = noviCoins
                    )
                }
            }
        }
    }

    fun dodajQuizNagradu() {
        viewModelScope.launch {
            coinsRepository.addCoins(5)
        }
    }


    fun promijeniPretragu(noviTekst: String) {
        savedStateHandle["searchText"] = noviTekst

        _uiState.update { trenutnoStanje ->
            trenutnoStanje.copy(
                searchText = noviTekst
            )
        }
        azurirajPrikazaneSlicice()
    }

    fun promijeniReprezentaciju(
        novaReprezentacija: String
    ) {
        _uiState.update { trenutnoStanje ->
            trenutnoStanje.copy(
                selectedTeam = novaReprezentacija
            )
        }
        azurirajPrikazaneSlicice()
    }


    fun promijeniSortiranje(
        novoSortiranje: String
    ) {
        _uiState.update { trenutnoStanje ->
            trenutnoStanje.copy(
                odabranoSortiranje = novoSortiranje
            )
        }
        azurirajPrikazaneSlicice()
    }

    fun promijeniNacinPrikaza(
        noviNacinPrikaza: String
    ) {
        _uiState.update { trenutnoStanje ->
            trenutnoStanje.copy(
                nacinPrikaza = noviNacinPrikaza
            )
        }
    }



    private fun azurirajPrikazaneSlicice() {
        val trenutnoStanje = _uiState.value

        val filtriraneSlicice =
            trenutnoStanje.sveSlicice.filter { sticker ->
                val odgovaraPretrazi =
                    sticker.ime.contains(
                        trenutnoStanje.searchText,
                        ignoreCase = true
                    ) || sticker.prezime.contains(
                            trenutnoStanje.searchText,
                            ignoreCase = true
                        ) || sticker.reprezentacija.contains(
                                  trenutnoStanje.searchText,
                                    ignoreCase = true
                            )

                val odgovaraReprezentaciji =
                    trenutnoStanje.selectedTeam == "All" ||
                            sticker.reprezentacija == trenutnoStanje.selectedTeam

                odgovaraPretrazi && odgovaraReprezentaciji
            }


        val sortiraneSlicice: List<Sticker>
        if (
            trenutnoStanje.odabranoSortiranje == "Jersey number ↑"
        ) {
            sortiraneSlicice =
                filtriraneSlicice.sortedWith(
                    compareBy<Sticker> { sticker ->
                        sticker.tip_slicice == "grb"
                    }.thenBy { sticker ->
                        sticker.broj_dresa
                    }
                )

        } else {
            sortiraneSlicice =
                filtriraneSlicice.sortedWith(
                    compareBy<Sticker> { sticker ->
                        sticker.tip_slicice == "grb"
                    }.thenByDescending { sticker ->
                        sticker.broj_dresa
                    }
                )
        }

        _uiState.update { stanje ->
            stanje.copy(
                prikazaneSlicice = sortiraneSlicice
            )
        }
    }



    fun promijeniFavorite(stickerId: Int) {
        val trenutnaSlicica =
            _uiState.value.sveSlicice.find { sticker ->
                sticker.id == stickerId
            }

        if (trenutnaSlicica != null) {
            val novaVrijednost = !trenutnaSlicica.favorite

            viewModelScope.launch { //jer repository funkcija update je suspend
                stickerRepository.updateFavorite(  //lokalno sacuvamo promjenu
                    stickerId = stickerId,
                    favorite = novaVrijednost
                )

                _uiState.update { trenutnoStanje ->
                    val noveSveSlicice =
                        trenutnoStanje.sveSlicice.map { sticker ->
                            if (sticker.id == stickerId) {
                                sticker.copy(
                                    favorite = novaVrijednost
                                )
                            } else {
                                sticker
                            }
                        }

                    val novePrikazaneSlicice =
                        trenutnoStanje.prikazaneSlicice.map { sticker ->
                            if (sticker.id == stickerId) {
                                sticker.copy(
                                    favorite = novaVrijednost
                                )
                            } else {
                                sticker
                            }
                        }

                    trenutnoStanje.copy(
                        sveSlicice = noveSveSlicice,
                        prikazaneSlicice = novePrikazaneSlicice
                    )
                }
            }
        }
    }



    fun otvoriPaket() {
        val cijenaPaketa = _uiState.value.cijenaPaketa

        if (_uiState.value.coins < cijenaPaketa) {
            _uiState.update {
                it.copy(
                    paketGreska = "Not enough coins. You need $cijenaPaketa coins."
                )
            }
            return
        }

        _uiState.update { trenutnoStanje ->
            trenutnoStanje.copy(
                paketUcitavanje = true,
                paketGreska = ""
            )
        }

        viewModelScope.launch {
            try {
                val paket = stickerRepository.openPack(5)

                coinsRepository.spendCoins(cijenaPaketa)

                _uiState.update { trenutnoStanje ->
                    val noveSveSlicice =
                        trenutnoStanje.sveSlicice.map { sticker ->

                            val stickerIzPaketa =
                                paket.find { paketSticker ->
                                    paketSticker.id == sticker.id
                                }

                            if (stickerIzPaketa != null) {
                                sticker.copy(
                                    collected = true,
                                    tip_slicice = stickerIzPaketa.tip_slicice,
                                    zlatna = stickerIzPaketa.zlatna
                                )
                            } else {
                                sticker
                            }
                        }


                    val novePrikazaneSlicice =
                        trenutnoStanje.prikazaneSlicice.map { sticker ->
                            val stickerIzPaketa =
                                paket.find { paketSticker ->
                                    paketSticker.id == sticker.id
                                }

                            if (stickerIzPaketa != null) {
                                sticker.copy(
                                    collected = true,
                                    tip_slicice = stickerIzPaketa.tip_slicice,
                                    zlatna = stickerIzPaketa.zlatna
                                )
                            } else {
                                sticker
                            }
                        }

                    trenutnoStanje.copy(
                        sveSlicice = noveSveSlicice,
                        prikazaneSlicice = novePrikazaneSlicice,
                        zadnjiPaket = paket,
                        paketUcitavanje = false
                    )
                }

            } catch (e: Exception) {
                _uiState.update { trenutnoStanje ->
                    trenutnoStanje.copy(
                        paketUcitavanje = false,
                        paketGreska = "Unable to open pack."
                    )
                }
            }
        }
    }



    fun ocistiPaket() {
        _uiState.update { trenutnoStanje ->
            trenutnoStanje.copy(
                zadnjiPaket = emptyList(),
                paketGreska = ""
            )
        }
    }

    fun ocistiPaketGresku() {
        _uiState.update {
            it.copy(
                paketGreska = ""
            )
        }
    }





    companion object {
        val Factory: ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val application =
                        this[APPLICATION_KEY]
                                as StickerAlbumApplication

                    val repository =
                        application.container.stickerRepository

                    val coinsRepository =
                        application.container.coinsRepository

                    AlbumViewModel(
                        stickerRepository = repository,
                        coinsRepository = coinsRepository,
                        savedStateHandle = createSavedStateHandle()
                    )
                }
            }
    }

}