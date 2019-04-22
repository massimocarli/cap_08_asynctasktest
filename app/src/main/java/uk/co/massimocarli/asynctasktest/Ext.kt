package uk.co.massimocarli.asynctasktest

import android.app.Activity
import android.widget.Toast

fun Activity.asToast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()