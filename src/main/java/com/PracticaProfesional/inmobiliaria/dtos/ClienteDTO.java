package com.PracticaProfesional.inmobiliaria.dtos;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO {

    private Integer id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String dni;
    private Date fechaRegistro;
    private String provincia;
    private Boolean estado;
    private String tipoCliente;

}
