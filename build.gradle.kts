import io.gitlab.arturbosch.detekt.report.ReportMergeTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"
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
    maven("https://raw.github.com/morpher-ru/morpher-ws3-java-client/mvn-repo")
}

dependencies {
    implementation("com.ithersta.tgbotapi:fsm:0.19.1")
    implementation("org.apache.poi:poi-ooxml:5.2.2")
    implementation("com.github.deividasstr:docx-word-replacer:0.4")
    implementation("io.ktor:ktor-client-okhttp:2.1.1")
    implementation("ru.morpher:ws3.client:1.0-SNAPSHOT")
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
