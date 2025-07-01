

plugins {
    id("dev.kikugie.stonecutter")
    alias(libs.plugins.modPublishPlugin)
    alias(libs.plugins.modstitch) apply false
}
stonecutter active "1.21.6-fabric" /* [SC] DO NOT CHANGE */

val releaseMod by tasks.registering {
    group = "replanter"
    dependsOn("buildAndCollect")
    dependsOn("releaseModVersion")
    dependsOn("publishMods")
}

val modVersion: String by project
version = modVersion

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://maven.kikugie.dev/third-party")
        maven("https://maven.neoforged.net")
        maven("https://maven.fabricmc.net/")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }
}