package com.example.stickeralbum.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore by preferencesDataStore(
    name = "game_preferences"
)

class CoinsRepository(
    private val context: Context
) {

    private val coinsKey = intPreferencesKey("coins")

    val coins: Flow<Int> =
        context.dataStore.data.map { preferences ->
            preferences[coinsKey] ?: 10
        }


    suspend fun addCoins(
        amount: Int
    ) {

        context.dataStore.edit { preferences ->

            val trenutniCoins = preferences[coinsKey] ?: 10

            preferences[coinsKey] = trenutniCoins + amount
        }
    }


    suspend fun spendCoins(
        amount: Int
    ) {

        context.dataStore.edit { preferences ->
            val trenutniCoins = preferences[coinsKey] ?: 10

            preferences[coinsKey] = trenutniCoins - amount
        }
    }
}