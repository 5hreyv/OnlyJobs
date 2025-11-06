// build.gradle (Module :app)

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.onlyjobs"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.onlyjobs"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
dependencies {

    // Firebase Libraries (no versions needed because of the BOM)
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.google.firebase:firebase-database:21.0.0")
    // For Realtime Database

    // Google Services
    implementation (libs.play.services.auth)
            implementation (libs.credentials)
            implementation (libs.credentials.play.services.auth)
            implementation (libs.googleid)

            // AndroidX & Material Components
            implementation (libs.appcompat)
            implementation (libs.material)
            implementation (libs.activity)
            implementation (libs.constraintlayout)

            // UI Libraries
            // IMPORTANT: Use firebase-ui-database for Realtime Database
            implementation ("com.firebaseui:firebase-ui-database:8.0.2")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation(platform("com.google.firebase:firebase-bom:34.5.0"))
    implementation("com.google.firebase:firebase-analytics")

    // Testing
    testImplementation (libs.junit)
            androidTestImplementation (libs.ext.junit)
            androidTestImplementation (libs.espresso.core)
}