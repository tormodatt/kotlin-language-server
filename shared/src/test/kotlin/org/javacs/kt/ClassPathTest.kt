package org.javacs.kt

import org.javacs.kt.classpath.defaultClassPathResolver
import org.junit.Test
import java.nio.file.FileSystems
import java.nio.file.Paths
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.Path
import kotlin.io.path.name

class ClassPathTest {

    @ExperimentalPathApi
    @Test
    fun `asdf`() {
//        defaultClassPathResolver(listOf(Path("/tmp/asdf")))
//        defaultClassPathResolver(listOf(Path("/Users/h804565/eika/dagligbank/sdc-neos-esb")))
        defaultClassPathResolver(listOf(Path("/Users/h804565/eika/dagligbank/tietoevry-era-esb")))
    }

    @Test
    fun `asdf2`() {
        val path = Paths.get("/Users/h804565/eika/dagligbank/tietoevry-era-esb/era-api-definitions/docs/accounts/.openapi-generator/")
//        val pattern = "glob:/Users/h804565/eika/dagligbank/tietoevry-era-esb/era-api-definitions/docs/accounts/.openapi-generator"
//        val pattern = "glob:/Users/h804565/eika/dagligbank/tietoevry-era-esb/era-api-definitions/*/.openapi-generator"
//        val pattern = "glob:*/.openapi-generator"
//        val pattern = "glob:/*/.openapi-generator"
//        val pattern = "glob:/**/.openapi-generator"
        val pattern = "glob:/Users/h804565/eika/dagligbank/tietoevry-era-esb/era-api-definitions/**/.openapi-generator"
        val matcher = FileSystems.getDefault().getPathMatcher(pattern)
        val match = matcher.matches(path)
        println(match)

    }

    @Test
    fun `asdf3`() {
//        /Users/h804565/eika/dagligbank/tietoevry-era-esb/era-api-definitions/docs/payments/.openapi-generator
//        /Users/h804565/eika/dagligbank/tietoevry-era-esb/era-api-definitions/docs/payments/.openapi-generator/ERAPaymentsAPI_v_1.yaml.sha256
//        /Users/h804565/eika/dagligbank/tietoevry-era-esb/era-api-definitions/docs/payments/.openapi-generator/VERSION

        val path = Paths.get("/Users/h804565/eika/dagligbank/tietoevry-era-esb/era-api-definitions/docs/payments/.openapi-generator/VERSION")
        val pattern = "glob:/Users/h804565/eika/dagligbank/tietoevry-era-esb/**/.openapi-generator**"
        val battern2 = "glob:/Users/h804565/eika/dagligbank/tietoevry-era-esb/**/.openapi-generator-ignore"
        val matcher = FileSystems.getDefault().getPathMatcher(pattern)
        val match = matcher.matches(path)
        println(match)

    }

    @Test
    fun `asdf4`() {
        val root = Paths.get("/Users/h804565/eika/dagligbank/tietoevry-era-esb/era-api-definitions")
        val path = Paths.get("/Users/h804565/eika/dagligbank/tietoevry-era-esb/era-api-definitions/docs/payments/.openapi-generator")
        val pattern = "glob:/Users/h804565/eika/dagligbank/tietoevry-era-esb/**/.openapi-generator"
        val matcher = FileSystems.getDefault().getPathMatcher(pattern)
        val match = matcher.matches(path)
        println(match)

        val ignored = listOf(matcher)

        val stepInto = ignored.none { it.matches(root.relativize(path)) }
        println(stepInto)

    }

    @Test
    fun `asdf5`() {
        val path = Paths.get("/Users/h804565/eika/dagligbank/tietoevry-era-esb/era-api-definitions/docs/payments/.openapi-generator-ignore")
        val pattern = "glob:/Users/h804565/eika/dagligbank/tietoevry-era-esb/**/.openapi-generator-ignore"
        val matcher = FileSystems.getDefault().getPathMatcher(pattern)
        val match = matcher.matches(path)
        println(match)


    }

    @Test
    fun `asdf6`() {
        val path = Paths.get("/Users/h804565/eika/dagligbank/tietoevry-era-esb/.git")
        val pattern = "glob:/Users/h804565/eika/dagligbank/tietoevry-era-esb**/.git"
        val matcher = FileSystems.getDefault().getPathMatcher(pattern)
        val match = matcher.matches(path)
        println(match)


    }

    @Test
    fun `asdf0`() {
        val root = Paths.get("/Users/h804565/eika/standardisering/aldin-esb-ws")
        val classPathResolver = defaultClassPathResolver(listOf(root))
        val classPaths = classPathResolver.classpath
        println(classPaths)
        println(classPaths.size)
    }

}
