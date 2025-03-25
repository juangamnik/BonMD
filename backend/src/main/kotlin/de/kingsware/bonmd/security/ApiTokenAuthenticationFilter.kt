package de.kingsware.bonmd.security

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

/**
 * Filter that authenticates incoming HTTP requests based on a bearer token.
 *
 * The filter checks the 'Authorization' header for a valid bearer token and compares it
 * against a list of allowed tokens defined in the 'API_TOKENS' environment variable.
 * If the token is valid, the request is allowed to proceed; otherwise, a 401 error is returned.
 */
class ApiTokenAuthenticationFilter : OncePerRequestFilter() {

    /**
     * Filters each incoming HTTP request to validate the bearer token in the Authorization header.
     *
     * @param request the incoming HttpServletRequest
     * @param response the HttpServletResponse to potentially modify
     * @param filterChain the filter chain to continue processing the request if authorized
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs during filtering
     */
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Missing or invalid Authorization header")
            return
        }

        val token = authHeader.removePrefix("Bearer ").trim()

        // Read allowed tokens from the environment variable on each request
        val allowedTokens = System.getenv("API_TOKENS")?.split(",")?.map { it.trim() } ?: emptyList()

        if (!allowedTokens.contains(token)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid API token")
            return
        }

        // Token is valid – continue processing the request
        filterChain.doFilter(request, response)
    }
}