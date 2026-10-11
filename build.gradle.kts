plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.sx)

    id("dev.kikugie.stonecutter")
    id("dev.kikugie.postprocess.j52j") version "2.1-beta.8"
    alias(libs.plugins.modstitch)
    id("me.modmuss50.mod-publish-plugin")

    alias(libs.plugins.indraGit)

    alias(libs.plugins.spotless)
}

fun prop(name: String, consumer: (prop: String) -> Unit) {
    (findProperty(name) as? String?)
        ?.let(consumer)
}

val productionMods: Configuration by configurations.creating {
    isTransitive = false
}
val loader = when {
    modstitch.isLoom -> "fabric"
    modstitch.isModDevGradleRegular -> "neoforge"
    else -> throw IllegalStateException("unsupported loader")
}
val mcVersion = property("deps.minecraft") as String;

if (loader == "fabric") {
    @Suppress("UnstableApiUsage")
    val runProdClient by tasks.registering(net.fabricmc.loom.task.prod.ClientProductionRunTask::class) {
        group = "fabric"

        mods.from(productionMods)

        outputs.upToDateWhen { false }
    }
} else {
    val runProdClient by tasks.registering {
        group = "replanter/versioned"
        dependsOn("runClient")
    }
}
createActiveTask(taskName = "runClient")
createActiveTask(taskName = "runProdClient")

// Stonecutter constants for mod loaders.
// See https://stonecutter.kikugie.dev/stonecutter/guide/comments#condition-constants
stonecutter {
    constants {
        val loader: String = current.project.substringAfter('-')
        match(loader, "fabric", "neoforge")
    }
}

modstitch {
    minecraftVersion = mcVersion

    javaTarget = if (stonecutter.eval(mcVersion, ">1.20.4")) 21 else 17

    // If parchment doesnt exist for a version yet you can safely
    // omit the "deps.parchment" property from your versioned gradle.properties
    parchment {
        prop("deps.parchment") { mappingsVersion = it }
    }

    // This metadata is used to fill out the information inside
    // the metadata files found in the templates folder.
    metadata {
        modId = "replanter"
        modName = "Replanter"
        modVersion = "${property("modVersion") as String}+${mcVersion}-${loader}"
        modGroup = "xyz.imide"
        modAuthor = "imide"
        modLicense = "AGPLv3"

        fun <K, V> MapProperty<K, V>.populate(block: MapProperty<K, V>.() -> Unit) {
            block()
        }

        replacementProperties.populate {
            put("pack_format", when (mcVersion) {
                "1.21.1" -> 34
                "1.21.3" -> 42
                "1.21.4" -> 46
                "1.21.5" -> 55
                "1.21.6" -> 63
                else -> throw IllegalArgumentException("Please store the resource pack version for ${property("deps.minecraft")} in build.gradle.kts! https://minecraft.wiki/w/Pack_format")
            }.toString())
            put("target_minecraft", property("mod.target") as String)
            put("loader", loader)
            put("flk", "${property("deps.flk") as String}+kotlin.${libs.versions.kotlin.get()}")
            put("rconfig", property("deps.rconfig") as String)
        }
    }

    // Fabric Loom (Fabric)
    loom {
        // It's not recommended to store the Fabric Loader version in properties.
        // Make sure its up to date.
        fabricLoaderVersion = "0.16.14"

        // Configure loom like normal in this block.
        configureLoom {

            runs {
                all {
                    runDir = "../../run"
                    ideConfigGenerated(true)
                }
            }
        }
    }

    // ModDevGradle (NeoForge, Forge, Forgelike)
    moddevgradle {
        enable {
            neoForgeVersion = findProperty("deps.neoforge") as String
        }

        // Configures client and server runs for MDG, it is not done by default
        defaultRuns()

        // This block configures the `neoforge` extension that MDG exposes by default,
        // you can configure MDG like normal from here
        configureNeoforge {
            validateAccessTransformers = true
            runs.all {
                disableIdeRun()
            }
        }
    }

    mixin {
        // You do not need to specify mixins in any mods.json/toml file if this is set to
        // true, it will automatically be generated.
        addMixinsToModManifest = true

//         configs.register("replanter")

        // Most of the time you wont ever need loader specific mixins.
        // If you do, simply make the mixin file and add it like so for the respective loader:
        // if (isLoom) configs.register("examplemod-fabric")
        // if (isModDevGradleRegular) configs.register("examplemod-neoforge")
        // if (isModDevGradleLegacy) configs.register("examplemod-forge")
    }
}

// All dependencies should be specified through modstitch's proxy configuration.
// Wondering where the "repositories" block is? Go to "stonecutter.gradle.kts"
// If you want to create proxy configurations for more source sets, such as client source sets,
// use the modstitch.createProxyConfigurations(sourceSets["client"]) function.
dependencies {
    fun Dependency?.jij() = this?.also(::modstitchJiJ)
    fun Dependency?.productionMod() = this?.also { productionMods(it) }

    modstitch.loom {
        modstitchModCompileOnly("net.fabricmc.fabric-api:fabric-api:${property("deps.fapi")}").productionMod()
        modstitchModCompileOnly("maven.modrinth:modmenu:${property("deps.mod_menu")}").productionMod()
        modstitchModImplementation("net.fabricmc:fabric-language-kotlin:${property("deps.flk")}+kotlin.${libs.versions.kotlin.get()}").productionMod()
        modstitchImplementation(annotationProcessor("io.github.llamalad7:mixinextras-fabric:${property("deps.mixinExtras")}")!!).jij()
    }

    modstitch.moddevgradle {
        implementation("io.github.llamalad7:mixinextras-neoforge:${property("deps.mixinExtras")}").jij()
        modstitchModImplementation("maven.modrinth:kotlin-for-forge:5.9.0").productionMod()

    }
//    if (property("deps.minecraft") == "1.21.1") {
//        modstitchModCompileOnly("maven.modrinth:resourceful-config-${loader}-1.21:${property("deps.rconfig")}").jij().productionMod()
//    } else {
        modstitchModCompileOnly("maven.modrinth:resourceful-config:${property("deps.rconfig")}").productionMod()
//    }
}

// Language settings

java {
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    jvmToolchain(21)
}

tasks {
    withType<JavaCompile> {
        options.release = 21
    }

    publishMods {
        from(rootProject.publishMods)
        dryRun = rootProject.publishMods.dryRun

        val modVersion: String by project
        version = modVersion

        val modChangelog = provider {
            rootProject.file("CHANGELOG.md")
                .takeIf { it.exists() }
                ?.readText()
                ?.replace("{version}", modVersion)
                ?.replace(
                    "{targets}", stonecutter.versions
                        .map { it.project }
                        .joinToString(separator = "\n") { "- $it"})
                ?: "no changelog provided."
        }

        changelog.set(modChangelog)

        type.set(
            when {
                "alpha" in modVersion -> ALPHA
                "beta" in modVersion -> BETA
                else -> STABLE
            }
        )

        file = modstitch.finalJarTask.flatMap { it.archiveFile }

        displayName = "$modVersion for $loader $mcVersion"
        modLoaders.add(loader)

        fun versionList(prop: String) = findProperty(prop)?.toString()
            ?.split(',')
            ?.map { it.trim() }
            ?: emptyList()

        val stableMCVersions = versionList("pub.stable")
        val modrinthId: String by project

        if (modrinthId.isNotBlank() && hasProperty("modrinthToken")) {
            modrinth {
                projectId.set(modrinthId)
                accessToken.set(findProperty("modrinthToken")?.toString())
                minecraftVersions.addAll(stableMCVersions)

                requires { slug.set("resourceful-config") }

                if (loader == "fabric") {
                    requires { slug.set("fabric-language-kotlin") }
                    requires { slug.set("fabric-api") }
                    optional { slug.set("modmenu") }
                } else {
                    requires { slug.set("kotlin-for-forge") }
                }
            }
        }
    }

    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        val overrides = mapOf(
            "ktlint_standard_filename" to "disabled",
            "ktlint_standard_trailing-comma-on-call-site" to "disabled",
            "ktlint_standard_trailing-comma-on-declaration-site" to "disabled",
        )
        kotlin {
            ktlint(libs.versions.ktlint.get()).editorConfigOverride(overrides)
            target("src/*/kotlin/**/*.kt")
            licenseHeaderFile(rootProject.file("LICENSE-HEADER"))
        }
        kotlinGradle {
            ktlint(libs.versions.ktlint.get()).editorConfigOverride(overrides)
        }
    }
}

val releaseModVersion by tasks.registering {
    group = "replanter/versioned"

    dependsOn("publishMods")
}
createActiveTask(releaseModVersion)

val finalJarTasks = listOf(
    modstitch.finalJarTask
)
val buildAndCollect by tasks.registering(Copy::class) {
    group = "replanter/versioned"

    finalJarTasks.forEach { jar ->
        dependsOn(jar)
        from(jar.flatMap { it.archiveFile })
    }

    into(rootProject.layout.buildDirectory.dir("finalJars"))
}
createActiveTask(buildAndCollect)

fun createActiveTask(
    taskProvider: TaskProvider<*>? = null,
    taskName: String? = null,
    internal: Boolean = false
): String {
    val taskExists = taskProvider != null || taskName!! in tasks.names
    val task = taskProvider ?: taskName?.takeIf { taskExists }?.let { tasks.named(it) }
    val taskName = when {
        taskProvider != null -> taskProvider.name
        taskName != null -> taskName
        else -> error("Either taskProvider or taskName must be provided")
    }
    val activeTaskName = "${taskName}Active"

    if (stonecutter.current.isActive) {
        rootProject.tasks.register(activeTaskName) {
            group = "replanter${if (internal) "/versioned" else ""}"

            task?.let { dependsOn(it) }
        }
    }

    return activeTaskName
}