// Top-level build file where you can add configuration options common to all sub-projects/modules.


buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

group="com.github.useradgents"
version="1.2.0"

allprojects {
    repositories {
        google()
        mavenCentral()
//        maven { url "https://raw.github.com/synergian/wagon-git/releases"}
    }

}


tasks.register("clean",Delete::class){
    delete(rootProject.buildDir)
}