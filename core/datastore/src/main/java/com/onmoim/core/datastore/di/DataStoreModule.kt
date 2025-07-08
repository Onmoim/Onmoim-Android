package com.onmoim.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.onmoim.core.datastore.DataStorePreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "OnmoimPreferences")

    @Provides
    fun provideDataStorePreferences(
        @ApplicationContext context: Context
    ): DataStorePreferences = DataStorePreferences(context.dataStore)
}