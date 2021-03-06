package liveplugin.pluginrunner.kotlin

import liveplugin.LivePluginPaths
import java.io.File
import kotlin.script.experimental.api.*
import kotlin.script.experimental.jvm.JvmDependency
import kotlin.script.experimental.jvm.withUpdatedClasspath

object LivePluginScriptCompilationConfiguration: ScriptCompilationConfiguration(body = {
    val sources = File(LivePluginPaths.livePluginLibPath).listFiles().toList()
    val classpath = sources +
        File("${LivePluginPaths.ideJarsPath}/../plugins/Kotlin/lib/").listFiles() +
        File(LivePluginPaths.ideJarsPath).listFiles()

    refineConfiguration {
        beforeParsing { context ->
            ResultWithDiagnostics.Success(
                value = context.compilationConfiguration.withUpdatedClasspath(classpath),
                reports = emptyList()
            )
        }
        beforeCompiling { context ->
            ResultWithDiagnostics.Success(
                value = context.compilationConfiguration.withUpdatedClasspath(classpath),
                reports = emptyList()
            )
        }
    }
    ide {
        acceptedLocations(ScriptAcceptedLocation.Everywhere)
        dependenciesSources(JvmDependency(sources))
    }
})