package com.fondos.inversion.repository;

import com.fondos.inversion.model.Disponibilidad;
import com.fondos.inversion.model.DisponibilidadId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, DisponibilidadId> {
}
