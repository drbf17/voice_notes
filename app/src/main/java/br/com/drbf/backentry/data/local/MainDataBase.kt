package br.com.drbf.backentry.data.local

import android.content.Context
import androidx.room.*
import br.com.drbf.backentry.data.local.converters.Converters
import br.com.drbf.backentry.data.notes.Note
import br.com.drbf.backentry.data.notes.NotesDao
import java.util.*


@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = true,

    )

@TypeConverters(Converters::class)
abstract class MainDatabase : RoomDatabase() {

    abstract fun getNoteDao(): NotesDao

    companion object {

        private const val TAG = "MainDataBase"

        private var INSTANCE: MainDatabase? = null

        fun getInstance(context: Context): MainDatabase {
            return INSTANCE ?: synchronized(this) {


                val instance = Room.databaseBuilder(
                    context,
                    MainDatabase::class.java,
                    "main_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}