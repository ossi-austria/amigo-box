package org.ossiaustria.amigobox

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.jitsi.ui.AmigoSingleJitsiActivity
import org.ossiaustria.lib.jitsi.ui.AmigoSingleJitsiActivity.Companion.PARAM_JITSI_TOKEN
import org.ossiaustria.lib.jitsi.ui.AmigoSingleJitsiActivity.Companion.PARAM_JITSI_URL

class Navigator() {

    private lateinit var navController: NavController
    private var activity: AppCompatActivity? = null

    fun bind(activity: AppCompatActivity, navController: NavController) {
        this.activity = activity
        this.navController = navController
    }

    fun back() {
        navController.popBackStack()
    }

    fun toLoading() {
        navController.navigate(R.id.loadingFragment)
    }

    fun toTimeline() {
        navController.navigate(R.id.timelineFragment)
    }

    fun toJitsiCall(url: String, token: String) {
        activity?.startActivity(Intent(activity, AmigoSingleJitsiActivity::class.java).apply {
            putExtra(PARAM_JITSI_URL, url)
            putExtra(PARAM_JITSI_TOKEN, token)
        })
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

        fun getPerson(bundle: Bundle) = bundle.getSerializable(PARAM_PERSON) as Person?
        fun setPerson(bundle: Bundle, person: Person) =
            bundle.putSerializable(PARAM_PERSON, person)

        fun getAlbum(bundle: Bundle) = bundle.getSerializable(PARAM_ALBUM) as Album
        fun setAlbum(bundle: Bundle, album: Album) =
            bundle.putSerializable(PARAM_ALBUM, album)

        fun getCall(bundle: Bundle) = bundle.getSerializable(PARAM_CALL) as Call?
        fun setCall(bundle: Bundle, call: Call) =
            bundle.putSerializable(PARAM_CALL, call)

    }
}
