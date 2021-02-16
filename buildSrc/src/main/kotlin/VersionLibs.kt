//import Versions.compose_version

object AndroidVersion {
    const val compileSdk = 30
    val buildTools = "29.0.3"
    val minSdk = 23
    val targetSdk = 30
}

object Versions {
    val gradle = "4.1.1"
    val kotlin = "1.4.20"
}

object Libs {

    val appcompat_version = "1.2.0"
    val appcompat = "androidx.appcompat:appcompat:$appcompat_version"
    val appcompatResources = "androidx.appcompat:appcompat-resources:$appcompat_version"

    // Using Compose (still alpha) for declarative GUI components
    object Compose {
        val compose_version = "1.0.0-alpha12"
        val animation = "androidx.compose.animation:animation:$compose_version"
        val compiler = "androidx.compose.compiler:compiler:$compose_version"
        val foundation = "androidx.compose.foundation:foundation:$compose_version"
        val material = "androidx.compose.material:material:$compose_version"
        val runtime = "androidx.compose.runtime:runtime:$compose_version"
        val ui = "androidx.compose.ui:ui:$compose_version"
    }

    val core_ktx_version = "1.3.2"
    val coreKtx = "androidx.core:core-ktx:$core_ktx_version"
    val coroutines_version = "1.4.2"
    val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version"
    val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version"

    // Koin for DI (better: IoC or Service Locator)
    val koin_version = "2.2.2"
    val koin = "org.koin:koin-android:$koin_version"
    val koinViewmodel = "org.koin:koin-androidx-viewmodel:$koin_version"
    val koinScope = "org.koin:koin-androidx-scope:$koin_version"

    object Lifecycle {
        val lifecycle_version = "2.3.0"
        val arch_version = "2.1.0"

        val viewmodel = "androidx.lifecycle:lifecycle-viewmodel:$lifecycle_version"
        val viewmodelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
        val livedata = "androidx.lifecycle:lifecycle-livedata:$lifecycle_version"
        val livedataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
        val viewmodeSavedstate = "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"
        val viewmodelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha01"
        val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:$lifecycle_version"
        val lifecycleKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version"

        // optional - helpers for implementing LifecycleOwner in a Service
        val lifecycleService = "androidx.lifecycle:lifecycle-service:$lifecycle_version"
        val coreTesting = "androidx.arch.core:core-testing:$arch_version"
    }

    val material_version = "1.3.0"
    val material = "com.google.android.material:material:$material_version"

    // Navigation
    val navigation_version = "2.3.3"
    val navigationRuntime = "androidx.navigation:navigation-runtime:$navigation_version"
    val navigationFragment = "androidx.navigation:navigation-fragment:$navigation_version"
    val navigationUi = "androidx.navigation:navigation-ui:$navigation_version"
    val navigationRuntimeKtx = "androidx.navigation:navigation-runtime-ktx:$navigation_version"
    val navigationFragmentKtx = "androidx.navigation:navigation-fragment-ktx:$navigation_version"
    val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:$navigation_version"

    // Retrofit for HTTP and Json wrapping
    const val retrofit_version = "2.9.0"
    val retrofit = "com.squareup.retrofit2:retrofit:$retrofit_version"
    val retrofitJson = "com.squareup.retrofit2:converter-gson:$retrofit_version"

    // Room for Persistence
    val room_version = "2.2.6"
    val roomRuntime = "androidx.room:room-runtime:$room_version"
    val roomCompiler = "androidx.room:room-compiler:$room_version"
    val roomCommon = "androidx.room:room-common:$room_version"
    val roomKtx = "androidx.room:room-ktx:$room_version"
    val roomTesting = "androidx.room:room-testing:$room_version"

    // Dropbox Store4 for building Repositories with Room and Retrofit
    val store4_version = "4.0.0"
    val store4 = "com.dropbox.mobile.store:store4:${store4_version}"

    object Test {
        val xtest_version = "1.3.0"
        val test = "androidx.test:core:$xtest_version"
        val testExt = "androidx.test.ext:junit:1.1.2"

        const val espresso_version = "3.3.0"
        val espressoCore = "androidx.test.espresso:espresso-core:$espresso_version"
        val espressoContrib = "androidx.test.espresso:espresso-contrib:$espresso_version"

        const val junit5_version = "5.3.2"
        val jupiterApi = "org.junit.jupiter:junit-jupiter-api:$junit5_version"
        val jupiterEngine = "org.junit.jupiter:junit-jupiter-engine:$junit5_version"
        val jupiterParams = "org.junit.jupiter:junit-jupiter-params:$junit5_version"

//        val junit4_version = "4.12"
//        val junit = "junit:junit:$junit4_version"

        const val mockk_Version = "1.10.6"
        val mockk = "io.mockk:mockk:${mockk_Version}"
        val mockkAndroid = "io.mockk:mockk-android:${mockk_Version}"

        const val barista_version = "3.7.0"
        val barista = "com.schibsted.spain:barista:$barista_version"

        val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines_version"
    }
}