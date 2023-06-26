package br.com.drbf.backentry.ui.notes

import br.com.drbf.backentry.data.notes.Note

sealed class NotesEvent {

    class NewNoteEvent(val text : String): NotesEvent()
    class DeleteNoteEvent(val note: Note) : NotesEvent() {

    }
}