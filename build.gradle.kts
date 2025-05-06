import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    kotlin("jvm") version "2.0.20"
    id("jacoco")
    id("org.jlleitschuh.gradle.ktlint") version "12.2.0"
}

group = "org.buinos"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.insert-koin:koin-core:4.0.4")
    implementation("com.jsoizo:kotlin-csv-jvm:1.10.0") // for JVM platform

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.12.2")
    testImplementation("io.mockk:mockk:1.14.0")
    testImplementation("com.google.truth:truth:1.4.4")
    implementation(kotlin("reflect"))
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
    }
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
        csv.required = true
    }
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        classDirectories.setFrom(
            classDirectories.files.map {
                fileTree(it) {
                    exclude("**/di/**")
                    exclude("**/entities/**")
                }
            },
        )
        rule {
            limit {
                minimum = "0.8".toBigDecimal()
            }

            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.8".toBigDecimal()
            }
            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "0.8".toBigDecimal()
            }
            limit {
                counter = "METHOD"
                value = "COVEREDRATIO"
                minimum = "0.8".toBigDecimal()
            }
        }
    }
}

jacoco {
    toolVersion = "0.8.13"
}

kotlin {
    jvmToolchain(21)
}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    debug.set(true)
    verbose.set(true)
    android.set(false)
    outputToConsole.set(true)
    outputColorName.set("RED")
    ignoreFailures.set(true) //FIXME
    enableExperimentalRules.set(true)
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }

    reporters {
        reporter(ReporterType.HTML)
    }
}
