package com.example.stickeralbum.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.stickeralbum.model.Sticker

@Dao
interface StickerDao {
    @Query("SELECT * FROM stickers ORDER BY id")
    suspend fun getAllStickers(): List<Sticker>

    @Insert(onConflict = OnConflictStrategy.REPLACE)  //ako pokusamo ubaciti isti stiker opet: zamijeni postojeci red novim
    suspend fun insertAll(stickers: List<Sticker>)  //kako replace ne resetuje favorite i collected? - uradila, repository

    @Query("UPDATE stickers SET favorite = :favorite WHERE id = :stickerId")
    suspend fun updateFavorite(  // :favorite znaci - uzmi vrijednost paramtera funkcije koji se zove favorite
        stickerId: Int,
        favorite: Boolean
    )

}