package br.com.drbf.backentry.data.local.di

import android.content.Context
import br.com.drbf.backentry.data.local.MainDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {

    @Singleton
    @Provides
    fun provideOkMainDataBase(@ApplicationContext appContext: Context): MainDatabase {
        return MainDatabase.getInstance(appContext)
    }

}