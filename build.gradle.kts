import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ktlint.gradle)
}

val mod_version: String by project
val maven_group: String by project

version = mod_version
group = maven_group

val archives_base_name: String by project
base {
    archivesName = archives_base_name
}

val gameTest = "gametest"

sourceSets {
    val main by this
    create(gameTest) {
        compileClasspath += main.compileClasspath
        compileClasspath += main.output
        runtimeClasspath += main.runtimeClasspath
        runtimeClasspath += main.output
    }
}
val gameTestSourceSet = sourceSets.getByName(gameTest)

configurations {
    val testImplementation by this
    getByName("${gameTest}Implementation") {
        extendsFrom(testImplementation)
        exclude("org.slf4j", "slf4j-simple")
    }
}

repositories {
    // Add repositories to retrieve artifacts from in here.
    // You should only use this when depending on other mods because
    // Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
    // See https://docs.gradle.org/current/userguide/declaring_repositories.html
    // for more information about repositories.
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://maven.shedaniel.me/") }
    maven { url = uri("https://maven.terraformersmc.com/releases/") }
    maven {
        name = "Terraformers"
        url = uri("https://maven.terraformersmc.com/")
    }
    maven {
        name = "Ladysnake Mods"
        url = uri("https://maven.ladysnake.org/releases")
    }
    exclusiveContent {
        forRepository {
            maven {
                name = "Modrinth"
                url = uri("https://api.modrinth.com/maven")
            }
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }

    maven { url = uri("https://maven.florens.be/releases") }
}

loom {
    splitEnvironmentSourceSets()
    log4jConfigs.setFrom(file("log4j2.xml"))

    mods {
        create("robandpeace") {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets.getByName("client"))
        }
    }

    runs {
        create("gametest") {
            server()
            configName = name
            vmArgs += "-Dfabric-api.gametest"
            vmArgs += "-Dfabric-api.gametest.report-file=${project.layout.buildDirectory.get()}/$name/junit.xml"
            runDir = "build/$name"
            setSource(gameTestSourceSet)
            isIdeConfigGenerated = true
        }
        create("manualGameTest") {
            server()
            configName = "Manual Game Test"
            vmArgs += "-Dfabric-api.gametest.command=true"
            runDir = "build/$name"
            setSource(gameTestSourceSet)
            isIdeConfigGenerated = true
        }
    }
}

fabricApi {
    configureDataGeneration()
}

dependencies {
    // To change the versions see the gradle.properties file
    minecraft(libs.minecraft)
    mappings("net.fabricmc:yarn:${libs.versions.yarn.get()}:v2")
    modImplementation(libs.bundles.fabric)
    modApi(libs.cloth.config) {
        exclude(group = "net.fabricmc.fabric-api")
    }
    modImplementation(libs.modmenu)
    modImplementation(libs.immersiveportals)

    modRuntimeOnly(libs.sodium)
    modRuntimeOnly(libs.jei)
    modRuntimeOnly(libs.cardinal.components.base)
    modRuntimeOnly(libs.cardinal.components.world)
    modRuntimeOnly(libs.cardinal.components.entity)

    // Artifacts
    // modRuntimeOnly(libs.architectury.api)
    // modRuntimeOnly(libs.trinkets)
    // modRuntimeOnly(libs.night.config)
    // modRuntimeOnly(libs.night.config.toml)
    // modRuntimeOnly(libs.expandability)
    // modRuntimeOnly(libs.artifacts)

    testImplementation("org.slf4j:slf4j-api:2.0.16")
    testImplementation("org.slf4j:slf4j-simple:2.0.16")
    testImplementation(kotlin("test"))
    testImplementation(libs.bundles.kotest)
}

tasks.getByName<ProcessResources>("processResources") {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 21
}

java {
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.base.archivesName.get()}" }
    }
}

ktlint {
    version.set(libs.versions.ktlint.asProvider())
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks {
    create("runIntegration") {
        dependsOn(":runClient")
    }
}

// configure the maven publication
// publishing {
// 	publications {
// 		create("mavenJava", MavenPublication) {
// 			artifactId = project.base.archivesName.get()
// 			from(components.java)
// 		}
// 	}
//
// 	// See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
// 	repositories {
// 		// Add repositories to publish to here.
// 		// Notice: This block does NOT have the same function as the block in the top level.
// 		// The repositories here will be used for publishing your artifact, not for
// 		// retrieving dependencies.
// 	}
// }
