# archunit-hexagonal

Hexagonal Architecture rules for ArchUnit


## Usage

To run the architecture tests in your repository do something along the following

````
package my.package

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.junit.ArchTests
import io.github.whiskeysierra.archunit.hexagonal.HexagonalArchitecture
import com.tngtech.archunit.junit.ArchTests.`in` as from

// Standard archunit scope identifier. 
// See https://www.archunit.org/userguide/html/000_Index.html#_using_junit_support_with_kotlin
@AnalyzeClasses(
    packagesOf = [Application::class],
    importOptions = [ImportOption.DoNotIncludeTests::class]
)
object ArchitectureTest {
    @ArchTest
    val tests: ArchTests = from(HexagonalArchitecture::class.java)
}
