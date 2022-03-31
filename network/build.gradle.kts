plugins {
    java
    idea
}

repositories {
    mavenCentral()
}

group = "kingdomBuilder"
version = "1.0"

val junitVersion: String by extra

dependencies {
    implementation(project(":annotations"))
    annotationProcessor(project(":annotationProcessors"))

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

