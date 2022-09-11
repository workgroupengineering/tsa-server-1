plugins {
    id("com.diffplug.spotless")
}

spotless {
    kotlin {
        diktat().configFile("$rootDir/diktat-analysis.yml")
    }

    java {
        importOrderFile("$rootDir/spotless.importorder")
        removeUnusedImports()
        eclipse().configFile("$rootDir/eclipse-formatter.xml")
    }
}
