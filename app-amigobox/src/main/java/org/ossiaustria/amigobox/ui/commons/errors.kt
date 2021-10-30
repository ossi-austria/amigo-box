package org.ossiaustria.amigobox.ui.commons

import android.content.Context
import android.widget.Toast

object Toasts {

    fun personNotFound(context: Context) = showLong(context, "Person not found")

    private fun showLong(context: Context, text: String) =
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()

    private fun showShort(context: Context, text: String) =
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}