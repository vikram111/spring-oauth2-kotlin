package com.vikram.oAuthSpring
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@SpringBootApplication
class OAuthSpringApplication

@EnableWebSecurity
class OauthSecConfig: WebSecurityConfigurerAdapter() {
	@Throws(Exception::class)
	override fun configure(http: HttpSecurity) {
		val simpleUrlAuthenticationFailureHandler = SimpleUrlAuthenticationFailureHandler()
		http.authorizeRequests { a ->
			a
				.antMatchers("/", "error", "/webjars/**")
				.permitAll()
				.anyRequest()
				.authenticated()
		}.logout{ l -> l.logoutSuccessUrl("/").permitAll()}
			.csrf{c -> c.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())}
			.exceptionHandling { e ->
			e.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
		}.oauth2Login{e ->
			e.failureHandler{request, response, exception -> handleFailure(request, response, exception, simpleUrlAuthenticationFailureHandler)}
			}

	}
}

fun handleFailure(request: HttpServletRequest, response: HttpServletResponse, exception: AuthenticationException, handler: SimpleUrlAuthenticationFailureHandler){
	println("I was called")
	request.session.setAttribute("error.message", exception.message)
	handler.onAuthenticationFailure(request,response,exception)
}



	fun main(args: Array<String>) {
		runApplication<OAuthSpringApplication>(*args)
	}