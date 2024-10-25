/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.entidades.util;

/**
 *
 * @author Exequiel
 */

/*
PENDIENTE: El contrato se encuentra en proceso de pagos
ACTIVO: El cliente inicio su estadia en el inmueble
FINALIZADO: !ACTIVO
CANCELADO: El cliente por alguna razon se dio de baja
 */
public enum EnumEstadoContrato {
    PENDIENTE, ACTIVO, FINALIZADO, CANCELADO
}
