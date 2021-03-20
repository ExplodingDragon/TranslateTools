import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.jvm.tasks.Jar

plugins {
    kotlin("jvm")
    application
    id("org.openjfx.javafxplugin") version "0.0.9"
}

java.sourceCompatibility = JavaVersion.VERSION_11

val compileKotlin: KotlinCompile by tasks
val compileJava: JavaCompile by tasks
compileJava.destinationDir = compileKotlin.destinationDir

val mainClassStr = "i.translate.MainKt"

application{
    mainClass.set(mainClassStr)
}


javafx {
    version = "11.0.2"
    modules("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib"))
    implementation("com.github.OpenEdgn.Logger4K:core:1.4.0")
    implementation("org.json:json:20210307")
    implementation("com.github.OpenEdgn.Logger4K:logger-console:1.4.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")
    testImplementation("org.junit.platform:junit-platform-launcher:1.6.2")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "11"
}
val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

tasks.register<Jar>("fatJar") {
    archiveClassifier.set("fatJar")
    manifest {
        attributes["Main-Class"] = mainClassStr
    }
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}


