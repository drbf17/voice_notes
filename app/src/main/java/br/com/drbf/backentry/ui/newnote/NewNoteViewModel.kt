package br.com.drbf.backentry.ui.newnote

import androidx.lifecycle.ViewModel
import br.com.drbf.backentry.domain.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewNoteViewModel @Inject constructor(val notesRepository: NotesRepository): ViewModel() {

    fun getName(): String {
        return notesRepository.getName()
    }
}