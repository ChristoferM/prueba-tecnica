package com.fondos.inversion.services.impl;

import com.fondos.inversion.model.Sucursal;
import com.fondos.inversion.repository.SucursalRepository;
import com.fondos.inversion.services.SucursalService;
import com.fondos.inversion.exception.RecursoNoEncontradoException;
import com.fondos.inversion.exception.ValidacionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SucursalServiceImpl implements SucursalService {

    @Autowired
    private SucursalRepository sucursalRepository;

    @Override
    public List<Sucursal> findAll() throws Exception {
        return sucursalRepository.findAll();
    }

    @Override
    public Optional<Sucursal> findById(Integer id) throws Exception {
        return Optional.ofNullable(sucursalRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Sucursal no encontrada con ID: " + id)));
    }

    @Override
    public Sucursal save(Sucursal entity) throws Exception {
        validate(entity);
        return sucursalRepository.save(entity);
    }

    @Override
    public Sucursal update(Sucursal entity) throws Exception {
        validate(entity);
        return sucursalRepository.save(entity);
    }

    @Override
    public void delete(Sucursal entity) throws Exception {
        sucursalRepository.delete(entity);
    }

    @Override
    public void deleteById(Integer id) throws Exception {
        sucursalRepository.deleteById(id);
    }

    @Override
    public void validate(Sucursal entity) throws Exception {
        if (entity.getNombre() == null || entity.getNombre().isEmpty()) {
            throw new ValidacionException("El nombre no puede estar vacío");
        }
    }

    public void validateForUpdate(Sucursal entity) throws Exception {
        if (entity.getId() == null) {
            throw new ValidacionException("El ID no puede ser nulo al actualizar");
        }
        validate(entity);
    }
}
