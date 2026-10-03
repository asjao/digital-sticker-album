package com.example.stickeralbum

import android.app.Application  //glavni objekat na nivou cijele aplikacije
import com.example.stickeralbum.data.AppContainer
import com.example.stickeralbum.data.DefaultAppContainer

class StickerAlbumApplication : Application() {
    lateinit var container: AppContainer  //najavljujemo da cemo ga kasnije inicijalizirati

    override fun onCreate() { //funkcija koju Android poziva kada se ovaj Application
        // objekat kreira odnosno pokrene
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}