package io.github.whiskeysierra.archunit.hexagonal

import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition
import com.tngtech.archunit.library.dependencies.syntax.GivenSlices

fun SlicesRuleDefinition.Creator.matching(
    vararg packageIdentifiers: String
): GivenSlices =
    assignedFrom(MultiPackageMatchingSliceIdentifier(*packageIdentifiers))
