package com.vikram.oAuthSpring

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
class UserController {

    @GetMapping("/user")
    fun user(@AuthenticationPrincipal user: OAuth2User): Map<String,Any?>{
        return mapOf("name" to user.attributes.get("name"))
    }

   @GetMapping("/error")
   @ResponseBody
   fun onError(request: HttpServletRequest): String?{
       println("The session data is ${request.session.getAttribute("error.message")}")
       val attribute: String?  = request.session?.getAttribute("error.message") as? String
       request.session.removeAttribute("error.message")
       return attribute;
   }
}