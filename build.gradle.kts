import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    kotlin("plugin.serialization") version "1.7.21"
    id("com.google.devtools.ksp") version "1.7.21-1.0.8"
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
    application
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
    runtimeOnly(files("src/main/ruby"))
    implementation("ru.morpher:ws3.client:1.0-SNAPSHOT")
    implementation("com.ithersta.tgbotapi:fsm:0.21.0")
    implementation("com.ithersta.tgbotapi:sqlite-persistence:0.2.0")
    implementation("com.ithersta.tgbotapi:commands:0.1.0")
    implementation("org.apache.poi:poi-ooxml:5.2.2")
    implementation("com.github.deividasstr:docx-word-replacer:0.4")
    implementation("org.apache.commons:commons-email:1.5")
    implementation("io.ktor:ktor-client-okhttp:2.1.1")
    implementation("org.jsoup:jsoup:1.15.3")
    implementation("org.slf4j:slf4j-simple:2.0.4")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    implementation("org.junit.jupiter:junit-jupiter:5.9.0")
    implementation("commons-validator:commons-validator:1.7")
    implementation("ru.morpher:ws3.client:1.0-SNAPSHOT")
    implementation("com.ibm.icu:icu4j:72.1")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation("io.github.classgraph:classgraph:4.8.151")
    implementation("org.jruby:jruby-complete:9.4.0.0")
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

application {
    mainClass.set("MainKt")
}
