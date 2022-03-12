plugins {
    `java-convention`
    `spotless-config`
}

dependencies {
    api(project(":datetime"))
    api(project(":domain"))

    implementation(libs.bouncycastle.bcpkix)
    implementation("jakarta.validation:jakarta.validation-api")
    implementation("commons-io:commons-io:2.11.0")
    implementation("org.apache.commons:commons-lang3")

    testImplementation(project(":test-util"))
    testImplementation("org.apache.commons:commons-lang3")
    testImplementation("commons-codec:commons-codec")
}

tasks.compileJava {
    // ignore compiler warnings concerning exports to unknown modules
    // suppressing all module warnings is not ideal, but there seems to be no better alternative
    options.compilerArgs.add("-Xlint:-module")
}
