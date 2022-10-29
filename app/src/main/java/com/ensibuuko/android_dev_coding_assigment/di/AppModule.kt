package com.ensibuuko.android_dev_coding_assigment.di

import android.app.Application
import androidx.room.Room
import com.ensibuuko.android_dev_coding_assigment.api.Api
import com.ensibuuko.android_dev_coding_assigment.data.EnsibukoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit =
        Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit) : Api =
        retrofit.create(Api::class.java)

    @Provides
    @Singleton
    fun provideDatabase(app : Application) : EnsibukoDatabase =
        Room.databaseBuilder(app, EnsibukoDatabase::class.java, "ensibuko_database")
            .build()
}