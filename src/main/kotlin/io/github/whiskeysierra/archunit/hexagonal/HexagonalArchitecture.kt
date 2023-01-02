package io.github.whiskeysierra.archunit.hexagonal

import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.library.Architectures
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices

object HexagonalArchitecture {

    @ArchTest
    val packages: ArchRule =
        Architectures.layeredArchitecture()
            .consideringOnlyDependenciesInLayers()
            .optionalLayer("adapters").definedBy("..adapters..")
            .optionalLayer("application").definedBy("..application..")
            .optionalLayer("domain.api").definedBy("..domain.api..")
            .optionalLayer("domain.spi").definedBy("..domain.spi..")
            .layer("domain.model").definedBy("..domain.model..")
            .layer("domain.logic").definedBy("..domain.logic..")
            .optionalLayer("infrastructure").definedBy("..infrastructure..")
            .whereLayer("application").mayNotBeAccessedByAnyLayer()
            .whereLayer("domain.api").mayOnlyBeAccessedByLayers("domain.logic", "adapters", "application")
            .whereLayer("domain.spi").mayOnlyBeAccessedByLayers("domain.logic", "adapters", "infrastructure")
            .whereLayer("domain.model").mayOnlyBeAccessedByLayers(
                "domain.api",
                "domain.spi",
                "domain.logic",
                "adapters",
                "application",
                "infrastructure",
            )
            .whereLayer("domain.logic").mayNotBeAccessedByAnyLayer()
            .whereLayer("infrastructure").mayNotBeAccessedByAnyLayer()

    @ArchTest
    val driverAdapters: ArchRule =
        slices()
            .matching(
                "..adapters.(*).(*)..",
                "..application.(*).(*)..",
            )
            .namingSlices("$1.$2")
            .should().notDependOnEachOther()

    @ArchTest
    val domainApis: ArchRule =
        slices()
            .matching(
                "..domain.api.(*)..",
                "..domain.logic.(*)..",
            )
            .namingSlices("$1")
            .should().notDependOnEachOther()

    @ArchTest
    val domainLogics: ArchRule =
        slices()
            .matching("..domain.logic.(*)..")
            .namingSlices("$1")
            .should().notDependOnEachOther()

    @ArchTest
    val domainSpis: ArchRule =
        slices()
            .matching(
                "..adapters.(*)..",
                "..domain.spi.(*)..",
                "..infrastructure.(*)..",
            )
            .namingSlices("$1")
            .should().notDependOnEachOther()

    @ArchTest
    val drivenAdapters: ArchRule =
        slices()
            .matching(
                "..adapters.(*).(*)..",
                "..infrastructure.(*).(*)..",
            )
            .namingSlices("$1.$2")
            .should().notDependOnEachOther()
}
