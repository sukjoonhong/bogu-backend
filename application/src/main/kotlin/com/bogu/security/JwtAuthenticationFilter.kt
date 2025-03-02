package com.bogu.security

import com.bogu.service.crud.MemberCrudService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val memberCrudService: MemberCrudService
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.getHeader(AUTH_HEADER)
            ?.takeIf { it.startsWith(BEARER_PREFIX) }
            ?.substringAfter(BEARER_PREFIX)
            ?.trim()

        if (!token.isNullOrBlank() && JwtUtil.validateToken(token)) {
            val authId = JwtUtil.extractAuthId(token) ?: return
            val member = memberCrudService.findByAuthId(authId)
            val authentication = UsernamePasswordAuthenticationToken(
                member, null, emptyList()
            )
            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(request, response)
    }

    companion object {
        const val AUTH_HEADER = "Authorization"
        const val BEARER_PREFIX = "Bearer "
    }
}