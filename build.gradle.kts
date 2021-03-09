buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0-alpha09")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinVersion}")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
}

task("clean") {
    delete(rootProject.buildDir)
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java).configureEach {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }
}