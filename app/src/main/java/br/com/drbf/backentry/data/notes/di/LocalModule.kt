package br.com.drbf.backentry.data.notes.di

import android.content.Context
import br.com.drbf.backentry.data.local.MainDatabase
import br.com.drbf.backentry.data.notes.NotesDao
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
    fun provideNotesDao(mainDatabase: MainDatabase): NotesDao{
        return mainDatabase.getNoteDao()
    }



}