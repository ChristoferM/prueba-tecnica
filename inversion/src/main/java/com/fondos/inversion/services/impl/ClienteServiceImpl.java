package com.fondos.inversion.services.impl;

import com.fondos.inversion.model.Cliente;
import com.fondos.inversion.repository.ClienteRepository;
import com.fondos.inversion.services.ClienteService;
import com.fondos.inversion.exception.RecursoNoEncontradoException;
import com.fondos.inversion.exception.ValidacionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public List<Cliente> findAll() throws Exception {
        return clienteRepository.findAll();
    }

    @Override
    public Optional<Cliente> findById(Integer id) throws Exception {
        return Optional.ofNullable(clienteRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + id)));
    }

    @Override
    public Cliente save(Cliente entity) throws Exception {
        validate(entity);
        return clienteRepository.save(entity);
    }

    @Override
    public Cliente update(Cliente entity) throws Exception {
        validate(entity);
        return clienteRepository.save(entity);
    }

    @Override
    public void delete(Cliente entity) throws Exception {
        clienteRepository.delete(entity);
    }

    @Override
    public void deleteById(Integer id) throws Exception {
        clienteRepository.deleteById(id);
    }

    @Override
    public void validate(Cliente entity) throws Exception {
        if (entity.getNombre() == null || entity.getNombre().isEmpty()) {
            throw new ValidacionException("El nombre no puede estar vacío");
        }
    }

    public void validateForUpdate(Cliente entity) throws Exception {
        if (entity.getId() == null) {
            throw new ValidacionException("El ID no puede ser nulo al actualizar");
        }
        validate(entity);
    }
}
