plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.netty:netty-all:4.2.0.Final")
    implementation("org.luaj:luaj-jse:3.0.1")
    implementation("com.google.jimfs:jimfs:1.3.0")
    implementation("com.google.code.gson:gson:2.13.1")
}