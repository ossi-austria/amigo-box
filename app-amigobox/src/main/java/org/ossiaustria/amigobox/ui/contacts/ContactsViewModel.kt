package org.ossiaustria.amigobox.ui.contacts

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(): ViewModel() {

    var contactsScrollPosition: Int = 0

}