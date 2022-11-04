import io.gitlab.arturbosch.detekt.report.ReportMergeTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"
    id("com.google.devtools.ksp") version "1.7.20-1.0.7"
    id("io.gitlab.arturbosch.detekt").version("1.22.0-RC2")
}

group = "ru.spbstu"
version = "1.0-SNAPSHOT"

sourceSets.main {
    java.srcDirs("build/generated/ksp/main/kotlin")
}

repositories {
    mavenCentral()
    maven("https://repo.repsy.io/mvn/ithersta/tgbotapi")
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.ithersta.tgbotapi:fsm:0.19.3")
    implementation("com.ithersta.tgbotapi:sqlite-persistence:0.1.0")
    implementation("org.apache.poi:poi-ooxml:5.2.2")
    implementation("com.github.deividasstr:docx-word-replacer:0.4")
    implementation("io.ktor:ktor-client-okhttp:2.1.1")
    compileOnly("io.insert-koin:koin-annotations:1.0.3")
    ksp("io.insert-koin:koin-ksp-compiler:1.0.3")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

detekt {
    buildUponDefaultConfig = true
}
