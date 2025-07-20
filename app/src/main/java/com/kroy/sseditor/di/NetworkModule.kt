package com.kroy.sseditor.di

import com.kroy.sseditor.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient {
        // Create a logging interceptor
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Logs request and response body
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Add the logging interceptor
            .connectTimeout(90, TimeUnit.SECONDS) // Connection timeout
            .readTimeout(90, TimeUnit.SECONDS)   // Read timeout
            .writeTimeout(90, TimeUnit.SECONDS)  // Write timeout
            .build()
    }

    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://13.202.225.156:4000/api/") // Replace with your API base URL
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun providesApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
