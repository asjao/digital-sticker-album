package com.example.stickeralbum.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.stickeralbum.R
import com.example.stickeralbum.model.Sticker
import com.example.stickeralbum.ui.theme.DarkText
import com.example.stickeralbum.ui.theme.DeepGreen
import com.example.stickeralbum.ui.theme.Gold
import com.example.stickeralbum.ui.theme.LightGold
import com.example.stickeralbum.ui.theme.SoftGray
import com.example.stickeralbum.ui.theme.WarmWhite

@Composable
fun StickerCard(
    sticker: Sticker,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isGrid: Boolean = false
) {
    val jeGrb = sticker.tip_slicice == "grb"
    val imageUrl = "http://49.13.125.189:3300" + sticker.slicica_lokacija
    val imageHeight = if (isGrid) 155.dp else 230.dp

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = WarmWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(
            width = if (sticker.collected) 2.dp else 1.dp,
            color = if (sticker.collected) Gold
            else MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Column {

            //SLIKA
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.40f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                val imageColorFilter = if (!sticker.collected) {
                    val colorMatrix = ColorMatrix().apply {
                        setToSaturation(0f)
                    }
                    ColorFilter.colorMatrix(colorMatrix)
                } else {
                    null
                }

                AsyncImage(
                    model = imageUrl,
                    contentDescription = if (jeGrb) {
                        "Grb ${sticker.reprezentacija}"
                    } else {
                        "${sticker.ime} ${sticker.prezime}"
                    },
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                    colorFilter = imageColorFilter
                )

                //GOLD BADGE
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

            //--------------------PODACI O SLICICI--------------------------------------------------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (sticker.zlatna && !jeGrb)
                            LightGold
                        else
                            WarmWhite
                    )
                    .padding(if (isGrid) 10.dp else 16.dp)
            ) {
                Surface(
                    color = if (sticker.collected) DeepGreen else LightGold,
                    contentColor = if (sticker.collected) Color.White else DarkText,
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text(
                        text = if (sticker.collected) {
                            stringResource(R.string.collected)
                        } else {
                            stringResource(R.string.missing)
                        },
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(9.dp))

                //IME IGRACA / GRB
                Text(
                    text = if (jeGrb) {
                        "Grb ${sticker.reprezentacija}"
                    } else {
                        "${sticker.ime} ${sticker.prezime}"
                    },
                    style = if (isGrid) {
                        MaterialTheme.typography.titleMedium
                    } else {
                        MaterialTheme.typography.titleLarge
                    },
                    fontWeight = FontWeight.ExtraBold,
                    color = DarkText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                //REPREZENTACIJA
                Text(
                    text = sticker.reprezentacija,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SoftGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(7.dp))

                //BROJ DRESA I POZICIJA
                if (!jeGrb) {
                    Text(
                        text = stringResource(
                            R.string.jersey_short,
                            sticker.broj_dresa
                        ) + "  |  " + sticker.pozicija,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = DeepGreen,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}





@Composable
fun AlbumScreen(
    onStickerClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    albumViewModel: AlbumViewModel = viewModel(factory = AlbumViewModel.Factory),
    windowSize: WindowWidthSizeClass,
) {
    val uiState by albumViewModel.uiState.collectAsStateWithLifecycle()

    val brojKolona: Int
    when (windowSize) {
        WindowWidthSizeClass.Compact -> brojKolona = 2
        WindowWidthSizeClass.Medium -> brojKolona = 3
        WindowWidthSizeClass.Expanded -> brojKolona = 4
        else -> brojKolona = 2
    }

    /*
    var searchText by remember {
        mutableStateOf("")
    }
    */

    var teamMenuExpanded by remember { mutableStateOf(false) } //da li je dropdown menu otvoren
    var meniSortiranjaOtvoren by remember { mutableStateOf(false) }
    var meniPrikazaOtvoren by remember { mutableStateOf(false) }

    val teamNames = uiState.sveSlicice.map { sticker -> sticker.reprezentacija }
    val uniqueTeams = teamNames.distinct()
    val teams = listOf("All") + uniqueTeams

    Box {
        Image(
            painter = painterResource(id = R.drawable.album_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            if (uiState.ucitavanje) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = stringResource(R.string.loading_stickers))
                }
            }

            if (uiState.porukaGreske.isNotEmpty()) {
                Text(
                    text = uiState.porukaGreske,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            OutlinedTextField(
                value = uiState.searchText,
                onValueChange = { noviTekst ->
                    albumViewModel.promijeniPretragu(noviTekst)
                },
                label = {
                    Text(text = stringResource(R.string.search_player))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = WarmWhite,
                    unfocusedContainerColor = WarmWhite,
                    focusedBorderColor = DeepGreen,
                    unfocusedBorderColor = Gold,
                    focusedLabelColor = DeepGreen,
                    focusedLeadingIconColor = DeepGreen,
                    unfocusedLeadingIconColor = SoftGray,
                    cursorColor = DeepGreen
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 12.dp)
            )

            //BIRANJE TIMA I SORTIRANJE---------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedButton(
                        onClick = { teamMenuExpanded = true },
                        shape = RoundedCornerShape(50.dp),
                        border = BorderStroke(2.dp, WarmWhite),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = DeepGreen,
                            contentColor = WarmWhite
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(
                                R.string.filter_team,
                                uiState.selectedTeam
                            ),
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    DropdownMenu(
                        expanded = teamMenuExpanded,
                        onDismissRequest = { teamMenuExpanded = false }
                    ) {
                        teams.forEach { team ->
                            DropdownMenuItem(
                                text = { Text(team) },
                                onClick = {
                                    albumViewModel.promijeniReprezentaciju(team)
                                    teamMenuExpanded = false
                                }
                            )
                        }
                    }
                }


                val tekstSortiranja: String
                if (uiState.odabranoSortiranje == "Jersey number ↑")  {
                    tekstSortiranja = stringResource(R.string.sort_jersey_up)
                } else {
                    tekstSortiranja = stringResource(R.string.sort_jersey_down)
                }

                Box(modifier = Modifier.weight(1f)) {
                    OutlinedButton(
                        onClick = { meniSortiranjaOtvoren = true },
                        shape = RoundedCornerShape(50.dp),
                        border = BorderStroke(2.dp, Gold),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = LightGold,
                            contentColor = DeepGreen
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = tekstSortiranja,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    DropdownMenu(
                        expanded = meniSortiranjaOtvoren,
                        onDismissRequest = { meniSortiranjaOtvoren = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(stringResource(R.string.sort_jersey_up))
                            },
                            onClick = {
                                albumViewModel.promijeniSortiranje("Jersey number ↑")
                                meniSortiranjaOtvoren = false
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                Text(
                                    stringResource(R.string.sort_jersey_down)
                                )
                            },
                            onClick = {
                                albumViewModel.promijeniSortiranje("Jersey number ↓")
                                meniSortiranjaOtvoren = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(
                        R.string.results_count,
                        uiState.prikazaneSlicice.size
                    ),
                    style = MaterialTheme.typography.labelLarge,
                    color = DeepGreen,
                    fontWeight = FontWeight.Bold
                )

                Box {
                    OutlinedButton(
                        onClick = { meniPrikazaOtvoren = true },
                        shape = RoundedCornerShape(50.dp),
                        border = BorderStroke(1.dp, Gold),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = WarmWhite,
                            contentColor = DeepGreen
                        )
                    ) {
                        Text(
                            text = stringResource(
                                R.string.view_value,
                                uiState.nacinPrikaza
                            ),
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    DropdownMenu(
                        expanded = meniPrikazaOtvoren,
                        onDismissRequest = { meniPrikazaOtvoren = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(stringResource(R.string.view_list))
                            },
                            onClick = {
                                albumViewModel.promijeniNacinPrikaza("List")
                                meniPrikazaOtvoren = false
                            }
                        )


                        DropdownMenuItem(
                            text = {
                                Text(stringResource(R.string.view_grid))
                            },
                            onClick = {
                                albumViewModel.promijeniNacinPrikaza("Grid")
                                meniPrikazaOtvoren = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            //-------------------PRIKAZ----------------------------------------------------------
            if (uiState.nacinPrikaza == "List") {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(top = 4.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.prikazaneSlicice) { sticker ->
                        StickerCard(
                            sticker = sticker,
                            onClick = { onStickerClick(sticker.id) },
                            isGrid = false
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(brojKolona),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(top = 4.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(uiState.prikazaneSlicice) { sticker ->
                        StickerCard(
                            sticker = sticker,
                            onClick = { onStickerClick(sticker.id) },
                            isGrid = true
                        )
                    }
                }
            }
        }
    }

}