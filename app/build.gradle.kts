plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.redfast.mpass"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.redfast.mpass"
        minSdk = 21
        targetSdk = 33
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
    kotlinOptions {
        jvmTarget = "1.8"
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
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

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

    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation("com.google.android.flexbox:flexbox:3.0.0")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation(platform("com.google.firebase:firebase-bom:32.7.1"))
    implementation("com.google.firebase:firebase-analytics")
    "googleImplementation"("com.google.firebase:firebase-messaging")

    "amazonCompileOnly"(files("src/amazon/libs/amazon-device-messaging-1.2.0.jar"))
    "amazonImplementation"(files("src/amazon/libs/redfast-amazon-release.aar"))
    "googleImplementation"(files("src/google/libs/redfast-google-release.aar"))
    "googleImplementation"("com.android.billingclient:billing:6.0.1")
    "googleImplementation"("com.android.billingclient:billing-ktx:6.0.1")

}