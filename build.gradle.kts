plugins {
    kotlin("jvm") version "1.8.10"
    kotlin("plugin.serialization") version "1.8.10"
    id("com.google.devtools.ksp") version "1.8.10-1.0.9"
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
    implementation("ru.morpher:ws3.client:1.0-SNAPSHOT")
    implementation("com.ithersta.tgbotapi:fsm:0.29.0")
    implementation("com.ithersta.tgbotapi:sqlite-persistence:0.6.1")
    implementation("com.ithersta.tgbotapi:commands:0.3.0")
    implementation("org.apache.poi:poi-ooxml:5.2.2")
    implementation("com.deepoove:poi-tl:1.12.1")
    implementation("org.apache.xmlgraphics:batik-bridge:1.16")
    implementation("org.apache.commons:commons-email:1.5")
    implementation("io.ktor:ktor-client-okhttp:2.1.1")
    implementation("org.jsoup:jsoup:1.15.3")
    implementation("org.slf4j:slf4j-simple:2.0.4")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    implementation("org.junit.jupiter:junit-jupiter:5.9.0")
    implementation("commons-validator:commons-validator:1.7")
    implementation("ru.morpher:ws3.client:1.0-SNAPSHOT")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.1")
    implementation("com.ibm.icu:icu4j:72.1")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation("io.github.classgraph:classgraph:4.8.151")
    implementation("org.jruby:jruby-complete:9.4.2.0")
    compileOnly("io.insert-koin:koin-annotations:1.0.3")
    ksp("io.insert-koin:koin-ksp-compiler:1.0.3")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

detekt {
    buildUponDefaultConfig = true
}

application {
    mainClass.set("MainKt")
}
