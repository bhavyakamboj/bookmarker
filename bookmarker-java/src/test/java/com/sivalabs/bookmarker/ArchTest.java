package com.sivalabs.bookmarker;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

class ArchTest {

    JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.sivalabs.bookmarker");

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        noClasses().that().resideInAnyPackage("com.sivalabs.bookmarker.domain.service..").or()
                .resideInAnyPackage("com.sivalabs.bookmarker.domain.repository..").should().dependOnClassesThat()
                .resideInAnyPackage("com.sivalabs.bookmarker.web..")
                .because("Services and repositories should not depend on web layer").check(importedClasses);

    }

    @Test
    void shouldNotUseFieldInjection() {
        noFields().should().beAnnotatedWith(Autowired.class).check(importedClasses);
    }

    @Test
    void shouldFollowLayeredArchitecture() {
        layeredArchitecture()
                .layer("Config").definedBy("..config..")
                .layer("Web").definedBy("..web..")
                .layer("Service").definedBy("..service..")
                .layer("Persistence").definedBy("..repository..")

                .whereLayer("Web").mayNotBeAccessedByAnyLayer()
                .whereLayer("Service").mayOnlyBeAccessedByLayers("Config", "Web")
                .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Service")

                .check(importedClasses);
    }

    @Test
    void shouldFollowNamingConvention() {
        classes().that().resideInAPackage("com.sivalabs.bookmarker.domain.repository").should()
                .haveSimpleNameEndingWith("Repository").check(importedClasses);

        classes().that().resideInAPackage("com.sivalabs.bookmarker.domain.service").should()
                .haveSimpleNameEndingWith("Service").check(importedClasses);
    }

}
