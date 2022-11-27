import org.springframework.boot.gradle.tasks.aot.AbstractAot

plugins {
    `java-convention`
    `spotless-config`
    alias(libs.plugins.graalvm.native)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.gitProperties)
}

dependencies {
    implementation(project(":web"))
    implementation(project(":signing"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-tomcat")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.apache.commons:commons-lang3")
    testImplementation("io.rest-assured:rest-assured")
    testImplementation(libs.bouncycastle.bcpkix)
    testImplementation("com.fasterxml.jackson.core:jackson-databind")

    runtimeOnly("org.springframework.boot:spring-boot-starter-validation")
    runtimeOnly("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    developmentOnly("org.springframework.boot:spring-boot-devtools:${libs.versions.spring.boot.get()}")
}

tasks.bootRun.configure {
    args("--spring.profiles.active=dev")
    systemProperty("spring.aot.enabled", "true")
}

tasks.test.configure {
    // use the AOT generated classes for context bootstrapping
    systemProperty("spring.aot.enabled", "true")
}

tasks.processAot.configure {
    // Spring's @ConditionalOnProperty annotation is not supported in GraalVM native images, so we have to
    // set a placeholder value during AOT processing. The HttpsRedirectConfiguration will be ignored otherwise.
    //    systemProperty("server.ssl.key-store", "placeholder")
}

springBoot {
    buildInfo {
        properties {
            excludes.set(setOf("time", "artifact", "group", "name"))
        }
    }
}

graalvmNative {
    binaries {
        named("main") {
            // otherwise a shared lib will be built since the java-library plugin is applied
            sharedLibrary.set(false)
        }
    }
}

spotless {
    java {
        // ignore source files generated during AOT processing
        targetExclude(fileTree("$buildDir/generated"))
    }
}
