package org.ossiaustria.dependencies

import org.ossiaustria.dependencies.Versions.compose_version

// https://google.github.io/accompanist/
object Libs {

    const val appcompat_version = "1.3.0"
    const val appcompat = "androidx.appcompat:appcompat:$appcompat_version"
    const val appcompatResources = "androidx.appcompat:appcompat-resources:$appcompat_version"

    object Coil {
        const val coil_version = "1.3.2"
        const val coilCompose = "io.coil-kt:coil-compose:$coil_version"
        const val coil = "io.coil-kt:coil:$coil_version"
    }

    // Using Compose (still alpha) for declarative GUI components
    object Compose {
        const val ui = "androidx.compose.ui:ui:$compose_version"
        const val uiTooling = "androidx.compose.ui:ui-tooling:$compose_version"
        const val activity = "androidx.activity:activity-compose:1.3.1"
        const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07"
        const val livedata = "androidx.compose.runtime:runtime-livedata:$compose_version"
        const val rxjava2 = "androidx.compose.runtime:runtime-rxjava2:$compose_version"
        const val foundation = "androidx.compose.foundation:foundation:$compose_version"
        const val animation = "androidx.compose.animation:animation:$compose_version"
//        const val compiler = "androidx.compose.compiler:compiler:$compose_version"

        const val material = "androidx.compose.material:material:$compose_version"
        const val materialIconsCore =
            "androidx.compose.material:material-icons-core:$compose_version"
        const val materialIconsExtended =
            "androidx.compose.material:material-icons-extended:$compose_version"
//        const val runtime = "androidx.compose.runtime:runtime:$compose_version"
    }

    const val core_ktx_version = "1.6.0"

    const val coreKtx = "androidx.core:core-ktx:$core_ktx_version"
    const val coroutines_version = "1.5.2"
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version"
    const val coroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version"

    const val dropbox_store_version = "4.0.2-KT15"
    const val dropboxStore = "com.dropbox.mobile.store:store4:${dropbox_store_version}"

    // hilt & dagger
    const val dagger_version = "2.38.1"
    const val hilt_ext_version = "1.0.0"
    const val hilt_android_version = "2.38.1"
    const val dagger = "com.google.dagger:dagger:$dagger_version"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:$dagger_version"
    const val hiltAndroid = "com.google.dagger:hilt-android:$hilt_android_version"
    const val hiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:$hilt_android_version"
    const val hiltAndroidTesting = "com.google.dagger:hilt-android-testing:$hilt_android_version"
    const val hiltCompiler = "androidx.hilt:hilt-compiler:$hilt_ext_version"
    const val hiltCommon = "androidx.hilt:hilt-common:$hilt_ext_version"
    const val hiltLifecycle = "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03"

    // Jitsi
    const val jitsiMeetSdk = "org.jitsi.react:jitsi-meet-sdk:3.9.1"

    object Firebase {
        const val firebaseBom = "com.google.firebase:firebase-bom:28.4.0"
    }

    object Koin {
        const val koin_version= "3.1.2"
        const val koinCore = "io.insert-koin:koin-core:$koin_version"
        const val koinTest = "io.insert-koin:koin-test:$koin_version"
        const val koinTestJunit = "io.insert-koin:koin-test-junit4:$koin_version"
        const val koinAndroid = "io.insert-koin:koin-android:$koin_version"
        const val koinCompose = "io.insert-koin:koin:koin-androidx-compose:$koin_version"
    }

    object Lifecycle {
        const val lifecycle_version = "2.3.1"
        const val arch_version = "2.1.0"

        const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel:$lifecycle_version"
        const val viewmodelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
        const val livedata = "androidx.lifecycle:lifecycle-livedata:$lifecycle_version"
        const val livedataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
        const val viewmodeSavedstate =
            "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"
        const val viewmodelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha01"
        const val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:$lifecycle_version"
        const val lifecycleKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version"

        // optional - helpers for implementing LifecycleOwner in a Service
        const val lifecycleService = "androidx.lifecycle:lifecycle-service:$lifecycle_version"
        const val coreTesting = "androidx.arch.core:core-testing:$arch_version"
    }

    const val material_version = "1.4.0"
    const val material = "com.google.android.material:material:$material_version"

    // Navigation
    const val navigation_version = "2.3.5"
    const val navigationDynamicFeatures =
        "androidx.navigation:navigation-dynamic-features-fragment:$navigation_version"
    const val navigationRuntimeKtx =
        "androidx.navigation:navigation-runtime-ktx:$navigation_version"
    const val navigationFragmentKtx =
        "androidx.navigation:navigation-fragment-ktx:$navigation_version"
    const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:$navigation_version"
    const val navigationTest = "androidx.navigation:navigation-testing:$navigation_version"


    const val permissions = "com.nabinbhandari.android:permissions:3.8"


    // Retrofit for HTTP and Json wrapping
    const val retrofit_version = "2.9.0"
    const val retrofit = "com.squareup.retrofit2:retrofit:$retrofit_version"
    const val retrofitJson = "com.squareup.retrofit2:converter-gson:$retrofit_version"

    const val okhttp_version = "4.9.1"

    //    const val mockwebserver = "com.squareup.okhttp3:mockwebserver:$okhttp_version"
    const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$okhttp_version"

    // Room for Persistence
    const val room_version = "2.3.0"
    const val roomRuntime = "androidx.room:room-runtime:$room_version"
    const val roomCompiler = "androidx.room:room-compiler:$room_version"
    const val roomCommon = "androidx.room:room-common:$room_version"
    const val roomKtx = "androidx.room:room-ktx:$room_version"
    const val roomTesting = "androidx.room:room-testing:$room_version"

    const val timber_version = "5.0.1"
    const val timber = "com.jakewharton.timber:timber:$timber_version"

    object Security {
        const val crypto = "androidx.security:security-crypto:1.0.0"
    }

    object Test {
        const val xtest_version = "1.4.0"
        const val robolectric = "org.robolectric:robolectric:4.6.1"
        const val test = "androidx.test:core:$xtest_version"
        const val testExt = "androidx.test.ext:junit-ktx:1.1.2"
        const val testRunner = "androidx.test:runner:$xtest_version"
        const val testRules = "androidx.test:rules:$xtest_version"
        const val testTruth = "androidx.test:truth:$xtest_version"
        const val orchestrator = "androidx.test:orchestrator:$xtest_version"

        const val espresso_version = "3.4.0"
        const val espressoCore = "androidx.test.espresso:espresso-core:$espresso_version"
        const val espressoContrib = "androidx.test.espresso:espresso-contrib:$espresso_version"

        const val junit5_version = "5.7.2"
        const val manno_junit5_version = "1.2.2"
        const val mannodermausAndroidTestCore =
            "de.mannodermaus.junit5:android-test-core:$manno_junit5_version"
        const val mannodermausAndroidTestRunner =
            "de.mannodermaus.junit5:android-test-runner:$manno_junit5_version"

        const val jupiterApi = "org.junit.jupiter:junit-jupiter-api:$junit5_version"
        const val jupiterEngine = "org.junit.jupiter:junit-jupiter-engine:$junit5_version"
        const val jupiterVintageEngine = "org.junit.vintage:junit-vintage-engine:$junit5_version"
        const val jupiterParams = "org.junit.jupiter:junit-jupiter-params:$junit5_version"

//        const val junit4_version = "4.12"
//        const val junit = "junit:junit:$junit4_version"

        const val mockk_Version = "1.12.0"
        const val mockk = "io.mockk:mockk:${mockk_Version}"
        const val mockkAndroid = "io.mockk:mockk-android:${mockk_Version}"

        const val barista_version = "3.9.0"
        const val barista = "com.schibsted.spain:barista:$barista_version"

        const val coroutinesTest =
            "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines_version"

    }
}