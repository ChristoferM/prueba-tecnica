package com.fondos.inversion.config;

import com.fondos.inversion.model.Usuarios;
import com.fondos.inversion.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        
        Usuarios usuario = usuariosRepository.findById(correo)
            .orElseThrow(() -> new UsernameNotFoundException(
                "Usuario no encontrado con correo: " + correo));
        
        if (!usuario.getActivo()) {
            throw new UsernameNotFoundException(
                "Usuario inactivo: " + correo);
        }
        
        return User.builder()
            .username(usuario.getCorreo())
            .password(usuario.getContrasena())
            .authorities(Collections.singleton(
                new SimpleGrantedAuthority("ROLE_" + usuario.getRole())
            ))
            .accountLocked(false)
            .accountExpired(false)
            .credentialsExpired(false)
            .disabled(false)
            .build();
    }
}
