package com.kroy.sseditor.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.kroy.sseditor.data.remote.ApiService
import com.kroy.sseditor.data.repo.ClientRepoImp
import com.kroy.sseditor.data.repo.ContactRepoImp
import com.kroy.sseditor.data.repo.SettingRepoImp
import com.kroy.sseditor.domain.repo.ClientRepo
import com.kroy.sseditor.domain.repo.ContactRepo
import com.kroy.sseditor.domain.repo.SettingsRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideSettingsRepo(
        dataStorePref: DataStore<Preferences>
    ): SettingsRepo {
        return SettingRepoImp(dataStorePref)
    }

    @Provides
    @Singleton
    fun provideClientRepo(
        apiService: ApiService
    ) : ClientRepo {
        return ClientRepoImp(apiService)
    }


    @Provides
    @Singleton
    fun provideContactRepo(
        apiService: ApiService
    ): ContactRepo {
        return ContactRepoImp(apiService)
    }

}






























