package com.fondos.inversion.repository;

import com.fondos.inversion.model.Inscripcion;
import com.fondos.inversion.model.InscripcionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscripcionRepository extends JpaRepository<Inscripcion, InscripcionId> {
}
