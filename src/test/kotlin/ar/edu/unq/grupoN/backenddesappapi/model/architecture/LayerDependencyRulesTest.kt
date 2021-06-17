package ar.edu.unq.grupoN.backenddesappapi.model.architecture

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields
import org.junit.jupiter.api.Test


class LayerDependencyRulesTest {
    private val classes = ClassFileImporter().withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importClasspath()

    @Test
    fun servicesShouldNotAccessControllers() {
        ArchRuleDefinition.noClasses().that().resideInAPackage("..service..")
            .should().accessClassesThat().resideInAPackage("..controller..").check(classes)
    }

    @Test
    fun repositoryShouldNotAccessServices() {
        ArchRuleDefinition.noClasses().that().resideInAPackage("..repository..")
            .should().accessClassesThat().resideInAPackage("..service..").check(classes)
    }

    @Test
    fun servicesShouldNotDependOnControllers() {
        ArchRuleDefinition.noClasses().that().resideInAPackage("..service..")
            .should().dependOnClassesThat().resideInAPackage("..controller..").check(classes)
    }

    @Test
    fun repositoryShouldNotDependOnServices() {
        ArchRuleDefinition.noClasses().that().resideInAPackage("..repository..")
            .should().dependOnClassesThat().resideInAPackage("..service..").check(classes)
    }
}