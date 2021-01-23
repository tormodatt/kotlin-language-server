package org.javacs.kt.classpath

import org.javacs.kt.LOG
import java.nio.file.Path
import java.nio.file.PathMatcher
import java.nio.file.FileSystems
import java.nio.file.Paths

fun defaultClassPathResolver(workspaceRoots: Collection<Path>): ClassPathResolver =
    WithStdlibResolver(
        ShellClassPathResolver.global(workspaceRoots.firstOrNull())
            .or(workspaceRoots.asSequence().flatMap(::workspaceResolvers).joined)
    ).or(BackupClassPathResolver)

/** Searches the workspace for all files that could provide classpath info. */
private fun workspaceResolvers(workspaceRoot: Path): Sequence<ClassPathResolver> {
    val ignored: List<PathMatcher> = ignoredPathPatterns(workspaceRoot, workspaceRoot.resolve(".gitignore"))
    println(ignored.map {  })

    val res = ignored.none { it.matches(workspaceRoot.relativize(Paths.get("/Users/h804565/eika/dagligbank/tietoevry-era-esb/era-api-definitions/docs/accounts/.openapi-generator/"))) }
//    val match = ignored.none { it.matches(workspaceRoot.relativize(Paths.get("/Users/h804565/eika/dagligbank/tietoevry-era-esb/era-api-definitions/docs/accounts/.openapi-generator"))) }
    val folders = folderResolvers(workspaceRoot, ignored)
//    println(folders.map { it.classpath })
    return folders.asSequence()
}

/** Searches the folder for all build-files. */
private fun folderResolvers(root: Path, ignored: List<PathMatcher>): Collection<ClassPathResolver> =
    root.toFile()
        .walk()
        .onEnter { file -> ignored.none { it.matches(file.toPath()) } }
        .filter { file -> ignored.none { it.matches(Paths.get(file.absolutePath)) }}
        .filter { it.extension == "xml" }
        .mapNotNull { asClassPathProvider(it.toPath()) }
        .toList()

/** Tries to read glob patterns from a gitignore. */
private fun ignoredPathPatterns(root: Path, gitIgnore: Path): List<PathMatcher> =
    gitIgnore.toFile()
        .takeIf { it.exists() }
        ?.readLines()
        ?.map { it.trim() }
        ?.filter { it.isNotEmpty() && !it.startsWith("#") }
        ?.map { it.removeSuffix("/") }
        ?.let { it + listOf(
            // Patterns that are ignored by default
            ".git"
        ) }
        ?.mapNotNull { try {
            val pattern = "glob:$root**/$it"
            println(pattern)
            LOG.debug("Adding ignore pattern '{}' from {}", it, gitIgnore)
            FileSystems.getDefault().getPathMatcher(pattern)
        } catch (e: Exception) {
            LOG.warn("Did not recognize gitignore pattern: '{}' ({})", it, e.message)
            null
        } }
        ?: emptyList<PathMatcher>()

/** Tries to create a classpath resolver from a file using as many sources as possible */
private fun asClassPathProvider(path: Path): ClassPathResolver? {
    println(path)

    return MavenClassPathResolver.maybeCreate(path)
        ?: GradleClassPathResolver.maybeCreate(path)
        ?: ShellClassPathResolver.maybeCreate(path)
}
