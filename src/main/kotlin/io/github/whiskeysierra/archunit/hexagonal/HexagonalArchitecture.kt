package io.github.whiskeysierra.archunit.hexagonal

import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.library.Architectures
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.Creator
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

    private val driverAdapters =
        slices()
            .matching(
                "..application.(*).*..",
                "..domain.api.(*)..",
                "..domain.logic.(*)..",
            )
            .namingSlices("$1")

    @ArchTest
    val driverAdapterCycles: ArchRule =
        driverAdapters.should().beFreeOfCycles()

    @ArchTest
    val driverAdapterDependencies: ArchRule =
        driverAdapters.should().notDependOnEachOther()

    private val domainApplicationProgrammingInterfaces =
        slices()
            .matching("..domain.api.(*)..")
            .namingSlices("$1")

    @ArchTest
    val domainApplicationProgrammingInterfacesCycles: ArchRule =
        domainApplicationProgrammingInterfaces.should().beFreeOfCycles()

    @ArchTest
    val domainApplicationProgrammingInterfacesDependencies: ArchRule =
        domainApplicationProgrammingInterfaces.should().notDependOnEachOther()

    private val domainLogic =
        slices()
            .matching("..domain.logic.(*).(*)..")
            .namingSlices("$1.$2")

    @ArchTest
    val domainLogicCycles: ArchRule =
        domainLogic.should().beFreeOfCycles()

    @ArchTest
    val domainLogicDependencies: ArchRule =
        domainLogic.should().notDependOnEachOther()

    private val domainServiceProviderInterfaces =
        slices()
            .matching("..domain.spi.(*)..")
            .namingSlices("$1")

    @ArchTest
    val domainServiceProviderInterfaceCycles: ArchRule =
        domainServiceProviderInterfaces.should().beFreeOfCycles()

    @ArchTest
    val domainServiceProviderInterfaceDependencies: ArchRule =
        domainServiceProviderInterfaces.should().notDependOnEachOther()

    private val drivenAdapters =
        slices()
            .matching(
                "..domain.spi.(*)..",
                "..infrastructure.(*).*..",
            )
            .namingSlices("$1")

    @ArchTest
    val drivenAdapterCycles: ArchRule =
        drivenAdapters.should().beFreeOfCycles()

    @ArchTest
    val drivenAdapterDependencies: ArchRule =
        drivenAdapters.should().notDependOnEachOther()

    private fun Creator.matching(vararg packageIdentifiers: String) =
        assignedFrom(MultiPackageMatchingSliceIdentifier(*packageIdentifiers))
}
