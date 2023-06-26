package br.com.drbf.backentry.data.di

import br.com.drbf.backentry.data.notes.DefaultNotesRepository
import br.com.drbf.backentry.domain.NotesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsNotesRepository(
        repository: DefaultNotesRepository
    ): NotesRepository
}