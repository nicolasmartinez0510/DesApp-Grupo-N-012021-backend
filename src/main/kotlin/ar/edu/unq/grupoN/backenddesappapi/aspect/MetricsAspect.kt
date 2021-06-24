package ar.edu.unq.grupoN.backenddesappapi.aspect

import ar.edu.unq.grupoN.backenddesappapi.model.Metric
import ar.edu.unq.grupoN.backenddesappapi.service.PlatformAdminService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.lang.RuntimeException
import javax.servlet.http.HttpServletRequest


@Aspect
@Component
@Order(2)
class MetricsAspect {

    @Autowired
    lateinit var request: HttpServletRequest
    @Autowired
    lateinit var platformAdminService: PlatformAdminService


    @Around("@annotation(UserMetric)")
    fun userMetricsRegister(proceedingJoinPoint: ProceedingJoinPoint): Any {
        val apiKey : String? = request.getHeader("Authorization")?.replace("Api key ", "")

        val metric: Metric = findMetric(proceedingJoinPoint.signature.name)

        platformAdminService.addMetric(apiKey!!,metric)

        return proceedingJoinPoint.proceed()
    }

    private fun findMetric(methodName: String): Metric {
        return when(methodName.toUpperCase()){
            "ADDREVIEW" -> Metric.ADDREVIEW
            "REPORT" -> Metric.REPORT
            "RATE" -> Metric.RATE
            "SEARCH" -> Metric.SEARCH
            "CONTENTSEARCH" -> Metric.CONTENTSEARCH
            else -> throw RuntimeException("Invalid method name")
        }

    }

}


annotation class UserMetric