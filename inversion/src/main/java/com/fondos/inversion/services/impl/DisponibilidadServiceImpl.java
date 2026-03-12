package com.fondos.inversion.services.impl;

import com.fondos.inversion.model.Disponibilidad;
import com.fondos.inversion.model.DisponibilidadId;
import com.fondos.inversion.repository.DisponibilidadRepository;
import com.fondos.inversion.services.DisponibilidadService;
import com.fondos.inversion.exception.RecursoNoEncontradoException;
import com.fondos.inversion.exception.ValidacionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DisponibilidadServiceImpl implements DisponibilidadService {

    @Autowired
    private DisponibilidadRepository disponibilidadRepository;

    @Override
    public List<Disponibilidad> findAll() throws Exception {
        return disponibilidadRepository.findAll();
    }

    @Override
    public Optional<Disponibilidad> findById(DisponibilidadId id) throws Exception {
        return Optional.ofNullable(disponibilidadRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Disponibilidad no encontrada con ID: " + id)));
    }

    @Override
    public Disponibilidad save(Disponibilidad entity) throws Exception {
        validate(entity);
        return disponibilidadRepository.save(entity);
    }

    @Override
    public Disponibilidad update(Disponibilidad entity) throws Exception {
        validate(entity);
        return disponibilidadRepository.save(entity);
    }

    @Override
    public void delete(Disponibilidad entity) throws Exception {
        disponibilidadRepository.delete(entity);
    }

    @Override
    public void deleteById(DisponibilidadId id) throws Exception {
        disponibilidadRepository.deleteById(id);
    }

    @Override
    public void validate(Disponibilidad entity) throws Exception {
        if (entity.getSucursal() == null) {
            throw new ValidacionException("La sucursal no puede ser nula");
        }
        if (entity.getProducto() == null) {
            throw new ValidacionException("El producto no puede ser nulo");
        }
    }
}
