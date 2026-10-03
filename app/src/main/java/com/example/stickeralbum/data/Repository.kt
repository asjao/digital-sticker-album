package com.example.stickeralbum.data

import com.example.stickeralbum.model.Sticker
import com.example.stickeralbum.network.StickerApiService

interface StickerRepository {
    suspend fun getAllStickers(): List<Sticker>

    suspend fun updateFavorite(stickerId: Int, favorite: Boolean)

    suspend fun openPack(count: Int): List<Sticker>
}
//Iznad je ugovor: ko god bude nas repository, mora imati ove tri mogucnosti
//Sad dobijemo klasu koja taj ugovor stvarno izvrsava; tj stvarno implementiramo rad repozitorija


class NetworkStickerRepository(
    private val stickerApiService: StickerApiService,
    private val stickerDao: StickerDao
) : StickerRepository {
    override suspend fun getAllStickers(): List<Sticker> {
        try {
            val igraciSaApi = stickerApiService.getAllPlayers()

            val grboviSaApi =
                stickerApiService
                    .getGrbovi()
                    .map { grb ->
                        grb.copy(
                            tip_slicice = "grb",  //stavljamo rucno jer api/grbovi nema ta polja
                            zlatna = false
                        )
                    }
            val sliciceSaApi = igraciSaApi + grboviSaApi
           //cuvamo rezultat sa servera, tj listu stikera koje smo dobili preko fukcije iz stickerapiservice
            val lokalneSlicice = stickerDao.getAllStickers()  //trenutno stanje sacuvano na telefonu

            //zasto nam trebaju oba? ovdje je nasa sumnja sa replace uzeta u obzir:
            val sliciceZaBazu = sliciceSaApi.map { stickerSaApi ->
                    val lokalnaSlicica =
                        lokalneSlicice.find { sticker -> //Da li u lokalnoj bazi vec postoji ova ista slicica koju smo upravo dobili sa servera?
                            sticker.id == stickerSaApi.id
                        }
                    if (lokalnaSlicica != null) {
                        stickerSaApi.copy(
                            tip_slicice = lokalnaSlicica.tip_slicice,
                            zlatna = lokalnaSlicica.zlatna,
                            favorite = lokalnaSlicica.favorite,
                            collected = lokalnaSlicica.collected
                        )
                    } else {
                        stickerSaApi
                    //ako ovu slicicu jos nikada nismo imali lokalno, samo koristi ono sto je doslo sa servera.
                    }
                }
            stickerDao.insertAll(sliciceZaBazu) // lista koju sada mozemo sigurno zapisati u bazu.
            return sliciceZaBazu

        } catch (e: Exception) {
            val lokalneSlicice = stickerDao.getAllStickers()  //uzmi ono sto vec imamo na telefonu
            if (lokalneSlicice.isNotEmpty()) {
                return lokalneSlicice
            } else {
                throw e
            }
        }
    }


    override suspend fun updateFavorite(stickerId: Int, favorite: Boolean) {
        stickerDao.updateFavorite(stickerId = stickerId, favorite = favorite)
    }


    override suspend fun openPack(count: Int): List<Sticker> {
        val paketSaApi = stickerApiService.getRandomPlayersUnique(count)
        val lokalneSlicice = stickerDao.getAllStickers()

        val skupljeneSlicice =
            paketSaApi.map { stickerSaApi ->

                val lokalnaSlicica =
                    lokalneSlicice.find { sticker ->
                        sticker.id == stickerSaApi.id
                    }


                if (lokalnaSlicica != null) {
                    stickerSaApi.copy(
                        favorite = lokalnaSlicica.favorite,
                        collected = true
                    )

                } else {
                    stickerSaApi.copy(
                        collected = true
                    )
                }
            }

        stickerDao.insertAll(
            skupljeneSlicice
        )

        return skupljeneSlicice
    }
}