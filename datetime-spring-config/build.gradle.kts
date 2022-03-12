plugins {
    `java-convention`
    `spotless-config`
}

dependencies {
    implementation(project(":datetime"))
    implementation("org.springframework:spring-context")
}
