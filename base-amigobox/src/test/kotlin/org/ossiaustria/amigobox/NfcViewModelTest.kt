package org.ossiaustria.amigobox

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.ossiaustria.amigobox.nfc.NfcViewModel
import org.ossiaustria.amigobox.nfc.NfcViewModelEvent.*
import org.ossiaustria.lib.commons.testing.TestCoroutineRule
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.AmigoNfcInfo
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.enums.MemberType
import org.ossiaustria.lib.domain.models.enums.NfcTagType
import org.ossiaustria.lib.domain.repositories.AlbumRepository
import org.ossiaustria.lib.domain.repositories.PersonRepository
import org.ossiaustria.lib.domain.services.NfcInfoService
import java.util.UUID.randomUUID
import kotlin.test.assertEquals

@FlowPreview
internal class NfcViewModelTest {

    private lateinit var subject: NfcViewModel

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @MockK
    lateinit var nfcInfoService: NfcInfoService

    @MockK
    lateinit var albumRepository: AlbumRepository

    @MockK
    lateinit var personRepository: PersonRepository

    @MockK
    lateinit var navigator: Navigator

    @Before
    fun setupBeforeEach() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        subject = NfcViewModel(
            coroutineRule.dispatcher,
            nfcInfoService,
            albumRepository, personRepository
        )
    }


    @Test
    fun `handleNfcInfo should load Album for OPEN_ALBUM`() =
        runBlockingTest {
            // prepare test
            val nfcInfo = mockNfcInfo(tagType = NfcTagType.OPEN_ALBUM)
            val album = EntityMocks.album(id = nfcInfo.linkedAlbumId!!)

            coEvery { nfcInfoService.findPerRef(eq("nfcRef")) } returns Resource.success(nfcInfo)
            every { albumRepository.getAlbum(eq(nfcInfo.linkedAlbumId!!)) } returns
                flowOf(Resource.success(album))

            subject.handleNfcInfo(nfcInfo)

            verify { albumRepository.getAlbum(eq(nfcInfo.linkedAlbumId!!)) }

            subject.nfcEvent.observeForever {
                assertEquals(OpenAlbum(album), it)
            }
        }

    @Test
    fun `handleNfcInfo should load Person for CALL_PERSON`() = runBlockingTest {
        // prepare test
        val nfcInfo = mockNfcInfo(tagType = NfcTagType.CALL_PERSON)
        val person = EntityMocks.person(personId = nfcInfo.linkedPersonId!!, randomUUID())

        coEvery { nfcInfoService.findPerRef(eq("nfcRef")) } returns Resource.success(nfcInfo)
        every { personRepository.getPerson(eq(nfcInfo.linkedPersonId!!)) } returns
            flowOf(Resource.success(person))

        subject.handleNfcInfo(nfcInfo)

        verify { personRepository.getPerson(eq(nfcInfo.linkedPersonId!!)) }

        subject.nfcEvent.observeForever {
            assertEquals(CallPerson(person), it)
        }
    }

    private fun mockNfcInfo(
        tagType: NfcTagType = NfcTagType.OPEN_ALBUM,
        nfcRef: String = "nfcRef"
    ) = AmigoNfcInfo(
        randomUUID(), randomUUID(), randomUUID(),
        tagType,
        "name", nfcRef,
        randomUUID(), randomUUID()
    )

    @Test
    fun `openAlbum should navigate to Album `() = runBlockingTest {
        val mock = Album(randomUUID(), "Peter", randomUUID(), emptyList())
        subject.openAlbum(mock, navigator)
        every { navigator.toImageGallery(eq(mock)) }
    }

    @Test
    fun `callPerson should navigate to Person `() = runBlockingTest {
        val mock = Person(
            randomUUID(),
            "Peter",
            randomUUID(),
            MemberType.MEMBER,
            "abc@gmx.de"
        )

        subject.callPerson(mock, navigator)
        every { navigator.toCallFragment(eq(mock)) }
    }

}