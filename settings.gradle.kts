pluginManagement {
    repositories {
        google()
        mavenCentral() // <-- AQUÍ ESTABA EL ERROR
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral() // <-- Y AQUÍ TAMBIÉN
    }
}

rootProject.name = "Desarrollo"
include(":app")
