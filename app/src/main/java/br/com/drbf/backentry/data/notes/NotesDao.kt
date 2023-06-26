package br.com.drbf.backentry.data.notes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface NotesDao {

    @Query("SELECT COUNT(date) FROM notes")
    fun getCount(): Flow<Int>

    @Query("SELECT * FROM notes ORDER BY date DESC LIMIT :limit OFFSET :offset ")
    suspend fun getNotesPage(limit: Int, offset: Int): List<Note>

    @Delete
    suspend fun delete(notification: Note)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: Note)

}