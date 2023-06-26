package br.com.drbf.backentry.ui

sealed class Routes(val route : String){

    object Notes: Routes("notes")
    object NewNote: Routes("newnote")





}
