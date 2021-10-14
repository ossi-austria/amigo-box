package org.ossiaustria.amigobox.timeline

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.ossiaustria.amigobox.databinding.MessageViewBinding
import org.ossiaustria.amigobox.timeline.list.MessageHolder
import org.ossiaustria.amigobox.timeline.list.SendableHolder
import org.ossiaustria.lib.domain.models.AlbumShare
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.models.Message
import org.ossiaustria.lib.domain.models.Multimedia
import org.ossiaustria.lib.domain.models.Sendable


class TimelineAdapter : RecyclerView.Adapter<SendableHolder>() {

    private var sendables: List<Sendable> = listOf()

    fun setSendables(list: List<Sendable>) {
        sendables = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SendableHolder {
        val from = LayoutInflater.from(parent.context)

        return when (viewType) {
            TYPE_ALBUM_SHARE ->
                MessageHolder(MessageViewBinding.inflate(from, parent, false))
            TYPE_CALL ->
                MessageHolder(MessageViewBinding.inflate(from, parent, false))
            TYPE_MESSAGE ->
                MessageHolder(MessageViewBinding.inflate(from, parent, false))
            TYPE_MULTIMEDIA ->
                MessageHolder(MessageViewBinding.inflate(from, parent, false))

            else -> MessageHolder(
                MessageViewBinding.inflate(from, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: SendableHolder, position: Int) {
        val sendable = sendables[position]
        holder.bind(sendable)
    }

    override fun getItemCount(): Int = sendables.size

    override fun getItemViewType(position: Int): Int {
        return when (sendables[position]) {
            is AlbumShare -> TYPE_ALBUM_SHARE
            is Call -> TYPE_CALL
            is Message -> TYPE_MESSAGE
//            is Multimedia -> TYPE_MULTIMEDIA
            else -> throw IllegalStateException("Not supported: ${sendables[position]}")
        }
    }

    companion object {
        const val TYPE_ALBUM_SHARE = 1
        const val TYPE_CALL = 2
        const val TYPE_MESSAGE = 3
        const val TYPE_MULTIMEDIA = 4
    }
}