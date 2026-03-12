package com.fondos.inversion.repository;

import com.fondos.inversion.model.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuariosRepository extends JpaRepository<Usuarios, String> {
}
