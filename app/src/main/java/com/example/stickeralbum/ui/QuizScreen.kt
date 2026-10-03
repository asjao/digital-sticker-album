package com.example.stickeralbum.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.stickeralbum.R
import com.example.stickeralbum.data.getRandomQuizQuestions
import com.example.stickeralbum.ui.theme.*


@Composable
fun QuizScreen(
    coins: Int,
    onCorrectAnswer: () -> Unit,
    onBackToPack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pitanja = remember { getRandomQuizQuestions(5) }

    var trenutnoPitanjeIndex by remember { mutableIntStateOf(0) }
    var odabraniOdgovorIndex by remember { mutableStateOf<Int?>(null) }
    var odgovoreno by remember { mutableStateOf(false) }
    var brojTacnih by remember { mutableIntStateOf(0) }
    var osvojeniCoins by remember { mutableIntStateOf(0) }
    var kvizZavrsen by remember { mutableStateOf(false) }


    if (!kvizZavrsen) {

        val pitanje = pitanja[trenutnoPitanjeIndex]

        Box(
            modifier = modifier.fillMaxSize()
        ) {

            Image(
                painter = painterResource(R.drawable.quiz_background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {

                //Naslov i coins--------------------------------------------
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column {

                        Text(
                            text = "Football Quiz",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = DeepGreen
                        )

                        Text(
                            text = "Test your football knowledge",
                            style = MaterialTheme.typography.bodySmall,
                            color = SoftGray
                        )
                    }


                    Surface(
                        color = WarmWhite,
                        shape = RoundedCornerShape(50.dp),
                        border = BorderStroke(1.dp, Gold)
                    ) {

                        Row(
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 7.dp
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Image(
                                painter = painterResource(R.drawable.coin_icon),
                                contentDescription = null,
                                modifier = Modifier.size(23.dp)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = "$coins",
                                fontWeight = FontWeight.Bold,
                                color = DeepGreen
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                //Broj trenutnog pitanja
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Question ${trenutnoPitanjeIndex + 1} of ${pitanja.size}",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = DeepGreen
                    )

                    Text(
                        text = "$brojTacnih correct",
                        style = MaterialTheme.typography.labelMedium,
                        color = SoftGray
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                LinearProgressIndicator(
                    progress = {
                        (trenutnoPitanjeIndex + 1).toFloat() /
                                pitanja.size.toFloat()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(7.dp),
                    color = DeepGreen,
                    trackColor = LightGold
                )


                Spacer(modifier = Modifier.height(10.dp))


                //pitanje i odgovori koriste sav preostali prostor
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(7.dp)
                ) {

                    //Pitanje
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(88.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = WarmWhite
                        ),
                        elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = 3.dp
                        )
                    ) {

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 18.dp),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = pitanje.question,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = DarkText,
                                textAlign = TextAlign.Center
                            )
                        }
                    }


                    //cetiri odgovora
                    pitanje.answers.forEachIndexed { index, odgovor ->

                        val tacanOdgovor =
                            index == pitanje.correctAnswerIndex

                        val odabraniOdgovor =
                            index == odabraniOdgovorIndex


                        val bojaKartice =
                            if (odgovoreno && tacanOdgovor) {
                                DeepGreen
                            } else if (
                                odgovoreno &&
                                odabraniOdgovor &&
                                !tacanOdgovor
                            ) {
                                MaterialTheme.colorScheme.errorContainer
                            } else {
                                WarmWhite
                            }


                        val bojaTeksta =
                            if (odgovoreno && tacanOdgovor) {
                                Color.White
                            } else {
                                DarkText
                            }


                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .clickable(enabled = !odgovoreno) {

                                    odabraniOdgovorIndex = index
                                    odgovoreno = true

                                    if (tacanOdgovor) {
                                        brojTacnih++
                                        osvojeniCoins += 5
                                        onCorrectAnswer()
                                    }
                                },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = bojaKartice
                            ),
                            elevation = CardDefaults.elevatedCardElevation(
                                defaultElevation = 2.dp
                            )
                        ) {

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {

                                Text(
                                    text = odgovor,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = bojaTeksta
                                )
                            }
                        }
                    }
                }


                Spacer(modifier = Modifier.height(8.dp))



                if (odgovoreno) {

                    val odgovorJeTacan =
                        odabraniOdgovorIndex ==
                                pitanje.correctAnswerIndex


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        //tacno/ntacno---------------------------------------------------
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),

                            color =
                                if (odgovorJeTacan) {
                                    LightGold
                                } else {
                                    MaterialTheme.colorScheme.errorContainer
                                },

                            shape = RoundedCornerShape(16.dp),

                            border = BorderStroke(
                                1.dp,
                                if (odgovorJeTacan) {
                                    Gold
                                } else {
                                    MaterialTheme.colorScheme.error
                                }
                            )
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 8.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    text =
                                        if (odgovorJeTacan) {
                                            "✓ Correct"
                                        } else {
                                            "x Wrong"
                                        },
                                    fontWeight = FontWeight.Bold,
                                    color =
                                        if (odgovorJeTacan) {
                                            DeepGreen
                                        } else {
                                            MaterialTheme.colorScheme.error
                                        }
                                )


                                if (odgovorJeTacan) {

                                    Spacer(modifier = Modifier.width(5.dp))

                                    Image(
                                        painter = painterResource(
                                            R.drawable.coin_icon
                                        ),
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )

                                    Spacer(modifier = Modifier.width(2.dp))

                                    Text(
                                        text = "+5",
                                        fontWeight = FontWeight.Bold,
                                        color = Gold
                                    )
                                }
                            }
                        }


                        // Next/Finish-----------------------------------------------------
                        Button(
                            onClick = {

                                if (trenutnoPitanjeIndex < pitanja.lastIndex) {

                                    trenutnoPitanjeIndex++
                                    odabraniOdgovorIndex = null
                                    odgovoreno = false

                                } else {

                                    kvizZavrsen = true
                                }
                            },

                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),

                            shape = RoundedCornerShape(16.dp),

                            colors = ButtonDefaults.buttonColors(
                                containerColor = DeepGreen,
                                contentColor = Color.White
                            )
                        ) {


                            Text(
                                text =
                                    if (
                                        trenutnoPitanjeIndex <
                                        pitanja.lastIndex
                                    ) {
                                        "Next"
                                    } else {
                                        "Finish"
                                    },
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }


                } else {

                    //cuvamo prostor za donji red da se ekran ne pomjera
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    )
                }
            }
        }


    } else {

        //kraj kviza-------------------------------------------------------------------
        Box(
            modifier = modifier.fillMaxSize()
        ) {

            Image(
                painter = painterResource(R.drawable.quiz_background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {

                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),

                    colors = CardDefaults.elevatedCardColors(
                        containerColor = WarmWhite
                    ),

                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 5.dp
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Image(
                            painter = painterResource(
                                R.drawable.trophy
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(95.dp)
                        )


                        Spacer(modifier = Modifier.height(12.dp))


                        Text(
                            text = "Quiz complete!",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = DeepGreen
                        )


                        Spacer(modifier = Modifier.height(8.dp))


                        Text(
                            text = "$brojTacnih / ${pitanja.size} correct",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )


                        Spacer(modifier = Modifier.height(22.dp))


                        Text(
                            text = "You earned",
                            style = MaterialTheme.typography.bodyLarge,
                            color = SoftGray
                        )


                        Spacer(modifier = Modifier.height(6.dp))


                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Image(
                                painter = painterResource(
                                    R.drawable.coin_icon
                                ),
                                contentDescription = null,
                                modifier = Modifier.size(34.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "$osvojeniCoins coins",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = Gold
                            )
                        }


                        Spacer(modifier = Modifier.height(24.dp))


                        Button(
                            onClick = onBackToPack,

                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),

                            shape = RoundedCornerShape(50.dp),

                            colors = ButtonDefaults.buttonColors(
                                containerColor = DeepGreen,
                                contentColor = Color.White
                            )
                        ) {

                            Text(
                                text = "Back to Packs",
                                fontWeight = FontWeight.Bold
                            )
                        }

                    }
                }
            }
        }
    }
}