# archunit-hexagonal

Hexagonal Architecture rules for ArchUnit.

## Usage

To run the architecture tests in your repository do something along the following

```kotlin
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
```

## Modes

The following package structures are supported:

### Default

In the default setup, you are expected to have:
* 
* `application`, the driver/primary/left side
    * `x` (for exposing `domain.api.x`)
        * e.g. `grpc`
        * e.g. `http`
        * e.g. `management`
        * ...
* `domain`
    * `api`
        * `x`
    * `model`
    * `logic`
    * `spi`
        * `y`
* `infrastructure`, the driven/secondary/right side
    * `y` (for implementing `domain.spi.y`)
        * e.g. `memory`
        * e.g. `postgres`
        * e.g. `redis`
        * ...

### Unified Adapters

Alternatively, you can merge `application` and `infrastructure` into `adapters`:

* `adapters`
    * `x`
        * e.g. `grpc`
        * e.g. `http`
        * e.g. `management`
        * ...
    * `y`
        * e.g. `memory`
        * e.g. `postgres`
        * e.g. `redis`
        * ...
* `domain`
    * `api`
        * `x`
    * `model`
    * `logic`
    * `spi`
        * `y`

### Hybrid

You can also use both approaches at the same time.
That gives you a hybrid mix of the [Default](#default) and [Unified Adapters](#unified-adapters) approaches.

* `adapters`
    * `z` (for exposing `domain.api.z` **and** implementing `domain.spi.z`)
      * e.g. `prometheus`
      * ...
* `application`
    * `x`
        * e.g. `grpc`
        * e.g. `http`
        * e.g. `management`
        * ...
* `domain`
    * `api`
        * `x`
        * `z`
    * `model`
    * `logic`
    * `spi`
        * `y`
        * `z`
* `infrastructure`
    * `y`
        * e.g. `memory`
        * e.g. `postgres`
        * e.g. `redis`
        * ...
