package org.ossiaustria.amigobox.timeline.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.ossiaustria.lib.domain.models.Sendable

open class SendableHolder(val view: View) : RecyclerView.ViewHolder(view) {
    open fun bind(sendable: Sendable) {

    }
}