import org.ossiaustria.dependencies.AndroidVersion
import org.ossiaustria.dependencies.Libs

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    jacoco
}

android {
    compileSdkVersion = "" + AndroidVersion.compileSdk
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

    // Room & Persistance
    implementation(Libs.dropboxStore)
    implementation(Libs.roomRuntime)
    kapt(Libs.roomCompiler)
    implementation(Libs.roomKtx)
    testImplementation(Libs.roomTesting)

    // Android, material & friends
    implementation(Libs.timber)
    implementation(Libs.appcompat)
    implementation(Libs.Compose.material)

    // retrofit & web
    implementation(Libs.retrofit)
    implementation(Libs.retrofitJson)
    implementation(Libs.loggingInterceptor)

    // hilt & dagger
//    implementation(Libs.dagger)
    implementation(Libs.hiltAndroid)
    implementation(Libs.hiltCommon)
    implementation(Libs.hiltLifecycle)
//    kapt(Libs.daggerCompiler)
    kapt(Libs.hiltAndroidCompiler)
    kapt(Libs.hiltCompiler)

    testImplementation(Libs.hiltAndroidTesting)
    kaptTest(Libs.hiltAndroidCompiler)
    kaptTest(Libs.hiltCompiler)

    // Testing
    testImplementation(Libs.Test.jupiterApi)
    testImplementation(Libs.Test.jupiterEngine)
    testImplementation(Libs.Test.jupiterParams)

    testImplementation(Libs.Test.test)
    testImplementation(Libs.Test.testExt)
    implementation(Libs.Test.coroutinesTest)
    testImplementation(Libs.Test.mockkAndroid)
    androidTestImplementation(Libs.Test.mockkAndroid)
//    testImplementation(Libs.mockwebserver)

    androidTestImplementation(Libs.Test.espressoCore)
    androidTestImplementation(Libs.Test.espressoContrib)
    androidTestImplementation(Libs.Test.barista)
    androidTestImplementation(Libs.Test.testRunner)
    androidTestImplementation(Libs.Test.testExt)
    androidTestImplementation(Libs.Test.testRules)
    // Required for instrumented tests
}



