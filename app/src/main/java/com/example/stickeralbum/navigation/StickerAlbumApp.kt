package com.example.stickeralbum.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.stickeralbum.R
import com.example.stickeralbum.ui.AlbumScreen
import com.example.stickeralbum.ui.AlbumViewModel
import com.example.stickeralbum.ui.FavoritesScreen
import com.example.stickeralbum.ui.PackScreen
import com.example.stickeralbum.ui.QuizScreen
import com.example.stickeralbum.ui.SplashScreen
import com.example.stickeralbum.ui.StatisticsScreen
import com.example.stickeralbum.ui.StickerDetailScreen
import com.example.stickeralbum.ui.theme.DeepGreen
import com.example.stickeralbum.ui.theme.Gold
import com.example.stickeralbum.ui.theme.LightGold
import com.example.stickeralbum.ui.theme.SoftGray
import com.example.stickeralbum.ui.theme.WarmWhite
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

enum class StickerAlbumScreen {
    Splash, Album, Details, Favorites, Pack, Quiz, Statistics
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StickerAlbumAppBar(
    trenutniEkran: StickerAlbumScreen,
    mozeNazad: Boolean,
    navigateUp: () -> Unit,   //=Daj mi radnju koju trebam izvrsiti kad korisnik klikne back
    onFavoritesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val naslov = when (trenutniEkran) {
        StickerAlbumScreen.Album -> stringResource(R.string.album_title)
        StickerAlbumScreen.Details -> stringResource(R.string.details_title)
        StickerAlbumScreen.Favorites -> stringResource(R.string.favorites_title)
        StickerAlbumScreen.Pack -> stringResource(R.string.pack_title)
        StickerAlbumScreen.Statistics -> stringResource(R.string.statistics_title)
        StickerAlbumScreen.Splash -> ""
        StickerAlbumScreen.Quiz -> "Quiz"
    }

    TopAppBar(
        title = {
            Text(
                text = naslov,
                fontWeight = FontWeight.Bold
            )
        },
        modifier = modifier,
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = DeepGreen,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White,
                actionIconContentColor = Gold
            ),
        navigationIcon = {
            if (mozeNazad) {
                IconButton(
                    onClick = {
                        navigateUp()
                    }
                ) {
                    Icon(
                        imageVector =
                            Icons.AutoMirrored
                                .Filled
                                .ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        },

        actions = {
            if (trenutniEkran == StickerAlbumScreen.Album) {
                IconButton(
                    onClick = {
                        onFavoritesClick()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = stringResource(R.string.favorites),
                        tint = Gold
                    )
                }
            }
        }
    )
}



@Composable
fun StickerAlbumApp(
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    val albumViewModel: AlbumViewModel = viewModel(
        factory = AlbumViewModel.Factory
    )

    val uiState by albumViewModel.uiState.collectAsStateWithLifecycle()

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val trenutnaRuta = backStackEntry?.destination?.route
    val detailsRoute = "${StickerAlbumScreen.Details.name}/{stickerId}"

    val trenutniEkran: StickerAlbumScreen = when (trenutnaRuta) {
        StickerAlbumScreen.Splash.name -> StickerAlbumScreen.Splash
        detailsRoute -> StickerAlbumScreen.Details
        StickerAlbumScreen.Pack.name -> StickerAlbumScreen.Pack
        StickerAlbumScreen.Favorites.name -> StickerAlbumScreen.Favorites
        StickerAlbumScreen.Statistics.name -> StickerAlbumScreen.Statistics
        StickerAlbumScreen.Quiz.name -> StickerAlbumScreen.Quiz
        else -> StickerAlbumScreen.Album
    }

    val mozeNazad: Boolean
    if (navController.previousBackStackEntry != null) {
        mozeNazad = true
    } else {
        mozeNazad = false
    }


    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            if (trenutniEkran != StickerAlbumScreen.Splash) {
                StickerAlbumAppBar(
                    trenutniEkran = trenutniEkran,
                    mozeNazad = mozeNazad,
                    navigateUp = {
                        navController.navigateUp()
                    },
                    onFavoritesClick = {
                        navController.navigate(StickerAlbumScreen.Favorites.name)
                    }
                )
            }
        },

        bottomBar = {
            if (
                trenutniEkran == StickerAlbumScreen.Album ||
                trenutniEkran == StickerAlbumScreen.Pack ||
                trenutniEkran == StickerAlbumScreen.Statistics
            ) {
                NavigationBar(
                    containerColor = WarmWhite,
                    tonalElevation = 6.dp
                ) {
                    NavigationBarItem(
                        selected = trenutniEkran == StickerAlbumScreen.Album,
                        onClick = {
                            if (trenutniEkran != StickerAlbumScreen.Album) {
                                navController.navigate(StickerAlbumScreen.Album.name) {
                                    popUpTo(StickerAlbumScreen.Album.name)
                                    launchSingleTop = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = stringResource(R.string.album)
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(R.string.album)
                            )
                        },
                        colors =
                            NavigationBarItemDefaults.colors(
                                selectedIconColor = DeepGreen,
                                selectedTextColor = DeepGreen,
                                indicatorColor = LightGold,
                                unselectedIconColor = SoftGray,
                                unselectedTextColor = SoftGray
                            )
                    )


                    NavigationBarItem(
                        selected =
                            trenutniEkran == StickerAlbumScreen.Pack,
                        onClick = {
                            if (trenutniEkran != StickerAlbumScreen.Pack) {
                                albumViewModel.ocistiPaket()
                                navController.navigate(StickerAlbumScreen.Pack.name) {
                                    popUpTo(StickerAlbumScreen.Album.name)
                                    launchSingleTop = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.AddCircle,
                                contentDescription = stringResource(R.string.pack)
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(R.string.pack)
                            )
                        },
                        colors =
                            NavigationBarItemDefaults.colors(
                                selectedIconColor = DeepGreen,
                                selectedTextColor = DeepGreen,
                                indicatorColor = LightGold,
                                unselectedIconColor = SoftGray,
                                unselectedTextColor = SoftGray
                            )
                    )

                    NavigationBarItem(
                        selected =
                            trenutniEkran == StickerAlbumScreen.Statistics,
                        onClick = {
                            if (trenutniEkran != StickerAlbumScreen.Statistics) {
                                navController.navigate(StickerAlbumScreen.Statistics.name) {
                                    popUpTo(StickerAlbumScreen.Album.name)
                                    launchSingleTop = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = stringResource(R.string.statistics)
                            )
                        },

                        label = {
                            Text(
                                text = stringResource(R.string.statistics)
                            )
                        },
                        colors =
                            NavigationBarItemDefaults.colors(
                                selectedIconColor = DeepGreen,
                                selectedTextColor = DeepGreen,
                                indicatorColor = LightGold,
                                unselectedIconColor = SoftGray,
                                unselectedTextColor = SoftGray
                            )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = StickerAlbumScreen.Splash.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(
                route = StickerAlbumScreen.Album.name
            ) {
                AlbumScreen(
                    onStickerClick = { stickerId ->
                        navController.navigate(
                            "${StickerAlbumScreen.Details.name}/$stickerId"
                        )
                    },
                    albumViewModel = albumViewModel,
                    windowSize = windowSize
                )
            }

            composable(
                route = "${StickerAlbumScreen.Details.name}/{stickerId}",
                arguments = listOf(
                    navArgument("stickerId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val stickerId =
                    backStackEntry.arguments?.getInt("stickerId")

                if (stickerId != null) {
                    val odabranaSlicica =
                        uiState.sveSlicice.find { sticker ->
                            sticker.id == stickerId
                        }

                    StickerDetailScreen(
                        sticker = odabranaSlicica,
                        onFavoriteClick = { stickerId ->
                            albumViewModel.promijeniFavorite(
                                stickerId
                            )
                        }
                    )
                }
            }


            composable(
                route = StickerAlbumScreen.Pack.name
            ) {
                PackScreen(
                    uiState = uiState,
                    onOpenPack = {
                        albumViewModel.otvoriPaket()
                    },
                    onOpenQuiz = {
                        navController.navigate(
                            StickerAlbumScreen.Quiz.name
                        )
                    },
                    onDismissPackError = {
                        albumViewModel.ocistiPaketGresku()
                    }
                )
            }

            composable(
                route = StickerAlbumScreen.Favorites.name
            ) {
                FavoritesScreen(
                    stickers = uiState.sveSlicice,
                    onStickerClick = { stickerId ->
                        navController.navigate(
                            "${StickerAlbumScreen.Details.name}/$stickerId"
                        )
                    }
                )
            }

            composable(
                route = StickerAlbumScreen.Statistics.name
            ) {
                StatisticsScreen(
                    uiState = uiState
                )
            }


            composable(
                route = StickerAlbumScreen.Splash.name
            ) {
                LaunchedEffect(Unit) {
                    delay(1500.milliseconds)
                    navController.navigate(
                        StickerAlbumScreen.Album.name
                    ) {
                        popUpTo(StickerAlbumScreen.Splash.name) {
                            inclusive = true
                        }
                    }
                }
                SplashScreen()
            }


            composable(
                route =
                    StickerAlbumScreen.Quiz.name
            ) {
                QuizScreen(
                    coins = uiState.coins,

                    onCorrectAnswer = {
                        albumViewModel.dodajQuizNagradu()
                    },

                    onBackToPack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }

}

