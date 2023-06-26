package br.com.drbf.backentry.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import br.com.drbf.backentry.data.notes.Note
import br.com.drbf.backentry.domain.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import javax.inject.Scope

@HiltViewModel
class NotesViewModel @Inject constructor(val notesRepository: NotesRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(NotesState())
    val uiState: StateFlow<NotesState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            notesRepository.getCount().collectLatest { count ->
                _uiState.value = _uiState.value.copy(count = count)
            }
        }
    }

    fun getNotes(): Flow<PagingData<Note>> = notesRepository.getNotes().cachedIn(viewModelScope)

    fun handleEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.NewNoteEvent -> {
                insertNote(
                    text = event.text
                )
            }
            is NotesEvent.DeleteNoteEvent -> {
                delete(event.note)
            }
        }
    }

    private fun insertNote(text: String) {

        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.insert(
                Note(
                    text = text,
                    date = Date()
                )
            )
        }
    }

    private fun delete(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.delete(note)
        }
    }
}