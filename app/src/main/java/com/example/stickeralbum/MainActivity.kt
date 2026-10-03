package com.example.stickeralbum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.stickeralbum.navigation.StickerAlbumApp
import com.example.stickeralbum.ui.theme.StickerAlbumTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass

class MainActivity : ComponentActivity() {
   @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StickerAlbumTheme {

                val windowSize =
                    calculateWindowSizeClass(this)

                StickerAlbumApp(
                    windowSize = windowSize.widthSizeClass
                )
            }
        }
    }
}

