plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.doanmonhoc"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.doanmonhoc"
        minSdk = 34
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.glide)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.converter.gson)
    implementation(libs.retrofit)
    implementation(libs.google.material)
    implementation(libs.logging.interceptor)
    implementation(libs.rxandroid)
    implementation(libs.rxjava)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.android.lottie)
    implementation(libs.commons.io)
    implementation(libs.picasso)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}