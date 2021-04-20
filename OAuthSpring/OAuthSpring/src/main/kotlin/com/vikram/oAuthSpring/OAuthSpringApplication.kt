package com.vikram.oAuthSpring
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.security.web.csrf.CookieCsrfTokenRepository


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
			e.failureHandler{request, response, exception ->
				request.session.setAttribute("error.message", exception.message)
				simpleUrlAuthenticationFailureHandler.onAuthenticationFailure(request,response,exception)
			}
			}

	}
}




	fun main(args: Array<String>) {
		runApplication<OAuthSpringApplication>(*args)
	}