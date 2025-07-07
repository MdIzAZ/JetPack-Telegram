plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
//    kotlin("android")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin") // Hilt plugin
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
}

android {
    namespace = "com.kroy.ssediotor"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kroy.sseditor"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "v1.8.1"


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
//            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    applicationVariants.all {
        outputs.all {
            this as com.android.build.gradle.internal.api.ApkVariantOutputImpl

            val name = "SSEditor"

            val apkName = name + "_" + defaultConfig.versionName + ".apk"

            outputFileName = apkName
        }
    }


}

dependencies {

    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.1")
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("com.google.android.material:material:1.12.0")

    implementation(platform("androidx.compose:compose-bom:2025.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("com.google.android.material:material:1.12.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2025.06.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")


    implementation("com.google.dagger:hilt-android:2.51.1")
    ksp("com.google.dagger:hilt-compiler:2.51.1")

    // Navigation dependencies (Compose)
    implementation("androidx.navigation:navigation-compose:2.9.0")
    // Jetpack Navigation for Compose


    // Hilt Jetpack Compose integration
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Coroutines dependencies
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // ViewModel dependencies (for Compose)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.1")

    // Icon Pack
    implementation("androidx.compose.material:material-icons-extended:1.7.8")


    // Retrofit dependencies
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp (optional, but commonly used with Retrofit)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("io.coil-kt:coil-compose:2.6.0")

    implementation("androidx.fragment:fragment-ktx:1.8.8")

    implementation("androidx.datastore:datastore-preferences:1.1.7")


}