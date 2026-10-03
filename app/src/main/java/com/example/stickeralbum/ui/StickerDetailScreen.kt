package com.example.stickeralbum.ui

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.stickeralbum.R
import com.example.stickeralbum.model.Sticker
import com.example.stickeralbum.ui.theme.Cream
import com.example.stickeralbum.ui.theme.DarkText
import com.example.stickeralbum.ui.theme.DeepGreen
import com.example.stickeralbum.ui.theme.Gold
import com.example.stickeralbum.ui.theme.LightGold
import com.example.stickeralbum.ui.theme.SoftGray
import com.example.stickeralbum.ui.theme.WarmWhite


@Composable
fun StickerDetailScreen(
    sticker: Sticker?,
    onFavoriteClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val scrollState = rememberScrollState()

    if (sticker != null) {
        val jeGrb = sticker.tip_slicice == "grb"

        val tipZaPrikaz =
            if (jeGrb) {
                "Crest"
            } else if (sticker.zlatna) {
                "Gold"
            } else {
                "Regular"
            }

        val nazivSlicice =
            if (jeGrb) {
                "Grb ${sticker.reprezentacija}"
            } else {
                "${sticker.ime} ${sticker.prezime}"
            }

        val imageUrl =
            "http://49.13.125.189:3300" + sticker.slicica_lokacija


        val imageColorFilter =
            if (!sticker.collected) {

                val colorMatrix = ColorMatrix().apply {
                    setToSaturation(0f)
                }

                ColorFilter.colorMatrix(colorMatrix)

            } else {
                null
            }


        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Cream)
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {


            // SLIKA----------------------------------------------------------------

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = WarmWhite
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                border = BorderStroke(2.dp, Gold)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(390.dp)
                        .background(
                            MaterialTheme.colorScheme
                                .surfaceVariant
                                .copy(alpha = 0.35f)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    AsyncImage(
                        model = imageUrl,
                        contentDescription = nazivSlicice,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit,
                        colorFilter = imageColorFilter
                    )



                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(14.dp),

                        color =
                            if (sticker.collected) {
                                DeepGreen
                            } else {
                                LightGold
                            },

                        contentColor =
                            if (sticker.collected) {
                                Color.White
                            } else {
                                DarkText
                            },

                        shape = RoundedCornerShape(50.dp)
                    ) {

                        Text(
                            text =
                                if (sticker.collected) {
                                    stringResource(R.string.collected)
                                } else {
                                    stringResource(R.string.missing)
                                },

                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,

                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 6.dp
                            )
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(22.dp))


            //NAZIV SLICICE------------------------------------------------

            Text(
                text = nazivSlicice,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = DeepGreen,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )


            Spacer(modifier = Modifier.height(20.dp))


            //PODACI--------------------------------------------------------------------------


            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = WarmWhite
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    // TEAM i TYPE
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.team),
                                style = MaterialTheme.typography.labelMedium,
                                color = SoftGray
                            )
                            Text(
                                text = sticker.reprezentacija,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = DarkText
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "Sticker type",
                                style = MaterialTheme.typography.labelMedium,
                                color = SoftGray
                            )
                            Text(
                                text = tipZaPrikaz,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = DeepGreen
                            )
                        }
                    }


                    Spacer(modifier = Modifier.height(18.dp))

                    // JERSEY i POSITION
                    if (!jeGrb) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = stringResource(R.string.jersey),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = SoftGray
                                )

                                Text(
                                    text = "#${sticker.broj_dresa}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DeepGreen
                                )
                            }


                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.End
                            ) {

                                Text(
                                    text = stringResource(R.string.position),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = SoftGray
                                )

                                Text(
                                    text = sticker.pozicija,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepGreen
                                )
                            }
                        }
                    }
                }
            }


            Spacer(modifier = Modifier.height(18.dp))


            // FAVORITE I SHARE--------------------------------------------

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Button(
                    onClick = {
                        onFavoriteClick(sticker.id)
                    },

                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = DeepGreen,
                        contentColor = Color.White
                    )
                ) {

                    Text(
                        text =
                            if (sticker.favorite) {
                                "♥ " + stringResource(R.string.remove_favorite)
                            } else {
                                "♡ " + stringResource(R.string.add_favorite)
                            },

                        fontWeight = FontWeight.Bold
                    )
                }


                OutlinedButton(
                    onClick = {

                        val poruka =
                            if (jeGrb) {
                                "Crest: ${sticker.reprezentacija}\n" +
                                        "Type: $tipZaPrikaz"

                            } else {

                                "${sticker.ime} ${sticker.prezime}\n" +
                                        "Team: ${sticker.reprezentacija}\n" +
                                        "Jersey number: ${sticker.broj_dresa}\n" +
                                        "Position: ${sticker.pozicija}\n" +
                                        "Type: $tipZaPrikaz"
                            }


                        val intent = Intent(Intent.ACTION_SEND)

                        intent.type = "text/plain"

                        intent.putExtra(
                            Intent.EXTRA_TEXT,
                            poruka
                        )


                        context.startActivity(
                            Intent.createChooser(
                                intent,
                                "Share sticker"
                            )
                        )
                    },

                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50.dp),
                    border = BorderStroke(1.dp, Gold),

                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = WarmWhite,
                        contentColor = DeepGreen
                    )
                ) {

                    Text(
                        text = stringResource(R.string.share),
                        fontWeight = FontWeight.Bold
                    )
                }
            }


            Spacer(modifier = Modifier.height(20.dp))
        }


    } else {

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Cream),

            contentAlignment = Alignment.Center
        ) {

            Text(
                text = stringResource(R.string.player_not_found),
                style = MaterialTheme.typography.titleLarge,
                color = DeepGreen
            )
        }
    }
}