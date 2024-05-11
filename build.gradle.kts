plugins {
    java
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    id("maven-publish")
}

group = "com.raymundo.librarium"
version = "0.0.2"
extra["jwtVersion"] = "0.12.5"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:${property("jwtVersion")}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${property("jwtVersion")}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${property("jwtVersion")}")
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/${System.getenv("GITHUB_REPOSITORY")}")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("plainJar") {
            groupId = group.toString()
            artifactId = rootProject.name
            version = version
            from(components["java"])
        }
    }
}