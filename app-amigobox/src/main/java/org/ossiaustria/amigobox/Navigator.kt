package org.ossiaustria.amigobox

import android.os.Bundle
import androidx.navigation.NavController
import dagger.hilt.android.scopes.ActivityScoped
import org.ossiaustria.lib.domain.models.Person
import javax.inject.Inject

@ActivityScoped
class Navigator @Inject constructor() {

    private lateinit var navController: NavController

    fun bind(navController: NavController) {
        this.navController = navController
    }

    fun toLoading() {
        navController.navigate(R.id.loadingFragment)
    }

    fun toTimeline() {
        navController.navigate(R.id.timelineFragment)
    }

    fun toJitsiCall() {
        navController.navigate(R.id.jitsiFragment)
    }

    fun toLogin() {
        navController.navigate(R.id.loginFragment)
    }

    fun toPersonDetail(person: Person) {
        val bundle = Bundle().apply {
            setPerson(this, person)
        }
        navController.navigate(R.id.personDetailFragment, bundle)
    }

    fun toCallPerson(person: Person) {
        val bundle = Bundle().apply {
            putSerializable(PARAM_PERSON, person)
        }
        navController.navigate(R.id.jitsiFragment, bundle)
    }

    fun toContacts() {
        navController.navigate(R.id.contactsFragment)
    }

    fun toHome() {
        navController.navigate(R.id.homeFragment)
    }

    fun toAlbums(){
        navController.navigate(R.id.albumsFragment)
    }

    fun toImageGallery(){
        navController.navigate(R.id.imageGalleryFragment)
    }

    companion object {
        const val PARAM_PERSON = "PERSON"
        fun getPerson(bundle: Bundle) = bundle.getSerializable(PARAM_PERSON) as Person
        fun setPerson(bundle: Bundle, person: Person) =
            bundle.putSerializable(PARAM_PERSON, person)
    }
}
