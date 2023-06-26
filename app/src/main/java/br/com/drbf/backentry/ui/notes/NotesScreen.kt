package br.com.drbf.backentry.ui.notes

import android.content.Intent
import android.content.res.Configuration
import android.speech.RecognizerIntent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.drbf.backentry.R
import br.com.drbf.backentry.data.notes.Note
import br.com.drbf.backentry.ui.Routes
import br.com.drbf.backentry.ui.loading.LoadingScreen
import br.com.drbf.backentry.ui.utils.SpeechToText
import br.com.drbf.backentry.ui.utils.format
import br.com.drbf.backentry.ui.utils.isScrollingUp
import java.net.URLEncoder
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    navController: NavHostController? = null, viewModel: NotesViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val notes = viewModel.getNotes().collectAsLazyPagingItems()

    LaunchedEffect(uiState.value.count) {
        notes.refresh()
    }


    val speechToTextLauncher = SpeechToText(onMessage = { message ->
        viewModel.handleEvent(
            NotesEvent.NewNoteEvent(message)
        )
    })

    val lazyListState =  rememberLazyListState()


    Scaffold(topBar = { DefaultTopAppBar(navController = navController) },
        floatingActionButton = {
            DefaultFloatingActionButton(
                speechToTextLauncher,
                expanded = lazyListState.isScrollingUp()
            ) }

    ) { contentPadding ->


        MessageList(modifier = Modifier.padding(contentPadding),
            notes = notes,
            count = uiState.value.count,
            lazyListState = lazyListState,
            onEvent = { event -> viewModel.handleEvent(event) })
    }
}


@Preview(showBackground = true)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark Mode"
)
@Composable
fun NotesScreenPreview() {

    NotesScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopAppBar(navController: NavHostController?) {

    TopAppBar(title = {
        Row(
            Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Voz Para Texto")
        }
    },

        actions = {
            IconButton(onClick = { navController?.navigate(Routes.NewNote.route) }) {
                Icon(
                    imageVector = Icons.Filled.Add, contentDescription = "Localized description"
                )
            }
        })

}

@Composable
fun DefaultFloatingActionButton(
    speechToTextLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    expanded: Boolean = true
) {

    FloatingActionButton(onClick = { speechToTextLauncher.launch(makeRecognizerIntent()) }) {

        Row(
            Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_mic_24),
                contentDescription = "Inicia a gravação de voz"
            )


            AnimatedVisibility(visible = expanded) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Gravar")
            }

        }


    }

}

@Composable
fun MessageList(
    modifier: Modifier = Modifier,
    notes: LazyPagingItems<Note>? = null,
    onEvent: ((NotesEvent) -> Unit)? = null,
    count: Int,
    lazyListState: LazyListState
) {


    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = lazyListState

    ) {

        item {

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(16.dp),
                    text = "Converta sua voz para texto e envie no WhatsApp",
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(16.dp),
                    text = if (count > 1) "${count} notas" else "${count} nota",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        if (notes != null) {


            items(count = notes.itemCount) { index ->
                notes[index]?.let { note ->
                    Message(
                        note = note, onEvent = onEvent
                    )
                }
            }

            when (notes.loadState.refresh) {

                is LoadState.Loading -> {
                    item {
                        LoadingScreen()
                    }
                }

                else -> {}
            }
        }

        item {
            Spacer(modifier = Modifier.height(48.dp))
        }


    }
}

@Preview(showBackground = true)
@Composable
fun MessageListPreview() {

    MessageList(
        notes = null,
        count = 10,
        lazyListState = rememberLazyListState()
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Message(
    note: Note, onEvent: ((NotesEvent) -> Unit)? = null
) {

    val uriHandler = LocalUriHandler.current
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    Card(onClick = {
        expanded = !expanded
    }) {

        Column(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End,

            ) {

            Text(
                text = note.text,
                maxLines = if (expanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis

            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(1f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = note.date.format(),
                    style = MaterialTheme.typography.bodySmall
                )

                IconButton(onClick = {

                    onEvent?.invoke(NotesEvent.DeleteNoteEvent(note))

                }) {

                    Icon(
                        imageVector = Icons.Filled.Delete, contentDescription = "Deletar"
                    )


                }

                IconButton(onClick = {

                    try {

                        val encoded = "UTF-8"
                        uriHandler.openUri(
                            "https://wa.me/?text=${
                                URLEncoder.encode(
                                    note.text, encoded
                                )
                            }"
                        )


                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }) {

                    Image(
                        painter = painterResource(id = R.drawable.ic_whatsapp),
                        contentDescription = "Enviar nota para o WhatsApp"
                    )


                }

            }//Row

        }//Column

    }
}

@Preview(showBackground = true)
@Composable
fun MessagePreview() {

    Message(
        note = Note(
            text = "Olá, eu gostaria de um empréstimo!", date = Date()
        )
    )
}

fun makeRecognizerIntent(): Intent {

    return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {

        putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "br.com.drbf.backentry")
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR")
        putExtra(RecognizerIntent.EXTRA_PROMPT, "Diga sua mensagem")
    }


}