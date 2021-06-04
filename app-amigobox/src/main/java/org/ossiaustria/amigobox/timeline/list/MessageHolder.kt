package org.ossiaustria.amigobox.timeline.list

import android.view.View
import org.ossiaustria.amigobox.databinding.MessageViewBinding
import org.ossiaustria.lib.domain.models.Sendable

//open class SendableBindingWrap(val view: ViewGroup) {
//    fun getRoot(): View = view
//}

//class MessageBindingWrap(val binding: MessageViewBinding) : SendableBindingWrap(binding.root) {
//
//}

class MessageHolder(private val binding: MessageViewBinding, view: View) : SendableHolder(view) {

    constructor(viewBinding: MessageViewBinding) : this(viewBinding, viewBinding.root)

    override fun bind(sendable: Sendable) {
//        if (sendable !is Message) throw IllegalStateException("MessageHolder must show Message")
        binding.message.text = sendable.toString()
    }

}