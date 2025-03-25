package de.kingsware.bonmd.config

import de.kingsware.bonmd.security.ApiTokenAuthenticationFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configuration class responsible for registering security-related filters.
 * 
 * This class sets up the {@link ApiTokenAuthenticationFilter} to protect API endpoints
 * by verifying incoming requests.
 */
@Configuration
class ApiSecurityConfig {

    /**
     * Registers the {@link ApiTokenAuthenticationFilter} for API endpoint protection.
     *
     * @return the configured FilterRegistrationBean
     */
    @Bean
    fun apiTokenFilterRegistrationBean(): FilterRegistrationBean<ApiTokenAuthenticationFilter> {
        val registrationBean = FilterRegistrationBean<ApiTokenAuthenticationFilter>()
        registrationBean.filter = ApiTokenAuthenticationFilter()
        // Apply filter only to API endpoints
        registrationBean.addUrlPatterns("/api/*")
        registrationBean.order = 1  // Optional: Sets the priority of the filter
        return registrationBean
    }
}