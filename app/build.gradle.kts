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
    // Keep only the alias-based dependencies
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.credentials)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.play.services.auth)
    // In app/build.gradle dependencies
    implementation("com.google.firebase:firebase-firestore:25.0.0")
    // Test dependencies
    // For modern UI components
    implementation("com.google.android.material:material:1.12.0")
// FirebaseUI for easily connecting RecyclerView to Firestore
    implementation("com.firebaseui:firebase-ui-firestore:8.0.2")
// Glide for loading profile images
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}