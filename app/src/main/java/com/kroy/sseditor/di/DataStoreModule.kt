package com.kroy.sseditor.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

// Extension function to create DataStore instance
val Context.dataStore by preferencesDataStore(name = "user_prefs")

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context) = context.dataStore



}
