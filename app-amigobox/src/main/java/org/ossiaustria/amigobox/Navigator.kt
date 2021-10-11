package org.ossiaustria.amigobox

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.jitsi.ui.AmigoJitsiActivity
import org.ossiaustria.lib.jitsi.ui.AmigoSingleJitsiActivity

class Navigator() {

    private lateinit var navController: NavController
    private var activity: AppCompatActivity? = null

    fun bind(activity: AppCompatActivity, navController: NavController) {
        this.activity = activity
        this.navController = navController
    }

    fun toLoading() {
        navController.navigate(R.id.loadingFragment)
    }

    fun toTimeline() {
        navController.navigate(R.id.timelineFragment)
    }

    fun toJitsiCall() {
//        activity?.startActivity(Intent(activity, AmigoJitsiActivity::class.java))
        activity?.startActivity(Intent(activity, AmigoSingleJitsiActivity::class.java))
    }

    fun toLogin() {
        navController.navigate(R.id.loginFragment)
    }

    fun toPersonDetail(person: Person) {
        val bundle = Bundle().apply { setPerson(this, person) }
        navController.navigate(R.id.personDetailFragment, bundle)
    }

    fun toCallPerson(person: Person) {
        activity?.startActivity(
            Intent(activity, AmigoJitsiActivity::class.java).apply {
                putExtra(PARAM_PERSON, person)
            })

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
        fun getPerson(bundle: Bundle) = bundle.getSerializable(PARAM_PERSON) as Person
        fun setPerson(bundle: Bundle, person: Person) =
            bundle.putSerializable(PARAM_PERSON, person)

        fun getAlbum(bundle: Bundle) = bundle.getSerializable(PARAM_ALBUM) as Album
        fun setAlbum(bundle: Bundle, album: Album) =
            bundle.putSerializable(PARAM_ALBUM, album)
    }
}
