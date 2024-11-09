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

repositories {
    // Add repositories to retrieve artifacts from in here.
    // You should only use this when depending on other mods because
    // Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
    // See https://docs.gradle.org/current/userguide/declaring_repositories.html
    // for more information about repositories.
}

loom {
    splitEnvironmentSourceSets()

    mods {
        create("robandpeace") {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets.getByName("client"))
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
