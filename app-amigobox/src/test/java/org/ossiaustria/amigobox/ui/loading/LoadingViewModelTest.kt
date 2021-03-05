package org.ossiaustria.amigobox.ui.loading

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach

class LoadingViewModelTest {

    lateinit var subject: LoadingViewModel

    @BeforeEach
    fun setupBeforeEach() {
        subject = LoadingViewModel()
    }

    @Test
    fun `doFancyHeavyStuffOnBackground should login my user `() {
        subject.doFancyHeavyStuffOnBackground()
        val value = subject.liveUserLogin.value
        assertEquals("user logged in!", value)
    }

}