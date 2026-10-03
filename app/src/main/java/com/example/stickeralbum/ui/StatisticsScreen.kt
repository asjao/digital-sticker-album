package com.example.stickeralbum.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.stickeralbum.R
import com.example.stickeralbum.ui.theme.DarkText
import com.example.stickeralbum.ui.theme.DeepGreen
import com.example.stickeralbum.ui.theme.Gold
import com.example.stickeralbum.ui.theme.LightGold
import com.example.stickeralbum.ui.theme.SoftGray
import com.example.stickeralbum.ui.theme.WarmWhite


@Composable
fun StatisticsScreen(
    uiState: AlbumUiState,
    modifier: Modifier = Modifier
) {
    // SVE SLICICE
    val ukupnoSlicica = uiState.sveSlicice.size

    // SKUPlJENE SLICICE
    val skupljene = uiState.sveSlicice.filter { it.collected }
    val brojSkupljenih = skupljene.size
    val brojNedostajucih = ukupnoSlicica - brojSkupljenih

    val procenat = if (ukupnoSlicica > 0) brojSkupljenih * 100 / ukupnoSlicica else 0

    // SKUPlJENO PREMA TIPU
    val obicne = skupljene.count { it.tip_slicice == "obicna" && !it.zlatna }
    val zlatne = skupljene.count { it.zlatna }
    val grbovi = skupljene.count { it.tip_slicice == "grb" }

    Box {
        Image(
            painter = painterResource(id = R.drawable.quiz_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.collection_statistics),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = DeepGreen
            )

            Spacer(modifier = Modifier.height(20.dp))




            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatisticCard(
                    number = brojSkupljenih,
                    label = stringResource(R.string.collected),
                    accentColor = DeepGreen,
                    modifier = Modifier.weight(1f)
                )
                StatisticCard(
                    number = brojNedostajucih,
                    label = stringResource(R.string.missing),
                    accentColor = Gold,
                    modifier = Modifier.weight(1f)
                )
                StatisticCard(
                    number = ukupnoSlicica,
                    label = stringResource(R.string.total_stickers),
                    accentColor = DarkText,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            //NAPREDAK ALBUMA
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = WarmWhite)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "${stringResource(R.string.album_completed)}: $procenat%",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepGreen
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LinearProgressIndicator(
                        progress = {
                            if (ukupnoSlicica > 0) {
                                brojSkupljenih.toFloat() / ukupnoSlicica.toFloat()
                            } else {
                                0f
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(12.dp),
                        color = DeepGreen,
                        trackColor = LightGold.copy(alpha = 0.40f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            //SKUPlJENO PREMA TIPU
            Text(
                text = stringResource(R.string.collected_by_type),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = DeepGreen
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatisticCard(
                    number = obicne,
                    label = stringResource(R.string.regular),
                    accentColor = DeepGreen,
                    modifier = Modifier.weight(1f)
                )
                StatisticCard(
                    number = zlatne,
                    label = stringResource(R.string.gold),
                    accentColor = Gold,
                    modifier = Modifier.weight(1f)
                )
                StatisticCard(
                    number = grbovi,
                    label = stringResource(R.string.crests),
                    accentColor = SoftGray,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = WarmWhite)
            ) {
                TypeChart(
                    regular = obicne,
                    gold = zlatne,
                    crests = grbovi,
                    modifier = Modifier.padding(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}




@Composable
fun TypeChart(
    regular: Int,
    gold: Int,
    crests: Int,
    modifier: Modifier = Modifier
) {
    val total = regular + gold + crests

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.collected_distribution),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )

        Spacer(modifier = Modifier.height(14.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
        ) {
            if (total > 0) {
                val regularWidth = size.width * regular.toFloat() / total.toFloat()
                val goldWidth = size.width * gold.toFloat() / total.toFloat()
                val crestsWidth = size.width * crests.toFloat() / total.toFloat()

                // OBICNE
                drawRect(
                    color = DeepGreen,
                    topLeft = Offset(x = 0f, y = 0f),
                    size = Size(width = regularWidth, height = size.height)
                )

                // ZLATNE
                drawRect(
                    color = Gold,
                    topLeft = Offset(x = regularWidth, y = 0f),
                    size = Size(width = goldWidth, height = size.height)
                )

                //GRBOVI
                drawRect(
                    color = SoftGray,
                    topLeft = Offset(x = regularWidth + goldWidth, y = 0f),
                    size = Size(width = crestsWidth, height = size.height)
                )
            } else {
                drawRect(color = LightGold.copy(alpha = 0.30f))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${stringResource(R.string.regular)}: $regular",
                color = DeepGreen,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = "${stringResource(R.string.gold)}: $gold",
                color = Gold,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = "${stringResource(R.string.crests)}: $crests",
                color = SoftGray,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}


@Composable
fun StatisticCard(
    number: Int,
    label: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,

        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = WarmWhite),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            Text(
                text = number.toString(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = accentColor
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = DarkText
            )
        }

    }
}
