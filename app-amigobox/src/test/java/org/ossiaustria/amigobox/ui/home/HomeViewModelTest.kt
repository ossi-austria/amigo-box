package org.ossiaustria.amigobox.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.modules.UserContext
import org.ossiaustria.lib.domain.repositories.AlbumRepository

@FlowPreview
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class HomeViewModelTest {

    lateinit var subject: HomeViewModel

    val albumRepository = mockk<AlbumRepository>()
    val userContext = mockk<UserContext>()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun `init should load all albums`() {

        coEvery { albumRepository.getAllAlbums() } answers { flowOf(Resource.success(listOf())) }

        subject = HomeViewModel(albumRepository, userContext)

    }
}