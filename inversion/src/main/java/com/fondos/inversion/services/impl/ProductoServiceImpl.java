package com.fondos.inversion.services.impl;

import com.fondos.inversion.model.Producto;
import com.fondos.inversion.repository.ProductoRepository;
import com.fondos.inversion.services.ProductoService;
import com.fondos.inversion.exception.RecursoNoEncontradoException;
import com.fondos.inversion.exception.ValidacionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<Producto> findAll() throws Exception {
        return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> findById(Integer id) throws Exception {
        return Optional.ofNullable(productoRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id)));
    }

    @Override
    public Producto save(Producto entity) throws Exception {
        validate(entity);
        return productoRepository.save(entity);
    }

    @Override
    public Producto update(Producto entity) throws Exception {
        validate(entity);
        return productoRepository.save(entity);
    }

    @Override
    public void delete(Producto entity) throws Exception {
        productoRepository.delete(entity);
    }

    @Override
    public void deleteById(Integer id) throws Exception {
        productoRepository.deleteById(id);
    }

    @Override
    public void validate(Producto entity) throws Exception {
        if (entity.getNombre() == null || entity.getNombre().isEmpty()) {
            throw new ValidacionException("El nombre no puede estar vacio");
        }
    }

    public void validateForUpdate(Producto entity) throws Exception {
        if (entity.getId() == null) {
            throw new ValidacionException("El ID no puede ser nulo al actualizar");
        }
        validate(entity);
    }
}
