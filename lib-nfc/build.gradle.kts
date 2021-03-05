import org.ossiaustria.dependencies.AndroidVersion
import org.ossiaustria.dependencies.Libs

plugins {
    id("com.android.application")
    kotlin("android")
    id("de.mannodermaus.android-junit5")
    jacoco
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
}

dependencies {
    implementation(Libs.appcompat)
}