package br.com.drbf.backentry.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.drbf.backentry.ui.newnote.NewNoteScreen
import br.com.drbf.backentry.ui.notes.NotesScreen

@Composable
fun MainScreen() {

    val navController = rememberNavController()
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(navController = navController, startDestination = Routes.Notes.route) {
            composable(Routes.Notes.route) {

                NotesScreen(navController = navController)

            }


            composable(Routes.NewNote.route) {

                NewNoteScreen(navController = navController)

            }
        }
    }

}

