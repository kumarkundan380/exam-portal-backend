package com.exam.security;
import com.exam.exception.ExamPortalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.exam.constants.ExamPortalConstant.AUTHORIZATION_HEADER;
import static com.exam.constants.ExamPortalConstant.AUTHORIZATION_PREFIX;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserDetailsService userDetailsService;
		
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String jwtToken = parseJwtToken(request);
		if(StringUtils.hasText(jwtToken)) {
			String userName = jwtUtil.getUserNameFromJwtToken(jwtToken);
			
			// Username should not be empty, context-auth must be empty
			if(StringUtils.hasText(userName) && SecurityContextHolder.getContext().getAuthentication()==null) {
				UserDetails user = userDetailsService.loadUserByUsername(userName);
				// Validate token
				boolean isValid = jwtUtil.validateToken(jwtToken, user.getUsername());
				if(isValid) {
					UsernamePasswordAuthenticationToken authToken = 
							new UsernamePasswordAuthenticationToken(userName, user.getPassword(),user.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				} else {
					throw new ExamPortalException("Token is Not Valid");
				}
			}
		}
		filterChain.doFilter(request, response);
		
	}

	private String parseJwtToken(HttpServletRequest request) {
		String headerAuth = request.getHeader(AUTHORIZATION_HEADER);
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(AUTHORIZATION_PREFIX)) {
			return headerAuth.substring(7, headerAuth.length());
		}
		return null;
		//throw new ExamPortalException("Authorization Header is Missing or Invalid");
	}

}
