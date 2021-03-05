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
        testInstrumentationRunnerArgument(
            "runnerBuilder",
            "de.mannodermaus.junit5.AndroidJUnit5Builder"
        )
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
        exclude("META-INF/LICENSE*")
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

    test {
        useJUnitPlatform()
    }
}

test {
    useJUnitPlatform()
}

dependencies {

    testImplementation("junit:junit:4.12")
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

    // Testing
    testImplementation(Libs.Test.jupiterApi)
    testImplementation(Libs.Test.jupiterParams)
    testRuntimeOnly(Libs.Test.jupiterEngine)
//    testRuntimeOnly(Libs.Test.jupiterVintageEngine)

    androidTestImplementation(Libs.Test.testRunner)
    androidTestImplementation(Libs.Test.jupiterApi)
    androidTestImplementation(Libs.Test.mannodermausAndroidTestCore)
    androidTestRuntimeOnly(Libs.Test.mannodermausAndroidTestRunner)

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


}

