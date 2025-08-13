plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.redfast.mpass"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.redfast.mpass"
        minSdk = 25
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    flavorDimensions += "device"

    productFlavors {
        create("google") {
            isDefault = true
            dimension = "device"
        }
        create("amazon") {
            dimension = "device"
        }
        create("noiap") {
            dimension = "device"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.9")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.9")
    testImplementation("junit:junit:4.13.2")

    //Moshi
    implementation("com.squareup.moshi:moshi-kotlin:1.9.2")
    implementation("com.squareup.moshi:moshi-kotlin-codegen:1.9.2")
    //Retrofit2
    implementation("com.squareup.retrofit2:retrofit:2.6.2")
    implementation("com.squareup.retrofit2:converter-moshi:2.6.2")
    //Okhttp3
    implementation("com.squareup.okhttp3:okhttp:4.2.2")
    implementation("com.squareup.okhttp3:logging-interceptor:4.2.2")
    //Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")

    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("com.google.android.flexbox:flexbox:3.0.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation("com.google.code.gson:gson:2.8.9")
    "amazonImplementation"("com.github.redfast.redfast-sdk-android-build:redfast-sdk-amazon:v2.2.1.6a")
    "googleImplementation"("com.github.redfast.redfast-sdk-android-build:redfast-sdk-google:v2.2.1.6a")
    "noiapImplementation"("com.github.redfast.redfast-sdk-android-build:redfast-sdk-noiap:v2.2.1.6a")
    "googleImplementation"("com.android.billingclient:billing:6.0.1")
    "googleImplementation"("com.android.billingclient:billing-ktx:6.0.1")
    "amazonImplementation"(files("src/amazon/A3LMessaging-1.1.0.aar"))
}
