import de.undercouch.gradle.tasks.download.Download

plugins {
    `java-convention`
    `spotless-config`
    alias(libs.plugins.download)
}

dependencies {
    implementation("org.apache.tomcat.embed:tomcat-embed-core")

    implementation(project(":integration"))
    implementation("org.springframework:spring-webmvc")
    implementation("org.springframework:spring-tx")
    implementation("org.springframework.boot:spring-boot")
    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("jakarta.servlet:jakarta.servlet-api")
    implementation("jakarta.validation:jakarta.validation-api")
    implementation("commons-codec:commons-codec")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("org.apache.commons:commons-lang3")
    implementation("commons-io:commons-io:${libs.versions.commonsIo.get()}")

    runtimeOnly("org.thymeleaf:thymeleaf-spring6")

    testImplementation("commons-io:commons-io:${libs.versions.commonsIo.get()}")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jsoup:jsoup:${libs.versions.jsoup.get()}")

    testRuntimeOnly("org.springframework.boot:spring-boot-starter-validation")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

val webResourcesWithTargetFolder = mapOf(
        // Bootstrap
        "https://cdn.jsdelivr.net/npm/bootstrap@${libs.versions.bootstrap.core.get()}/dist/css/bootstrap.min.css" to "css",
        "https://cdn.jsdelivr.net/npm/bootstrap-icons@${libs.versions.bootstrap.icons.get()}/font/bootstrap-icons.css" to "css",
        "https://cdn.jsdelivr.net/npm/bootstrap-icons@${libs.versions.bootstrap.icons.get()}/font/fonts/bootstrap-icons.woff2" to "css/fonts",
        "https://cdn.jsdelivr.net/npm/bootstrap@${libs.versions.bootstrap.core.get()}/dist/js/bootstrap.bundle.min.js" to "js",

        // Datatables
        "https://cdn.datatables.net/${libs.versions.datatables.get()}/css/dataTables.bootstrap5.min.css" to "css",
        "https://cdn.datatables.net/${libs.versions.datatables.get()}/js/jquery.dataTables.min.js" to "js",
        "https://cdn.datatables.net/${libs.versions.datatables.get()}/js/dataTables.bootstrap5.min.js" to "js",

        // JQuery
        "https://code.jquery.com/jquery-${libs.versions.jquery.get()}.min.js" to "js"
)

val downloadWebResources by tasks.registering(Download::class) {
    src(webResourcesWithTargetFolder.keys)
    overwrite(false)
    eachFile {
        val targetFolder = webResourcesWithTargetFolder[sourceURL.toExternalForm()]
        path = "$targetFolder/$path"
    }
    dest(layout.buildDirectory.dir("web-resources/static"))
}

sourceSets.main {
    resources.srcDir(downloadWebResources.map { it.dest.parentFile })
}

tasks.processResources.configure {
    // Copies each english properties resource bundle file into a file with the resource bundle's default name
    from("src/main/resources/") {
        include("*_en.properties")
        rename("(.*)_en.properties", "$1.properties")
    }
}
