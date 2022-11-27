import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    `java-library`
    jacoco
}

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

// Workaround for using version catalogs in precompiled script plugins.
// See https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
val libs = the<LibrariesForLibs>()

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:${libs.versions.spring.boot.get()}"))
    annotationProcessor(platform("org.springframework.boot:spring-boot-dependencies:${libs.versions.spring.boot.get()}"))

    implementation("org.slf4j:slf4j-api")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core")
    testImplementation("org.mockito:mockito-junit-jupiter")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    // required for GraalVM AOT processing
    options.compilerArgs.add("-parameters")
}

tasks.check.configure {
    finalizedBy(tasks.jacocoTestReport)
}

jacoco {
    toolVersion = libs.versions.jacoco.get()
}

// Makes the project version configurable via a project property that can be passed in via
// the -P CLI parameter. Useful for CI builds to infer the project version from the branch name.
if (hasProperty("projectVersion")) {
    version = property("projectVersion")!!
}

tasks.withType<Jar>().configureEach {
    manifest {
        attributes("Implementation-Version" to archiveVersion)
    }
}
