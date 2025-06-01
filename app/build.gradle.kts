plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.logins"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.logins"
        minSdk = 24
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


}

dependencies {

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.github.bumptech.glide:glide:4.15.1")
    implementation(libs.cardview)
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")
    implementation ("androidx.recyclerview:recyclerview:1.3.0")
    implementation ("com.squareup.picasso:picasso:2.8")


    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(files("libs\\jtds-1.3.1.jar"))
    implementation(files("libs\\mysql-connector-j-9.1.0.jar"))
    implementation(libs.swiperefreshlayout)
    implementation(libs.recyclerview)


    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}