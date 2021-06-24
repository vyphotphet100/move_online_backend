package com.move_up.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.move_up.converter.Converter;
import com.move_up.dto.UserDTO;
import com.move_up.entity.UserEntity;
import com.move_up.repository.UserRepository;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private Converter converter;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		UsernamePasswordAuthenticationToken authentication = null;

		if (request.getHeader("Authorization") != null) {
			String authorizationHeader = request.getHeader("Authorization");
			if (!authorizationHeader.equals("") && authorizationHeader.startsWith("Token ")) {
				String token = authorizationHeader.substring(6);
				UserEntity userEntity = userRepo.findOneByTokenCode(token);

				if (userEntity != null) {
					UserDTO userDto = converter.toDTO(userEntity, UserDTO.class);
					authentication = new UsernamePasswordAuthenticationToken(userDto, null,
							userDto.getAuthorities());
				}
			}
		}

		if (authentication != null) {
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);

	}

}
