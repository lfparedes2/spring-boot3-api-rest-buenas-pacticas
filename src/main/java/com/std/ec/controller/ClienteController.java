package com.std.ec.controller;

import com.std.ec.model.dto.ClienteDto;
import com.std.ec.model.entity.Cliente;
import com.std.ec.model.payload.MensajeResponse;
import com.std.ec.service.ICliente;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class ClienteController {

    private ICliente clienteService;
    public ClienteController(ICliente clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/clientes")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showAll() {
       List<Cliente> getList = clienteService.findAll();
        if(getList == null){
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("No existe registros")
                    .object(null)
                    .build(), HttpStatus.OK);
        }

        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("")
                .object(getList)
                .build(), HttpStatus.OK);
    }

    @PostMapping("/cliente")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody ClienteDto clienteDto) {
        Cliente clienteSave = null;
        try {
            clienteSave = clienteService.save(clienteDto);
            clienteDto =ClienteDto.builder()
                    .idCliente(clienteSave.getIdCliente())
                    .nombre(clienteSave.getNombre())
                    .apellido(clienteSave.getApellido())
                    .correo(clienteSave.getCorreo())
                    .fechaRegistro(clienteSave.getFechaRegistro())
                    .build();
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                    .mensaje("Guardo correctamente")
                    .object(clienteDto)
                    .build(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje(e.getMessage())
                    .object(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @PutMapping("/cliente/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> update(@RequestBody ClienteDto clienteDto, @PathVariable Long id ) {

        Cliente clienteUpdate = null;
        try {
           // Cliente findCliente = clienteService.findById(id);
            //if(findCliente == null){
            if(!clienteService.existsById(id)){
                return new ResponseEntity<>(MensajeResponse.builder()
                        .mensaje("El registro que desea actualizar no existe")
                        .object(null)
                        .build(), HttpStatus.NOT_FOUND);
            } else {
                clienteDto.setIdCliente(id);
                clienteUpdate = clienteService.save(clienteDto);
                clienteDto =ClienteDto.builder()
                        .idCliente(clienteUpdate.getIdCliente())
                        .nombre(clienteUpdate.getNombre())
                        .apellido(clienteUpdate.getApellido())
                        .correo(clienteUpdate.getCorreo())
                        .fechaRegistro(clienteUpdate.getFechaRegistro())
                        .build();
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Actualizado correctamente")
                                .object(clienteDto)
                                .build(), HttpStatus.CREATED);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje(e.getMessage())
                    .object(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/cliente/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            Cliente clienteDelete = clienteService.findById(id);
            clienteService.delete(clienteDelete);
            return new ResponseEntity<>(clienteDelete, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje(e.getMessage())
                    .object(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/cliente/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showById(@PathVariable Long id) {
        Cliente clienteTemp =clienteService.findById(id);
        if(clienteTemp == null){
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("El registro no existe")
                    .object(null)
                    .build(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("")
                .object(ClienteDto.builder()
                        .idCliente(clienteTemp.getIdCliente())
                        .nombre(clienteTemp.getNombre())
                        .apellido(clienteTemp.getApellido())
                        .correo(clienteTemp.getCorreo())
                        .fechaRegistro(clienteTemp.getFechaRegistro())
                        .build())
                .build(), HttpStatus.OK);
    }

    //exportar pdf
    @NotNull
    public ResponseEntity<Resource> exportPdf(@RequestParam Long idCliente, @RequestParam Long idOrden) {
       // Optional<Cliente> cliente = clienteService.findById(idCliente);
        //return this.clienteService.findById(idCliente);
        return null;
    }

}
