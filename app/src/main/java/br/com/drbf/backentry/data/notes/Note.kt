package br.com.drbf.backentry.data.notes

import androidx.room.Entity
import java.util.Date

@Entity(
    tableName = "notes",
    primaryKeys = ["text", "date"]
)
data class Note(
    val text: String,
    val date: Date
)
