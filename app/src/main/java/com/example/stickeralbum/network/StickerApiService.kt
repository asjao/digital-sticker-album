package com.example.stickeralbum.network

import com.example.stickeralbum.model.Sticker
import retrofit2.http.GET
import retrofit2.http.Path

interface StickerApiService {
    @GET("api/all-players")
    suspend fun getAllPlayers(): List<Sticker>

    @GET("api/random-players-unique/{count}")
    suspend fun getRandomPlayersUnique(
        @Path("count") count: Int
    ): List<Sticker>

    @GET("api/grbovi")
    suspend fun getGrbovi(): List<Sticker>
}

