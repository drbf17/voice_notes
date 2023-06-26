package br.com.drbf.backentry.domain

import androidx.paging.PagingData
import br.com.drbf.backentry.data.notes.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    fun getName(): String

    suspend fun insert(note: Note)

    suspend fun delete(note: Note)

    fun getNotes(): Flow<PagingData<Note>>

    fun getCount(): Flow<Int>
}