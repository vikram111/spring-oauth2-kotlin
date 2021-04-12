package com.vikram.oAuthSpring
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.Exception

@SpringBootApplication
class OAuthSpringApplication

@EnableWebSecurity
class OauthSecConfig: WebSecurityConfigurerAdapter() {
	@Throws(Exception::class)
	override fun configure(http: HttpSecurity) {
		http.authorizeRequests { a ->
			a
				.antMatchers("/", "error", "/webjars/**")
				.permitAll()
				.anyRequest()
				.authenticated()
		}.exceptionHandling { e ->
			e.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
		}.oauth2Login()

	}
}


	fun main(args: Array<String>) {
		runApplication<OAuthSpringApplication>(*args)
	}