package com.fondos.inversion.services.impl;

import com.fondos.inversion.model.Usuarios;
import com.fondos.inversion.repository.UsuariosRepository;
import com.fondos.inversion.services.UsuariosService;
import com.fondos.inversion.exception.RecursoNoEncontradoException;
import com.fondos.inversion.exception.ValidacionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuariosServiceImpl implements UsuariosService {

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<Usuarios> findAll() throws Exception {
        return usuariosRepository.findAll();
    }

    @Override
    public Optional<Usuarios> findById(String id) throws Exception {
        return Optional.ofNullable(usuariosRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con correo: " + id)));
    }

    @Override
    public Usuarios save(Usuarios entity) throws Exception {
        validate(entity);
        entity.setContrasena(passwordEncoder.encode(entity.getContrasena()));
        return usuariosRepository.save(entity);
    }

  
    @Override
    public Usuarios update(Usuarios entity) throws Exception {
        validate(entity);
        
        Usuarios usuarioExistente = usuariosRepository.findById(entity.getCorreo())
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Usuario no encontrado con correo: " + entity.getCorreo()));
        
        if (!passwordEncoder.matches(entity.getContrasena(), usuarioExistente.getContrasena())) {
            entity.setContrasena(passwordEncoder.encode(entity.getContrasena()));
        } else {
            // Si no se genera el cambiom se mantiene el hash anterior
            entity.setContrasena(usuarioExistente.getContrasena());
        }
        
        return usuariosRepository.save(entity);
    }

    @Override
    public void delete(Usuarios entity) throws Exception {
        usuariosRepository.delete(entity);
    }

    @Override
    public void deleteById(String id) throws Exception {
        usuariosRepository.deleteById(id);
    }

    @Override
    public void validate(Usuarios entity) throws Exception {
        if (entity.getCorreo() == null || entity.getCorreo().isEmpty()) {
            throw new ValidacionException("El correo no puede estar vacío");
        }
        if (entity.getNombre() == null || entity.getNombre().isEmpty()) {
            throw new ValidacionException("El nombre no puede estar vacío");
        }
        if (entity.getContrasena() == null || entity.getContrasena().isEmpty()) {
            throw new ValidacionException("La contraseña no puede estar vacía");
        }
        if (entity.getRole() == null || entity.getRole().isEmpty()) {
            throw new ValidacionException("El role no puede estar vacío");
        }
    }
}

