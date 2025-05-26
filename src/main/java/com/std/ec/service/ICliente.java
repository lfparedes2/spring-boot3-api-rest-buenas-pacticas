package com.std.ec.service;

import com.std.ec.model.dto.ClienteDto;
import com.std.ec.model.entity.Cliente;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

public interface ICliente {

    List<Cliente> findAll();

    Cliente save(ClienteDto clienteDto);

    Cliente findById(Long idCliente);

    void delete(Cliente cliente);

    boolean existsById(Long idCliente);


}
