package com.std.ec.service.impl;

import com.std.ec.model.dao.ClienteDao;
import com.std.ec.model.dto.ClienteDto;
import com.std.ec.model.entity.Cliente;
import com.std.ec.service.ICliente;
import jakarta.validation.constraints.NotNull;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class ClienteImpl implements ICliente {
    @Autowired
    private ClienteDao clienteDao;

    @Autowired
    private DataSource dataSource;

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


    @Override
    public ResponseEntity<Resource> exportarClientePdf(Long idCliente) {
        Optional<Cliente> cliente = clienteDao.findById(idCliente);

        if (cliente.isPresent()) {
            try {
                Cliente clienteTmp = cliente.get();

                File file = ResourceUtils.getFile("classpath:report_spring_sin_conn.jasper");
                File imgLogo = ResourceUtils.getFile("classpath:images/qr_local.PNG");

                JasperReport report = (JasperReport) JRLoader.loadObject(file);

                Map<String, Object> parameters = new HashMap<>();
                parameters.put("ID_CLIENTE", clienteTmp.getNombre());
                parameters.put("NOMBRE_CLIENTE", clienteTmp.getNombre());
                parameters.put("EMAIL_CLIENTE", clienteTmp.getCorreo());

                JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
                byte[] reporte = JasperExportManager.exportReportToPdf(jasperPrint);

                String sdf = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                String filename = "cliente_" + clienteTmp.getIdCliente() + "_" + sdf + ".pdf";

                HttpHeaders headers = new HttpHeaders();
                headers.setContentDisposition(ContentDisposition.builder("attachment").filename(filename).build());
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentLength(reporte.length);

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(new ByteArrayResource(reporte));

            } catch (Exception e) {
                throw new RuntimeException("Error al generar el PDF", e);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    @NotNull
    public ResponseEntity<Resource> exportarClientePdf2(Long idCliente) {
        try {
            final File file = ResourceUtils.getFile("classpath:report_spring.jasper");
            final JasperReport report = (JasperReport) JRLoader.loadObject(file);

            // Parámetros que se pasan al reporte
            final HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("ID_CLIENTE", idCliente); // asegúrate de que el .jasper reciba este parámetro
            parameters.put("EMAIL_CLIENTE" ,"Email 1");
            parameters.put("NOMBRE_CLIENTE", "Cliente 1");
            // Obtiene la conexión desde el DataSource
            try (Connection conn = dataSource.getConnection()) {
                JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, conn);
                byte[] reporte = JasperExportManager.exportReportToPdf(jasperPrint);

                String sdf = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                String filename = String.format("cliente_%d_%s.pdf", idCliente, sdf);

                return ResponseEntity.ok()
                        .contentLength(reporte.length)
                        .contentType(MediaType.APPLICATION_PDF)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                        .body(new ByteArrayResource(reporte));
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF", e);
        }
    }

}
