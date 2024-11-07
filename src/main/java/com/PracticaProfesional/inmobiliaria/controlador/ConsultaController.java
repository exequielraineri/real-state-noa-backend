/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.controlador;

import com.PracticaProfesional.inmobiliaria.entidades.Cliente;
import com.PracticaProfesional.inmobiliaria.entidades.Consulta;
import com.PracticaProfesional.inmobiliaria.entidades.Inmueble;
import com.PracticaProfesional.inmobiliaria.entidades.Usuario;
import com.PracticaProfesional.inmobiliaria.servicios.ClienteServicios;
import com.PracticaProfesional.inmobiliaria.servicios.ConsultaServicios;
import com.PracticaProfesional.inmobiliaria.servicios.InmuebleServicios;
import com.PracticaProfesional.inmobiliaria.servicios.UsuarioServicios;
import jakarta.faces.annotation.RequestMap;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Exequiel
 */
@RestController
@CrossOrigin("*")
@RequestMapping("consultas")
public class ConsultaController {

    @Autowired
    private ConsultaServicios consultaServicios;
    @Autowired
    private UsuarioServicios usuarioServicios;
    @Autowired
    private ClienteServicios clienteServicios;
    @Autowired
    private InmuebleServicios inmuebleServicios;
    private Map<String, Object> response;

    @GetMapping
    public ResponseEntity<Map<String, Object>> listar() {
        try {
            response = new HashMap<>();
            response.put("data", consultaServicios.listar());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Map<String, Object>> obtener(@PathVariable Integer id) {
        try {
            response = new HashMap<>();
            Optional<Consulta> consulta = consultaServicios.obtener(id);
            if (consulta == null) {
                response.put("data", "La consulta no se encontro");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            response.put("data", consulta);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> guardar(@RequestBody Consulta consulta) {
        try {
            response = new HashMap<>();
            Usuario agenteBD = usuarioServicios.obtener(consulta.getAgente().getId()).orElse(null);
            Inmueble inmuebleBD = inmuebleServicios.obtener(consulta.getInmueble().getId()).orElse(null);
            Cliente clienteBD = clienteServicios.obtener(consulta.getCliente().getId()).orElse(null);
            if (agenteBD == null) {
                response.put("data", "El agente no se encontro");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            if (inmuebleBD == null) {
                response.put("data", "El inmueble no se encontro");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            if (clienteBD == null) {
                response.put("data", "El cliente no se encontro");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            consulta.setAgente(agenteBD);
            consulta.setCliente(clienteBD);
            consulta.setInmueble(inmuebleBD);
            consulta.setEstado("PENDIENTE");
            consulta.setActivo(true);
            consulta.setFechaRegistro(new Date());
            response.put("data", consultaServicios.guardar(consulta));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Map<String, Object>> modificar(@PathVariable Integer id, @RequestBody Consulta consulta) {
        try {
            response = new HashMap<>();
            Consulta consultaBD = consultaServicios.obtener(id).orElse(null);
            Usuario agenteBD = usuarioServicios.obtener(consulta.getAgente().getId()).orElse(null);
            Inmueble inmuebleBD = inmuebleServicios.obtener(consulta.getInmueble().getId()).orElse(null);
            Cliente clienteBD = clienteServicios.obtener(consulta.getCliente().getId()).orElse(null);
            if (consultaBD == null) {
                response.put("data", "La consulta no se encontro");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            if (agenteBD == null) {
                response.put("data", "El agente no se encontro");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            if (inmuebleBD == null) {
                response.put("data", "El inmueble no se encontro");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            if (clienteBD == null) {
                response.put("data", "El cliente no se encontro");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            consultaBD.setEstado(consulta.getEstado());
            response.put("data", consultaServicios.guardar(consulta));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Integer id) {
        try {
            response = new HashMap<>();
            Consulta consultaBD = consultaServicios.obtener(id).orElse(null);
            if (consultaBD == null) {
                response.put("data", "La consulta no se encontro");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            consultaServicios.eliminar(id);
            response.put("data", consultaBD);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
