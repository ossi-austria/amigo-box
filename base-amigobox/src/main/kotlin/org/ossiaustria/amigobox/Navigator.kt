package org.ossiaustria.amigobox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.models.Person
import timber.log.Timber

class Navigator {

    private lateinit var navController: NavController
    private var activity: AppCompatActivity? = null

    fun bind(activity: AppCompatActivity, navController: NavController) {
        this.activity = activity
        this.navController = navController
    }

    fun back() {
        navController.popBackStack()
    }

    suspend fun autoBack(delay: Long = DELAY_MEDIUM) {
        delay(delay)
        Timber.d("autoBack after 3000s")
        withContext(Dispatchers.Main) {
            navController.popBackStack()
        }
    }

    fun toLoading() {
        navController.navigate(R.id.loadingFragment)
    }

    fun toTimeline() {
        navController.navigate(R.id.timelineFragment)
    }

    fun toPersonDetail(person: Person) {
        val bundle = Bundle().apply { setPerson(this, person) }
        navController.navigate(R.id.personDetailFragment, bundle)
    }

    /**
     * Navigate without an existing (=incoming) Call to CallFragment, to
     * create a NEW OUTGOING call to call a certain Person
     */
    fun toCallFragment(person: Person) {
        val bundle = Bundle().apply { setPerson(this, person) }
        navController.navigate(R.id.callFragment, bundle)
    }

    /**
     * Navigate with an EXISTING (=incoming) Call to CallFragment
     */
    fun toCallFragment(call: Call) {
        val bundle = Bundle().apply { setCall(this, call) }
        navController.navigate(R.id.callFragment, bundle)
    }

    fun toContacts() {
        navController.navigate(R.id.contactsFragment)
    }

    fun toHome() {
        navController.navigate(R.id.homeFragment)
    }

    fun toAlbums() {
        navController.navigate(R.id.albumsFragment)
    }

    fun toImageGallery(album: Album) {
        val bundle = Bundle().apply { setAlbum(this, album) }
        navController.navigate(R.id.imageGalleryFragment, bundle)
    }

    companion object {
        const val PARAM_ALBUM = "ALBUM"
        const val PARAM_PERSON = "PERSON"
        const val PARAM_CALL = "CALL"

        const val DELAY_MEDIUM = 3000L
        const val DELAY_LONG = 5000L

        fun getPerson(bundle: Bundle) = bundle.getSerializable(PARAM_PERSON) as Person?
        fun setPerson(bundle: Bundle, person: Person?) =
            bundle.putSerializable(PARAM_PERSON, person)

        fun getAlbum(bundle: Bundle) = bundle.getSerializable(PARAM_ALBUM) as Album?
        fun setAlbum(bundle: Bundle, album: Album?) =
            bundle.putSerializable(PARAM_ALBUM, album)

        fun getCall(bundle: Bundle) = bundle.getSerializable(PARAM_CALL) as Call?
        fun setCall(bundle: Bundle, call: Call?) =
            bundle.putSerializable(PARAM_CALL, call)

    }
}
