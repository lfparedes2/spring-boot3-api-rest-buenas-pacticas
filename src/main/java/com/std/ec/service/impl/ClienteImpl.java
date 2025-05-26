package com.std.ec.service.impl;

import com.std.ec.model.dao.ClienteDao;
import com.std.ec.model.dto.ClienteDto;
import com.std.ec.model.entity.Cliente;
import com.std.ec.service.ICliente;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteImpl implements ICliente {
    @Autowired
    private ClienteDao clienteDao;

    @Override
    public List<Cliente> findAll() {
        return (List<Cliente>) clienteDao.findAll();
    }

    @Transactional
    @Override
    public Cliente save(ClienteDto clienteDto) {
        Cliente cliente = Cliente.builder()
                .idCliente(clienteDto.getIdCliente())
                .nombre(clienteDto.getNombre())
                .apellido(clienteDto.getApellido())
                .correo(clienteDto.getCorreo())
                .fechaRegistro(clienteDto.getFechaRegistro())
                .build();
        return clienteDao.save(cliente);
    }

    @Transactional(readOnly = true)
    @Override
    public Cliente findById(Long idCliente) {
        return clienteDao.findById(idCliente).orElse(null);
    }

    @Transactional
    @Override
    public void delete(Cliente cliente) {
        clienteDao.delete(cliente);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(Long idCliente) {
        return clienteDao.existsById(idCliente);
    }


    @NotNull
    public ResponseEntity<Resource> exportCliente(@RequestParam Long idCliente) {
        Optional<Cliente> cliente = clienteDao.findById(idCliente);
        Double rpta = 0.0;
        if (cliente.isPresent()) {
            try {
                final Cliente clienteTmp = cliente.get();
                final File file = ResourceUtils.getFile("classpath:exportInvoice.jasper");
                final File imgLogo = ResourceUtils.getFile("classpath:images/logoCevicheria.png");

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        return null;
    }

}
