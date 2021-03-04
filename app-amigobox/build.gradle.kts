plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("de.mannodermaus.android-junit5")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    jacoco
}

android {

    compileSdkVersion(AndroidVersion.compileSdk)
    buildToolsVersion = AndroidVersion.buildTools

    defaultConfig {
        applicationId = "org.ossiaustria.amigobox"
        minSdkVersion(AndroidVersion.minSdk)
        targetSdkVersion(AndroidVersion.targetSdk)
        versionCode = 1
        versionName = "0.0.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions {
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
    }

    useLibrary("android.test.runner")
    useLibrary("android.test.base")
    useLibrary("android.test.mock")

    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }

    kapt {
        correctErrorTypes = true
    }
}

dependencies {

    project(":lib-domain")
    implementation(Libs.appcompat)
    implementation(Libs.Compose.material)
    implementation(Libs.material)

//    implementation(Libs.dagger)
    implementation(Libs.hiltAndroid)
    implementation(Libs.hiltCommon)
    implementation(Libs.hiltLifecycle)
//    kapt(Libs.daggerCompiler)
    kapt(Libs.hiltAndroidCompiler)
    kapt(Libs.hiltCompiler)

    implementation(Libs.navigationRuntimeKtx)
    implementation(Libs.navigationFragmentKtx)
    implementation(Libs.navigationUiKtx)
    implementation(Libs.navigationDynamicFeatures)
    androidTestImplementation(Libs.navigationTest)
//    implementation("androidx.appcompat:appcompat:1.2.0")
//    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
//    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.0")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.0")
}

