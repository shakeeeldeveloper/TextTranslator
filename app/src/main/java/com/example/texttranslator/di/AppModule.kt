package com.example.texttranslator.di

import android.content.Context
import com.example.texttranslator.repositories.TranslationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTranslationRepository(
        @ApplicationContext context: Context,

    ): TranslationRepository = TranslationRepository(context)
}

