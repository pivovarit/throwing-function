package com.pivovarit.function;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "com.pivovarit", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

    private static final String COM_PIVOVARIT_FUNCTION = "com.pivovarit.function";

    @ArchTest
    static final ArchRule shouldHaveSinglePackage = classes()
      .should().resideInAPackage(COM_PIVOVARIT_FUNCTION);

    @ArchTest
    static final ArchRule shouldHaveOnlyPublicInterfaces = classes()
      .that().arePublic().and().areInterfaces()
      .should().beInterfaces()
      .andShould().haveSimpleNameStartingWith("Throwing");

    @ArchTest
    static final ArchRule shouldHaveZeroDependencies = classes()
      .that().resideInAPackage(COM_PIVOVARIT_FUNCTION)
      .should()
      .dependOnClassesThat().resideInAPackage("java..")
      .as("the library should depend only on core Java classes")
      .because("users appreciate not experiencing a dependency hell");
}
