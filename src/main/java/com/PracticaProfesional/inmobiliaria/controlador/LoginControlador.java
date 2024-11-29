package com.PracticaProfesional.inmobiliaria.controlador;

import com.PracticaProfesional.inmobiliaria.entidades.Usuario;
import com.PracticaProfesional.inmobiliaria.servicios.UsuarioServicios;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("autenticacion")
public class LoginControlador {

    private Map<String, Object> response;

    @Autowired
    private UsuarioServicios usuarioService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> iniciarSesion(@RequestBody Usuario usuario) {
        response = new HashMap<>();
        try {
            usuario = usuarioService.findByCorreoAndPassword(usuario.getCorreo(), usuario.getPassword()).orElse(null);
            if (usuario == null) {
                response.put("data", "Credenciales incorrectas");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            response.put("data", usuario);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("usuario", usuario);
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
