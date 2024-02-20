buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.2" apply false
<<<<<<< HEAD
    id("com.google.gms.google-services") version "4.4.1" apply false
=======
    id("com.google.gms.google-services") version "4.4.0" apply false
>>>>>>> 3b1bcaa3e59ab127bdb6c94caa445a77b0d21c2b
}