plugins {
    java
    idea
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.8.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(project(":annotations"))
    annotationProcessor(project(":annotationProcessors"))
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
