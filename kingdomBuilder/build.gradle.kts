import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.file.DuplicatesStrategy
import kotlin.collections.listOf

plugins {
    application
    idea
    id("org.openjfx.javafxplugin") version "0.0.10"
}

repositories {
    mavenCentral()
}

group = "kingdomBuilder"
version = "1.0"

val javaFxVersion: String by extra
val junitVersion: String by extra
val jetbrainsAnnotationVersion: String by extra

dependencies {
    implementation(project(":network"))
    implementation(project(":annotations"))
    annotationProcessor(project(":annotationProcessors"))

    implementation("org.jetbrains:annotations:$jetbrainsAnnotationVersion")

    // Explicitly state runtime dependencies;
    // otherwise they won't be included in the resulting FatJar.
    val targetPlatforms = listOf("win", "linux", "mac")
    for(target in targetPlatforms) {
        println(target)
        runtimeOnly("org.openjfx:javafx-web:$javaFxVersion:$target")
        runtimeOnly("org.openjfx:javafx-media:$javaFxVersion:$target")
        runtimeOnly("org.openjfx:javafx-fxml:$javaFxVersion:$target")
        runtimeOnly("org.openjfx:javafx-controls:$javaFxVersion:$target")
        runtimeOnly("org.openjfx:javafx-graphics:$javaFxVersion:$target")
        runtimeOnly("org.openjfx:javafx-base:$javaFxVersion:$target")
    }

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
}

javafx {
    version = javaFxVersion
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
