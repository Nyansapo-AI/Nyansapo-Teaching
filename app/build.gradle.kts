import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    kotlin("plugin.serialization") version "2.1.10"
    alias(libs.plugins.google.gms.google.services)

    id("app.cash.sqldelight") version "2.1.0"
    alias(libs.plugins.google.firebase.appdistribution)
    id("com.google.firebase.crashlytics")
}


val localPropertiesFile = rootProject.file("local.properties")
val localProperties = Properties().apply {
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { stream ->
            load(stream)
        }
    }
}

android {
    namespace = "com.nyansapoai.teaching"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.nyansapoai.teaching"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            buildConfigField(
                "String",
                "AZURE_BASE_URL",
                localProperties.getProperty("AZURE_BASE_URL")
            )

            buildConfigField(
                "String",
                "AZURE_SUBSCRIPTION_KEY",
                localProperties.getProperty("AZURE_SUBSCRIPTION_KEY")
            )
            buildConfigField(
                "String",
                "AZURE_SPEECH_BASE_URL",
                localProperties.getProperty("AZURE_SPEECH_BASE_URL")
            )
            buildConfigField(
                "String",
                "AZURE_SPEECH_SUBSCRIPTION_KEY",
                localProperties.getProperty("AZURE_SPEECH_SUBSCRIPTION_KEY")
            )


        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}


sqldelight {
    databases {
        create("Database") {
            packageName.set("com.nyansapoai.teaching")
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.analytics)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation (libs.material3)


    // Koin for Android
    implementation(libs.koin.android)
    implementation (libs.koin.androidx.compose)
    testImplementation (libs.koin.test.junit4)

    //navigation
    implementation(libs.androidx.navigation.compose)

    //serialization
    implementation(libs.kotlinx.serialization.json)

    //firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.google.firebase.analytics)
    implementation(libs.firebase.crashlytics.ndk)

    //datetime
    implementation (libs.kotlinx.datetime)

    androidTestImplementation (libs.androidx.navigation.testing)

    //coil
    implementation(libs.coil.compose)

    //ktor
    implementation(libs.ktor.client.android)
    api(libs.ktor.core)
    implementation(libs.ktor.contentNegotiation)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.json)
    implementation(libs.ktor.logging)
    implementation(libs.ktor.client.okhttp)

    //lottie
    implementation (libs.lottie.compose)
    
    //microsoft cognitive services speech
    implementation(libs.client.sdk)

    // media 3
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.ui.compose)

    //showcase
    implementation (libs.introshowcaseview)


    //work manager
    implementation(libs.androidx.work.runtime.ktx)

    implementation(libs.androidx.room.runtime)

    //SQLDelight
    implementation(libs.android.driver)
    implementation(libs.sqlDelight.coroutine)

}