package io.github.whiskeysierra.archunit.hexagonal

import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.library.Architectures
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices

object HexagonalArchitecture {

    @ArchTest
    val packages: ArchRule =
        Architectures.layeredArchitecture()
            .layer("application").definedBy("..application..")
            .layer("domain.api").definedBy("..domain.api..")
            .layer("domain.spi").definedBy("..domain.spi..")
            .layer("domain.model").definedBy("..domain.model..")
            .layer("domain.logic").definedBy("..domain.logic..")
            .layer("infrastructure").definedBy("..infrastructure..")
            .whereLayer("application").mayNotBeAccessedByAnyLayer()
            .whereLayer("domain.api").mayOnlyBeAccessedByLayers("domain.logic", "application")
            .whereLayer("domain.spi").mayOnlyBeAccessedByLayers("domain.logic", "infrastructure")
            .whereLayer("domain.model").mayOnlyBeAccessedByLayers("domain.api", "domain.spi", "domain.logic", "application", "infrastructure")
            .whereLayer("domain.logic").mayNotBeAccessedByAnyLayer()
            .whereLayer("infrastructure").mayNotBeAccessedByAnyLayer()

    @ArchTest
    val driverAdapters: ArchRule =
        slices()
            .matching("..application.(*).(*)..")
            .namingSlices("$1.$2")
            .should().notDependOnEachOther()

    @ArchTest
    val domainApis: ArchRule =
        slices()
            .matching(
                "..application.(*)..",
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
                "..domain.spi.(*)..",
                "..infrastructure.(*)..",
            )
            .namingSlices("$1")
            .should().notDependOnEachOther()

    @ArchTest
    val drivenAdapters: ArchRule =
        slices()
            .matching("..infrastructure.(*).(*)..")
            .namingSlices("$1.$2")
            .should().notDependOnEachOther()
}
