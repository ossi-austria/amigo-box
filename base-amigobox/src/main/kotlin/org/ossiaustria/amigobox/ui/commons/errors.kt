package org.ossiaustria.amigobox.ui.commons

import android.content.Context
import android.widget.Toast
import org.ossiaustria.amigobox.R

object Toasts {

    fun personNotFound(context: Context) = showLong(context, context.getString(R.string.person_not_found))

    fun showLong(context: Context, text: String) =
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()

    private fun showShort(context: Context, text: String) =
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}