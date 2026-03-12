package com.fondos.inversion.services.impl;

import com.fondos.inversion.model.Inscripcion;
import com.fondos.inversion.model.InscripcionId;
import com.fondos.inversion.repository.InscripcionRepository;
import com.fondos.inversion.services.InscripcionService;
import com.fondos.inversion.exception.RecursoNoEncontradoException;
import com.fondos.inversion.exception.ValidacionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class InscripcionServiceImpl implements InscripcionService {

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Override
    public List<Inscripcion> findAll() throws Exception {
        return inscripcionRepository.findAll();
    }

    @Override
    public Optional<Inscripcion> findById(InscripcionId id) throws Exception {
        return Optional.ofNullable(inscripcionRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Inscripción no encontrada con ID: " + id)));
    }

    @Override
    public Inscripcion save(Inscripcion entity) throws Exception {
        validate(entity);
        return inscripcionRepository.save(entity);
    }

    @Override
    public Inscripcion update(Inscripcion entity) throws Exception {
        validate(entity);
        return inscripcionRepository.save(entity);
    }

    @Override
    public void delete(Inscripcion entity) throws Exception {
        inscripcionRepository.delete(entity);
    }

    @Override
    public void deleteById(InscripcionId id) throws Exception {
        inscripcionRepository.deleteById(id);
    }

    @Override
    public void validate(Inscripcion entity) throws Exception {
        if (entity.getProducto() == null) {
            throw new ValidacionException("El producto no puede ser nulo");
        }
        if (entity.getCliente() == null) {
            throw new ValidacionException("El cliente no puede ser nulo");
        }
    }
}
