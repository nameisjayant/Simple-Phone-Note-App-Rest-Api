package com.codingwithjks.phonenoteapp.di

import com.codingwithjks.phonenoteapp.MainActivity
import com.codingwithjks.phonenoteapp.data.network.ApiService
import com.codingwithjks.phonenoteapp.data.util.Listener
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesMoshi(): Moshi = Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun providesApiService(moshi: Moshi): ApiService = Retrofit
        .Builder()
        .run {
            baseUrl(ApiService.BASE_URL)
            addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
        }.create(ApiService::class.java)

}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModuleTwo {

    @Binds
   abstract fun providesOnClickDelete(
        mainActivity: MainActivity
    ):Listener

}