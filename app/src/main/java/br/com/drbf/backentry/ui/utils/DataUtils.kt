package br.com.drbf.backentry.ui.utils

import android.text.format.DateFormat
import java.util.Date


fun Date.format() : String {
    return DateFormat.format("dd/MM/yyyy HH:mm:ss", this).toString()
}