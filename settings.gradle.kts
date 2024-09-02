import java.net.URI

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {

    repositories {
        google()
        mavenCentral()
        maven{
            url = URI("https://jitpack.io");
        }
    }

}

rootProject.name = "Plantio"
include(":app")
