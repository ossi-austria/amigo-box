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

        //given

        // test
        subject.doFancyHeavyStuffOnBackground()

        // check
        val value = subject.liveUserLogin.value
        assertEquals("user logged in!", value)
    }

    @Test
    fun `doFancyHeavyStuffOnBackground should fail when network is unavailable `() {

        //given
        //
        // network = broken

        // test
        subject.doFancyHeavyStuffOnBackground()

        // check
        val value = subject.liveUserLogin.value
        assertEquals("error!", value)
    }
}