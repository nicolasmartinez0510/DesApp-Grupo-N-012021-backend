package ar.edu.unq.grupoN.backenddesappapi.aspect

import ar.edu.unq.grupoN.backenddesappapi.service.PlatformAdminService
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Aspect
@Component
@Order(1)
class AuthorizationCheckerAspect {

    @Autowired
    lateinit var request: HttpServletRequest
    @Autowired
    lateinit var respond: HttpServletResponse
    @Autowired
    lateinit var adminService: PlatformAdminService

    @Before("@annotation(Authorize)")
    fun validateApiKey(){
        val apiKey : String? = request.getHeader("Authorization")?.replace("Api key ", "")

        if(apiKey == null || !adminService.existByApiKey(apiKey)){
            respond.sendError(401,"Invalid Api key.")
        }

    }
}

annotation class Authorize