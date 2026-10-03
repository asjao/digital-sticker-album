package com.example.stickeralbum.ui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.SystemClock
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.stickeralbum.R
import com.example.stickeralbum.model.Sticker
import com.example.stickeralbum.ui.theme.DarkText
import com.example.stickeralbum.ui.theme.DeepGreen
import com.example.stickeralbum.ui.theme.Gold
import com.example.stickeralbum.ui.theme.LightGold
import com.example.stickeralbum.ui.theme.SoftGray
import com.example.stickeralbum.ui.theme.WarmWhite
import kotlin.math.sqrt

@Composable
fun PackScreen(
    uiState: AlbumUiState,
    onOpenPack: () -> Unit,
    onOpenQuiz: () -> Unit,
    onDismissPackError: () -> Unit,
    modifier: Modifier = Modifier
) {
    //Shake to open --------------------------------------------------------------

    val context = LocalContext.current
    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    val accelerometer = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }
    var vrijemeZadnjegShakea by remember { mutableStateOf(0L) }

    val packScale by animateFloatAsState(
        targetValue = if (uiState.paketUcitavanje) 1.05f else 1f,
        label = "packScale"
    )

    DisposableEffect(accelerometer, uiState.paketUcitavanje) {
        val sensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val jacinaTresenja = sqrt(
                    (x * x + y * y + z * z).toDouble()
                )

                val trenutnoVrijeme = SystemClock.elapsedRealtime()
                val prosloDovoljnoVremena =
                    trenutnoVrijeme - vrijemeZadnjegShakea > 1500

                if (
                    jacinaTresenja > 25 &&
                    prosloDovoljnoVremena &&
                    !uiState.paketUcitavanje
                ) {
                    vrijemeZadnjegShakea = trenutnoVrijeme
                    onOpenPack()
                }
            }

            override fun onAccuracyChanged(
                sensor: Sensor?,
                accuracy: Int
            ) {
            }
        }

        if (accelerometer != null) {
            sensorManager.registerListener(
                sensorListener,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        onDispose {
            sensorManager.unregisterListener(sensorListener)
        }
    }

    //prelaz zatvoren u otvoren paket------------------------------------------------------------

    Crossfade(
        targetState = uiState.zadnjiPaket.isEmpty(),
        label = "packScreen"
    ) { paketJeZatvoren ->
        if (paketJeZatvoren) {
            ClosedPackContent(
                uiState = uiState,
                packScale = packScale,
                onOpenPack = onOpenPack,
                onOpenQuiz = onOpenQuiz,
                modifier = modifier
            )
        } else {
            OpenedPackContent(
                uiState = uiState,
                onOpenPack = onOpenPack,
                onOpenQuiz = onOpenQuiz,
                modifier = modifier
            )
        }
    }

    //Error

    val nemaDovoljnoCoinsa = uiState.coins < uiState.cijenaPaketa

    if (uiState.paketGreska.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = onDismissPackError,
            title = {
                Text(
                    text = if (nemaDovoljnoCoinsa) "Not enough coins" else "Unable to open pack",
                    fontWeight = FontWeight.Bold,
                    color = DeepGreen
                )
            },
            text = { Text(text = uiState.paketGreska) },
            confirmButton = {
                if (nemaDovoljnoCoinsa) {
                    Button(
                        onClick = {
                            onDismissPackError()
                            onOpenQuiz()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DeepGreen)
                    ) {
                        Text(text = "Play Quiz")
                    }
                } else {
                    Button(
                        onClick = onDismissPackError,
                        colors = ButtonDefaults.buttonColors(containerColor = DeepGreen)
                    ) {
                        Text(text = "OK")
                    }
                }
            },
            dismissButton = {
                if (nemaDovoljnoCoinsa) {
                    TextButton(onClick = onDismissPackError) {
                        Text(text = "Close")
                    }
                }
            }

        )
    }

}



//Zatvoreni paket---------------------------------------------------------------------------

@Composable
fun ClosedPackContent(
    uiState: AlbumUiState,
    packScale: Float,
    onOpenPack: () -> Unit,
    onOpenQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.splash_background_1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DeepGreen.copy(alpha = 0.86f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 22.dp,
                    end = 22.dp,
                    top = 18.dp,
                    bottom = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Your pack is ready!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Tap the pack or shake your device to open it.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.82f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                //Balance------------------------------------------------------------------------
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    color = WarmWhite,
                    shape = RoundedCornerShape(50.dp),
                    border = BorderStroke(2.dp, Gold)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Balance: ",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = DeepGreen
                        )

                        Image(
                            painter = painterResource(id = R.drawable.coin_icon),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(7.dp))

                        Text(
                            text = "${uiState.coins}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = DeepGreen
                        )
                    }
                }

                // QUIZ
                Button(
                    onClick = onOpenQuiz,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(50.dp),
                    border = BorderStroke(2.dp, Color.White),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Gold,
                        contentColor = DarkText
                    )
                ) {
                    Text(
                        text = "Play Quiz",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sticker_pack),
                    contentDescription = "Sticker pack",
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.78f)
                        .scale(packScale)
                        .clickable(enabled = !uiState.paketUcitavanje) {
                            onOpenPack()
                        },
                    contentScale = ContentScale.Fit
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Pack price: ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.75f)
                )

                Spacer(modifier = Modifier.width(7.dp))

                Image(
                    painter = painterResource(id = R.drawable.coin_icon),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "${uiState.cijenaPaketa}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Gold
                )
            }

            Spacer(modifier = Modifier.height(13.dp))

            //Tap ili shake
            if (uiState.paketUcitavanje) {
                CircularProgressIndicator(
                    modifier = Modifier.size(30.dp),
                    color = Gold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Opening pack...",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    PackActionHint(
                        image = R.drawable.tap_icon,
                        text = "Tap to open"
                    )

                    PackActionHint(
                        image = R.drawable.shake_icon,
                        text = "Shake device"
                    )
                }
            }
        }
    }
}


// Tap/shake element

@Composable
fun PackActionHint(
    image: Int,
    text: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = image),
            contentDescription = null,
            modifier = Modifier.size(42.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = Color.White.copy(alpha = 0.90f)
        )
    }
}






//Otvoreni paket---------------------------------------------------------------------------



@Composable
fun OpenedPackContent(
    uiState: AlbumUiState,
    onOpenPack: () -> Unit,
    onOpenQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    var prikaziSlicice by remember(uiState.zadnjiPaket) {
        mutableStateOf(false)
    }

    LaunchedEffect(uiState.zadnjiPaket) {
        prikaziSlicice = true
    }

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.splash_background_1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DeepGreen.copy(alpha = 0.93f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // NASLOV
            Text(
                text = "Your Stickers",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            Text(
                text = "New stickers added to your collection",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.72f)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // BALANCE + QUIZ
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    color = WarmWhite,
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Balance: ",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = DeepGreen
                        )

                        Image(
                            painter = painterResource(id = R.drawable.coin_icon),
                            contentDescription = null,
                            modifier = Modifier.size(22.dp)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = "${uiState.coins}",
                            fontWeight = FontWeight.Bold,
                            color = DeepGreen
                        )
                    }
                }

                OutlinedButton(
                    onClick = onOpenQuiz,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    shape = RoundedCornerShape(50.dp),
                    border = BorderStroke(1.dp, Gold)
                ) {
                    Text(
                        text = "Play Quiz",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 5 SLICICA
            AnimatedVisibility(
                visible = prikaziSlicice,
                enter = fadeIn(animationSpec = tween(500)) +
                        slideInVertically(
                            animationSpec = tween(500),
                            initialOffsetY = { it / 5 }
                        )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val prviRed = uiState.zadnjiPaket.take(3)

                    Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                        prviRed.forEach { sticker ->
                            PackStickerCard(sticker = sticker)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val drugiRed = uiState.zadnjiPaket.drop(3)

                    Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                        drugiRed.forEach { sticker ->
                            PackStickerCard(sticker = sticker)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // OTVORI NOVI PAKET--------------------------------------------------
            Button(
                onClick = onOpenPack,
                enabled = !uiState.paketUcitavanje,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = WarmWhite,
                    contentColor = DeepGreen
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Open another pack",
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Image(
                        painter = painterResource(id = R.drawable.coin_icon),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(3.dp))

                    Text(
                        text = "${uiState.cijenaPaketa}",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}



//----------------------------------------JEDNA SLICICA-------------------------

@Composable
fun PackStickerCard(
    sticker: Sticker,
    modifier: Modifier = Modifier
) {
    val jeGrb = sticker.tip_slicice == "grb"
    val nazivSlicice = if (jeGrb) {
        "Grb ${sticker.reprezentacija}"
    } else {
        "${sticker.ime} ${sticker.prezime}"
    }
    val imageUrl = "http://49.13.125.189:3300" + sticker.slicica_lokacija

    val bojaOkvira = if (sticker.zlatna) {
        Gold
    } else {
        Color.White.copy(alpha = 0.65f)
    }

    Card(
        modifier = modifier.width(100.dp),
        shape = RoundedCornerShape(13.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (sticker.zlatna) {
                LightGold.copy(alpha = 0.28f)
            } else {
                WarmWhite
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = BorderStroke(
            width = if (sticker.zlatna) 2.dp else 1.dp,
            color = bojaOkvira
        )
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(112.dp)
                    .background(
                        if (sticker.zlatna) {
                            LightGold.copy(alpha = 0.45f)
                        } else {
                            LightGold.copy(alpha = 0.10f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = nazivSlicice,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )

                if (sticker.zlatna && !jeGrb) {
                    Image(
                        painter = painterResource(R.drawable.gold_badge),
                        contentDescription = "Gold sticker",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(5.dp)
                            .size(34.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }



            Column(modifier = Modifier.padding(7.dp)) {
                Text(
                    text = nazivSlicice,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (jeGrb) {
                    Text(
                        text = "Crest",
                        style = MaterialTheme.typography.labelSmall,
                        color = SoftGray
                    )
                } else {
                    Text(
                        text = "#${sticker.broj_dresa} • ${sticker.pozicija}",
                        style = MaterialTheme.typography.labelSmall,
                        color = SoftGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }


            }
        }
    }

}