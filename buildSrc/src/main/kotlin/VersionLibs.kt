//import Versions.compose_version

object AndroidVersion {
    const val compileSdk = 30
    val buildTools = "29.0.3"
    val minSdk = 23
    val targetSdk = 30
}

object Versions {
    val gradle = "4.1.1"
    val kotlin = "1.4.30"
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

    val dropbox_store_version = "4.0.0"
    val dropboxStore = "com.dropbox.mobile.store:store4:${dropbox_store_version}"


    // hilt & dagger
    val dagger_version = "2.33"
    val hilt_version = "1.0.0-alpha03"
    val hilt_android_version = "2.33-beta"
    val dagger = "com.google.dagger:dagger:$dagger_version"
    val hiltAndroid = "com.google.dagger:hilt-android:$hilt_android_version"
    val hiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:$hilt_android_version"
    val hiltAndroidTesting = "com.google.dagger:hilt-android-testing:$hilt_android_version"
    val hiltCommon = "androidx.hilt:hilt-common:$hilt_version"
    val hiltLifecycle = "androidx.hilt:hilt-lifecycle-viewmodel:$hilt_version"
    val daggerCompiler = "com.google.dagger:dagger-compiler:$dagger_version"
    val hiltCompiler = "androidx.hilt:hilt-compiler:$hilt_version"


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
        val viewmodeSavedstate =
            "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"
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
    val navigationDynamicFeatures =
        "androidx.navigation:navigation-dynamic-features-fragment:$navigation_version"
    val navigationRuntimeKtx = "androidx.navigation:navigation-runtime-ktx:$navigation_version"
    val navigationFragmentKtx = "androidx.navigation:navigation-fragment-ktx:$navigation_version"
    val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:$navigation_version"
    val navigationTest = "androidx.navigation:navigation-testing:$navigation_version"

    // Retrofit for HTTP and Json wrapping
    const val retrofit_version = "2.9.0"
    val retrofit = "com.squareup.retrofit2:retrofit:$retrofit_version"
    val retrofitJson = "com.squareup.retrofit2:converter-gson:$retrofit_version"

    const val okhttp_version = "4.9.0"

    //    val mockwebserver = "com.squareup.okhttp3:mockwebserver:$okhttp_version"
    val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$okhttp_version"

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

    const val timber_version = "4.7.1"
    val timber = "com.jakewharton.timber:timber:$timber_version"

    object Test {
        val xtest_version = "1.3.0"
        val test = "androidx.test:core:$xtest_version"
        val testExt = "androidx.test.ext:junit:1.1.2"
        val testRunner = "androidx.test:runner:1.1.2"
        val testRules = "androidx.test:runner:1.1.2"
        val orchestrator = "androidx.test:orchestrator:$xtest_version"

        const val espresso_version = "3.3.0"
        val espressoCore = "androidx.test.espresso:espresso-core:$espresso_version"
        val espressoContrib = "androidx.test.espresso:espresso-contrib:$espresso_version"

        const val junit5_version = "5.4.1"
        const val manno_junit5_version = "1.0.0"
        val mannodermausAndroidTestCore =
            "de.mannodermaus.junit5:android-test-core:$manno_junit5_version"
        val mannodermausAndroidTestRunner =
            "de.mannodermaus.junit5:android-test-runner:$manno_junit5_version"

        val jupiterApi = "org.junit.jupiter:junit-jupiter-api:$junit5_version"
        val jupiterEngine = "org.junit.jupiter:junit-jupiter-engine:$junit5_version"
        val jupiterVintageEngine = "org.junit.jupiter:junit-vintage-engine:$junit5_version"
        val jupiterParams = "org.junit.jupiter:junit-jupiter-params:$junit5_version"

//        val junit4_version = "4.12"
//        val junit = "junit:junit:$junit4_version"

        const val mockk_Version = "1.9.3"
        val mockkAndroid = "io.mockk:mockk-android:${mockk_Version}"

        const val barista_version = "3.7.0"
        val barista = "com.schibsted.spain:barista:$barista_version"

        val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines_version"

    }
}