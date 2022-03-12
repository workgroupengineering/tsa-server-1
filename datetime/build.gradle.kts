plugins {
    `java-convention`
    `spotless-config`
}

tasks.compileJava {
    // ignore compiler warnings concerning exports to unknown modules
    options.compilerArgs.add("-Xlint:-module")
}
