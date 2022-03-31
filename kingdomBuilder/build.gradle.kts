import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.file.DuplicatesStrategy

plugins {
    application
    idea
    id("org.openjfx.javafxplugin") version "0.0.10"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.8.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.jetbrains:annotations:16.0.2")
    implementation(project(":network"))
    implementation(project(":annotations"))
    annotationProcessor(project(":annotationProcessors"))
}

javafx {
    version = "17"
    modules("javafx.controls", "javafx.fxml", "javafx.web")
}

application {
    mainClass.set("kingdomBuilder.Boot")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}


tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "kingdomBuilder.Boot"
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(configurations
            .runtimeClasspath
            .get()
            .onEach { println("Including dependency: ${it.name}") }
            .map { if(it.isDirectory) it else zipTree(it) })

    val sourcesMain = sourceSets.main.get()
    sourcesMain
        .allSource
        .forEach { println("Including from sources: ${it.name}")}

    from(sourcesMain.output)
}

tasks.withType<JavaExec> {
    // Java3D only recognizes "official" drivers for OpenGL;
    // recent drivers are not recognized on linux, especially when
    // using a rolling release distribution like arch.
    if(Os.isFamily(Os.FAMILY_UNIX))
        jvmArgs?.add("-Dprism.forceGPU=true")
}
