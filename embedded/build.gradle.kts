plugins {
    `java-convention`
    `spotless-config`
}

tasks.withType<JavaCompile> {
    doFirst {
        options.compilerArgs = listOf(
                "--module-path", classpath.asPath,
        )
    }
}

dependencies {
    implementation(project(":datetime"))
    implementation(project(":signing"))

    implementation("org.slf4j:slf4j-api")
    implementation("org.glassfish.grizzly:grizzly-http-server:${libs.versions.grizzly.get()}")
}
