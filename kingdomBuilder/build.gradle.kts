plugins {
    application
    id("org.openjfx.javafxplugin") version "0.0.10"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.13.2")
}

javafx {
    version = "17"
    modules("javafx.controls", "javafx.fxml")
}

application {
    mainClass.set("kingdomBuilder.Boot")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "kingdomBuilder.Boot"
    }
}
