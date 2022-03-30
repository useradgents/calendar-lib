plugins {
    kotlin("android")
    id("com.android.library")
    id("maven-publish")
}


group="com.github.useradgents"
version="1.2.2"


android {

    compileSdk = 30
    defaultConfig {
        minSdk = 21
        targetSdk = 30
    }

    publishing {
        singleVariant("release") {
            // if you don't want sources/javadoc, remove these lines
            withSourcesJar()
            withJavadocJar()
        }
    }

}


afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("CalendarLib") {
                from(components["release"])
            }
        }
    }
}

//afterEvaluate {
//    publishing {
//        publications {
//            release(MavenPublication) {
//                from components.release
//                        groupId 'com.example'
//                artifactId 'mylibrary'
//                version = android.defaultConfig.versionName // or manually '1.0'
//            }
//        }
//    }
//}

//publishing {
//    publications {  }
//}

dependencies {
    implementation("androidx.appcompat:appcompat:1.0.2")
    implementation("androidx.recyclerview:recyclerview:1.0.0")
    api("com.jakewharton.timber:timber:5.0.1")
}
