plugins {
    application
}

group = "tech.zone84.examples"
version = "1.0-SNAPSHOT"

application.mainClass.set("tech.zone84.examples.barriersdisruptor.DisruptorApp")

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.toVersion("17")
    targetCompatibility = JavaVersion.toVersion("17")
}


dependencies {
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("com.lmax:disruptor:4.0.0.RC1")
    runtimeOnly("ch.qos.logback:logback-classic:1.2.10")
}
