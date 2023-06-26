package br.com.drbf.backentry.ui.utils

import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import java.util.ArrayList

@Composable
fun SpeechToText(onMessage : (String) -> Unit): ManagedActivityResultLauncher<Intent, ActivityResult> {

  return  rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == ComponentActivity.RESULT_OK) {

            val speech: ArrayList<String>? =
                result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            speech?.let { it ->
                if (it.size > 0) {

                    val text = buildString {
                        it.forEach {
                            append(it)
                            append("\n")
                        }
                    }

                    onMessage(text)
                }


            }
        }

    }
}