package br.com.drbf.backentry.data.notes

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import br.com.drbf.backentry.domain.NotesRepository
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class DefaultNotesRepository @Inject constructor(val notesDao: NotesDao) : NotesRepository {
    override fun getName(): String {
        return "DefaultNotesRepository"
    }

    override suspend fun insert(note: Note) {

        notesDao.insert(note)
    }

    override suspend fun delete(note: Note) {

        notesDao.delete(note)
    }


    override fun getNotes() = Pager(
        config = PagingConfig(
            pageSize = 20,
        ),
        pagingSourceFactory = {
            NotesPagingSource(notesDao)
        }
    ).flow

    override fun getCount(): Flow<Int> {
        return notesDao.getCount()
    }

    inner class NotesPagingSource(
        private val notesDao: NotesDao
    ) : PagingSource<Int, Note>() {

        override fun getRefreshKey(state: PagingState<Int, Note>): Int? {
            return state.anchorPosition?.let { anchorPosition ->
                state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                    ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
            }
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Note> {
            return try {
                val page = params.key ?: 1
                val limit = params.loadSize
                val offset = (page - 1) * params.loadSize
                val notes = notesDao.getNotesPage(limit, offset)

                Timber.d( "Page: $page")
                Timber.d( "Limit: $limit")
                Timber.d( "Offset: $offset")
                Timber.d( notes.toString())

                LoadResult.Page(
                    data = notes,
                    prevKey = if (page == 1) null else page.minus(1),
                    nextKey = if (notes.isEmpty()) null else page.plus(1),
                )

            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }
    }

}