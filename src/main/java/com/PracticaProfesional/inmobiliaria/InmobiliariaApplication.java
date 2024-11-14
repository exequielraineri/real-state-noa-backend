package com.PracticaProfesional.inmobiliaria;

import com.PracticaProfesional.inmobiliaria.entidades.Usuario;
import com.PracticaProfesional.inmobiliaria.servicios.UsuarioServicios;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InmobiliariaApplication {

    public static void main(String[] args) {
        SpringApplication.run(InmobiliariaApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(UsuarioServicios usuarioServicios) {
        return args -> {
            List<Usuario> usuarios = usuarioServicios.listar();
            if (usuarios.isEmpty()) {
                Usuario usuarioADMIN = new Usuario();
                usuarioADMIN.setRol("ADMIN");
                usuarioADMIN.setNombre("ADMIN");
                usuarioADMIN.setApellido("ADMIN");
                usuarioADMIN.setFechaRegistro(LocalDateTime.now());
                usuarioADMIN.setPassword("admin");
                usuarioADMIN.setCorreo("admin@admin.com");
                usuarioADMIN.setDni("0");
                usuarioADMIN.setComisionAlquiler(BigDecimal.ZERO);
                usuarioADMIN.setComisionVenta(BigDecimal.ZERO);

                usuarioServicios.guardar(usuarioADMIN);
                System.out.println("Usuario administrador inicializado con éxito.");
            }
        };
    }
}
