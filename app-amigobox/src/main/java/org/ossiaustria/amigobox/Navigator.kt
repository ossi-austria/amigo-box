package org.ossiaustria.amigobox

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

    fun toPersonDetail() {
        navController.navigate(R.id.personDetailFragment)
    }

    fun toCallPerson(person: Person) {
        navController.navigate(R.id.jitsiFragment)
    }

    fun toContacts() {
        navController.navigate(R.id.contactsFragment)
    }

    fun toHome() {
        navController.navigate(R.id.loadingFragment)
    }

    fun toAlbums(){
        navController.navigate(R.id.albumsFragment)
    }

    fun toImageGallery(){
        navController.navigate(R.id.imageGalleryFragment)
    }

}