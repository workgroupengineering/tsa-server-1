import org.apache.tools.ant.util.TeeOutputStream
import java.io.ByteArrayOutputStream

plugins {
    `java-convention`
    `spotless-config`
}

dependencies {
    implementation(project(":signing"))
    implementation(project(":datetime"))
    implementation(project(":datetime-spring-config"))

    implementation("org.springframework.boot:spring-boot")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("jakarta.validation:jakarta.validation-api")
    implementation("org.mapstruct:mapstruct:${libs.versions.mapstruct.get()}")

    integrationTestImplementation("org.springframework.boot:spring-boot-starter-test")
    integrationTestImplementation("org.hibernate.validator:hibernate-validator")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.mapstruct:mapstruct-processor:${libs.versions.mapstruct.get()}")
}

tasks.register("searchForClassUsages") {
    doLast {
        copy {
            from(configurations["runtimeClasspath"])
            into(temporaryDir)
            include("*.jar")
        }

        val depsWithPackageDependency = mutableSetOf<String>()

        fileTree(temporaryDir).files.parallelStream().forEach {
            println(it.name)

            val outputStream = ByteArrayOutputStream()

            exec {
                workingDir(temporaryDir)
                commandLine("jdeps", "--multi-release", "11", "--module-path", temporaryDir.absolutePath, it.name)
                standardOutput = outputStream
            }

            val output = outputStream.toString()
            if (output.contains("-> javax.xml")) {
                depsWithPackageDependency.add(it.name)
            }
        }

        println(depsWithPackageDependency)
    }

    dependsOn(configurations["runtimeClasspath"])
}
