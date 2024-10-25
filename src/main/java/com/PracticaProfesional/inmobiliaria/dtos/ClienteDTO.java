/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.dtos;

import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoCliente;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Exequiel
 */
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
