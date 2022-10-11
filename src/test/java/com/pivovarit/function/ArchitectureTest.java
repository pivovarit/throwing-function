package com.pivovarit.function;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

class ArchitectureTest {

    private static final ArchRule SINGLE_PACKAGE_RULE = classes()
      .should().resideInAPackage("com.pivovarit.function");

    private static final ArchRule PUBLIC_INTERFACES = classes()
      .that().arePublic().and().areInterfaces()
      .should().beInterfaces()
      .andShould().haveSimpleNameStartingWith("Throwing");

    private static final ArchRule ZERO_DEPS_RULE = classes()
      .that().resideInAPackage("com.pivovarit.function")
      .should()
      .dependOnClassesThat().resideInAPackage("java..")
      .as("the library should depend only on core Java classes")
      .because("users appreciate not experiencing a dependency hell");

    private static final JavaClasses classes = new ClassFileImporter()
      .importPackages("com.pivovarit");

    @Test
    void shouldHavePublicInterfacesWithCorrectNamingPattern() {
        PUBLIC_INTERFACES.check(classes);
    }

    @Test
    void shouldHaveZeroDependencies() {
        ZERO_DEPS_RULE.check(classes);
    }

    @Test
    void shouldHaveSinglePackage() {
        SINGLE_PACKAGE_RULE.check(classes);
    }
}
