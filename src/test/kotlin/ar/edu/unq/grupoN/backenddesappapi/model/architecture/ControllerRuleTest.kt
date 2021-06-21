package ar.edu.unq.grupoN.backenddesappapi.model.architecture

import com.tngtech.archunit.base.DescribedPredicate
import com.tngtech.archunit.base.PackageMatchers
import com.tngtech.archunit.core.domain.JavaClass.Functions.GET_PACKAGE_NAME
import com.tngtech.archunit.core.domain.JavaMember
import com.tngtech.archunit.core.domain.JavaMember.Predicates.declaredIn
import com.tngtech.archunit.core.domain.properties.CanBeAnnotated.Predicates.annotatedWith
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.conditions.ArchPredicates.are
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import org.springframework.security.access.annotation.Secured


@AnalyzeClasses(packages = ["ar.edu.unq.desapp.grupoK.backenddesappapi"])
object ControllerRulesTest {

    @ArchTest
    val controllersShouldOnlyCallSecuredMethods: ArchRule = classes().that().resideInAPackage("..controller..")
        .should().onlyCallMethodsThat(
            areDeclaredInController().or(
                are(
                    annotatedWith(
                        Secured::class.java
                    )
                )
            )
        )

    @ArchTest
    val controllersShouldOnlyCallSecuredConstructors: ArchRule = classes()
        .that().resideInAPackage("..controller..")
        .should().onlyCallConstructorsThat(
            areDeclaredInController().or(
                are(
                    annotatedWith(
                        Secured::class.java
                    )
                )
            )
        )

    @ArchTest
    val controllersShouldOnlyCallSecuredCodeUnits: ArchRule = classes()
        .that().resideInAPackage("..controller..")
        .should().onlyCallCodeUnitsThat(
            areDeclaredInController().or(
                are(
                    annotatedWith(
                        Secured::class.java
                    )
                )
            )
        )

    @ArchTest
    val controllersShouldOnlyAccessSecuredFields: ArchRule = classes()
        .that().resideInAPackage("..controller..")
        .should().onlyAccessFieldsThat(
            areDeclaredInController().or(
                are(
                    annotatedWith(
                        Secured::class.java
                    )
                )
            )
        )

    @ArchTest
    val controllersShouldOnlyAccessSecuredMembers: ArchRule = classes()
        .that().resideInAPackage("..controller..")
        .should().onlyAccessMembersThat(
            areDeclaredInController().or(
                are(
                    annotatedWith(
                        Secured::class.java
                    )
                )
            )
        )

    private fun areDeclaredInController(): DescribedPredicate<JavaMember> {
        val aPackageController = GET_PACKAGE_NAME.`is`(PackageMatchers.of("..controller..", "java.."))
            .`as`("a package '..controller..'")
        return are(declaredIn(aPackageController))
    }
}