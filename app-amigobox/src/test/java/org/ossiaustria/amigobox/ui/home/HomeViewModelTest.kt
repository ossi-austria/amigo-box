package org.ossiaustria.amigobox.ui.home

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.ossiaustria.amigobox.EntityMocks
import org.ossiaustria.amigobox.ui.loading.ViewModelTest
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.modules.UserContext
import org.ossiaustria.lib.domain.repositories.AlbumRepository

@FlowPreview
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class HomeViewModelTest : ViewModelTest() {

    lateinit var subject: HomeViewModel

    val albumRepository = mockk<AlbumRepository>()
    val userContext = mockk<UserContext>()

    @Test
    fun `init should load all albums`() {

        val mock = listOf(EntityMocks.album())
        coEvery { albumRepository.getAllAlbums() } answers { flowOf(Resource.success(mock)) }

        subject = HomeViewModel(coroutineRule.dispatcher, albumRepository, userContext)

        assertThat(subject.liveAlbums.value, equalTo(mock))
    }
}