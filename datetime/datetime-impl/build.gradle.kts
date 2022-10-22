plugins {
    `java-convention`
    `spotless-config`
}

dependencies {
    api(project(":datetime:datetime-api"))

    implementation("org.springframework:spring-context")
}
