//package leonardoferrante.progettofinale.security;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import leonardoferrante.progettofinale.services.impl.CustomUserDetailsService;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Collections;
//
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtUtils jwtUtils;
//    private final CustomUserDetailsService userDetailsService;
//
//    public JwtAuthenticationFilter(JwtUtils jwtUtils, CustomUserDetailsService userDetailsService) {
//        this.jwtUtils = jwtUtils;
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//
//        final String authHeader = request.getHeader("Authorization");
//        final String prefix = "Bearer ";
//
//        String jwt = null;
//        String username = null;
//
//        // 1) Prova a prendere il token dall'header
//        if (authHeader != null && authHeader.startsWith(prefix)) {
//            jwt = authHeader.substring(prefix.length());
//        } else {
//            // 2) Se non esiste, prendilo dal cookie
//            jwt = getJwtFromCookie(request);
//        }
//
//        // 3) Se c'è un token valido → estrae username
//        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
//            username = jwtUtils.getUsernameFromToken(jwt);
//        }
//
//        // 4) Imposta autenticazione nello SecurityContext
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//            String role = jwtUtils.getRoleFromToken(jwt);
//
//            UsernamePasswordAuthenticationToken authToken =
//                    new UsernamePasswordAuthenticationToken(
//                            userDetails,
//                            null,
//                            Collections.singleton(new SimpleGrantedAuthority(role))
//                    );
//
//            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//            SecurityContextHolder.getContext().setAuthentication(authToken);
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    private String getJwtFromCookie(HttpServletRequest request) {
//        if (request.getCookies() == null) return null;
//
//        for (var cookie : request.getCookies()) {
//            if ("jwt".equals(cookie.getName())) {
//                return cookie.getValue();
//            }
//        }
//        return null;
//    }
//}

package leonardoferrante.progettofinale.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import leonardoferrante.progettofinale.services.impl.CustomUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, CustomUserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String prefix = "Bearer ";

        String jwt = null;
        String username = null;

        // 1) Prova a prendere il token dall'header
        if (authHeader != null && authHeader.startsWith(prefix)) {
            jwt = authHeader.substring(prefix.length());
            System.out.println("JWT trovato nell'header: " + jwt);
        } else {
            // 2) Se non esiste, prendilo dal cookie
            jwt = getJwtFromCookie(request);
            System.out.println("JWT trovato nel cookie: " + jwt);
        }

        // 3) Controllo se il token è valido
        if (jwt != null) {
            boolean valid = jwtUtils.validateJwtToken(jwt);
            System.out.println("Token valido? " + valid);

            if (valid) {
                username = jwtUtils.getUsernameFromToken(jwt);
                System.out.println("Username estratto dal token: " + username);
            }
        } else {
            System.out.println("Nessun JWT trovato");
        }

        // 4) Imposta autenticazione nello SecurityContext
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            String role = jwtUtils.getRoleFromToken(jwt);
            System.out.println("Ruolo estratto: " + role);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            Collections.singleton(new SimpleGrantedAuthority(role))
                    );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (var cookie : request.getCookies()) {
            if ("jwt".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
