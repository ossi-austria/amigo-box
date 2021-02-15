//import Versions.compose_version

object AndroidVersion {
    val compileSdk = 30
    val buildTools = "29.0.3"
    val minSdk = 23
    val targetSdk = 30
}

object Versions{
    val gradle = "4.1.1"
    val kotlin = "1.4.20"
}

object Libs {
    object UI {
        object Compose {
            val compose_version = "1.0.0-alpha12"
            val animation = "androidx.compose.animation:animation:$compose_version"
            val compiler = "androidx.compose.compiler:compiler:$compose_version"
            val foundation = "androidx.compose.foundation:foundation:$compose_version"
            val material = "androidx.compose.material:material:$compose_version"
            val runtime = "androidx.compose.runtime:runtime:$compose_version"
            val ui = "androidx.compose.ui:ui:$compose_version"
        }

        val appcompat_version = "1.2.0"
        val appcompat= "androidx.appcompat:appcompat:$appcompat_version"
        val appcompatResources = "androidx.appcompat:appcompat-resources:$appcompat_version"
    }

    object Lifecycle {
        val lifecycle_version = "2.3.0"
        val arch_version = "2.1.0"

        val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
        val livedata ="androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
        val viewmodeSavedstate= "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"
        val viewmodelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha01"
        val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:$lifecycle_version"
        // optional - helpers for implementing LifecycleOwner in a Service
        val lifecycleService =  "androidx.lifecycle:lifecycle-service:$lifecycle_version"
        val coreTesting = "androidx.arch.core:core-testing:$arch_version"
    }

    object Test {
        val version = "1.3.0"
        val test = "androidx.test:$version"
    }
}