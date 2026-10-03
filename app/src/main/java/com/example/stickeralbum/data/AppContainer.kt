package com.example.stickeralbum.data

import com.example.stickeralbum.network.StickerApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import android.content.Context

interface AppContainer {
    val stickerRepository: StickerRepository
    val coinsRepository: CoinsRepository
}

class DefaultAppContainer(
    private val context: Context
) : AppContainer {

    private val database = StickerDatabase.getDatabase(context)
    private val stickerDao = database.stickerDao()
    private val baseUrl = "http://49.13.125.189:3300/"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(
            json.asConverterFactory(
                "application/json".toMediaType()
            )
        )
        .baseUrl(baseUrl)
        .build()


    private val retrofitService: StickerApiService by lazy {  //Retrofit napravi StickerApiService
        retrofit.create(
            StickerApiService::class.java
        )
    }


    /*onda container taj servis injectuje u NetworkStickerRepository*/
    override val stickerRepository: StickerRepository by lazy {

        NetworkStickerRepository(
            stickerApiService = retrofitService,
            stickerDao = stickerDao
        )
    }

    override val coinsRepository: CoinsRepository by lazy {
        CoinsRepository(context)
    }

}