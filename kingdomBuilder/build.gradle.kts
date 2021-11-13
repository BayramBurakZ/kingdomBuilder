plugins {
    application
    id("org.openjfx.javafxplugin") version "0.0.10"
}

javafx {
    version = "17"
    modules("javafx.controls", "javafx.fxml")
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("kingdomBuilder.Boot")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "kingdomBuilder.Boot"
    }
}

