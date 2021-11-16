plugins {
    application
    id("org.openjfx.javafxplugin") version "0.0.10"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.8.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
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
