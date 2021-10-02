package io.github.whiskeysierra.archunit.hexagonal

import com.tngtech.archunit.base.PackageMatcher
import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.library.dependencies.SliceAssignment
import com.tngtech.archunit.library.dependencies.SliceIdentifier

internal class MultiPackageMatchingSliceIdentifier(
    private vararg val packageIdentifiers: String
) : SliceAssignment {

    override fun getIdentifierOf(javaClass: JavaClass): SliceIdentifier =
        packageIdentifiers
            .map {
                PackageMatcher.of(it)
                    .match(javaClass.packageName)
                    .map(PackageMatcher.TO_GROUPS)
                    .orElse(emptyList())
            }
            .filter { it.isNotEmpty() }
            .flatten()
            .let { parts -> identifierOf(parts) }

    private fun identifierOf(parts: List<String>?) =
        if (parts.isNullOrEmpty())
            SliceIdentifier.ignore() else
            SliceIdentifier.of(parts)

    override fun getDescription(): String {
        return packageIdentifiers.joinToString(", ") { "'$it'" }
    }
}
