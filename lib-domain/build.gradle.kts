plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("de.mannodermaus.android-junit5")
}

android {
    compileSdkVersion(AndroidVersion.compileSdk)
    buildToolsVersion = AndroidVersion.buildTools

    defaultConfig {
        minSdkVersion(AndroidVersion.minSdk)
        targetSdkVersion(AndroidVersion.targetSdk)
        versionCode = 1
        versionName = "0.0.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(Libs.roomRuntime)
    kapt(Libs.roomCompiler)
    implementation(Libs.roomKtx)

    implementation(Libs.appcompat)
    implementation(Libs.Compose.material)

    testImplementation(Libs.Test.jupiterApi)
    testImplementation(Libs.Test.jupiterEngine)
    testImplementation(Libs.Test.jupiterParams)

    testImplementation(Libs.Test.test)
    testImplementation(Libs.Test.testExt)
    testImplementation(Libs.roomTesting)
    testImplementation(Libs.Test.coroutinesTest)
    testImplementation(Libs.Test.mockk)
    testImplementation(Libs.Test.mockkAndroid)

    androidTestImplementation(Libs.Test.espressoCore)
    androidTestImplementation(Libs.Test.espressoContrib)
    androidTestImplementation(Libs.Test.barista)

}