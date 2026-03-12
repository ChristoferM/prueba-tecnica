package com.fondos.inversion.services.impl;

import com.fondos.inversion.model.Visitan;
import com.fondos.inversion.model.VisitanId;
import com.fondos.inversion.repository.VisitanRepository;
import com.fondos.inversion.services.VisitanService;
import com.fondos.inversion.exception.RecursoNoEncontradoException;
import com.fondos.inversion.exception.ValidacionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class VisitanServiceImpl implements VisitanService {

    @Autowired
    private VisitanRepository visitanRepository;

    @Override
    public List<Visitan> findAll() throws Exception {
        return visitanRepository.findAll();
    }

    @Override
    public Optional<Visitan> findById(VisitanId id) throws Exception {
        return Optional.ofNullable(visitanRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Visita no encontrada con ID: " + id)));
    }

    @Override
    public Visitan save(Visitan entity) throws Exception {
        validate(entity);
        return visitanRepository.save(entity);
    }

    @Override
    public Visitan update(Visitan entity) throws Exception {
        validate(entity);
        return visitanRepository.save(entity);
    }

    @Override
    public void delete(Visitan entity) throws Exception {
        visitanRepository.delete(entity);
    }

    @Override
    public void deleteById(VisitanId id) throws Exception {
        visitanRepository.deleteById(id);
    }

    @Override
    public void validate(Visitan entity) throws Exception {
        if (entity.getSucursal() == null) {
            throw new ValidacionException("La sucursal no puede ser nula");
        }
        if (entity.getCliente() == null) {
            throw new ValidacionException("El cliente no puede ser nulo");
        }
        if (entity.getFechaVisita() == null) {
            throw new ValidacionException("La fecha de visita no puede ser nula");
        }
    }
}
