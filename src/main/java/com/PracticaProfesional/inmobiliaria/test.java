/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria;

import com.PracticaProfesional.inmobiliaria.entidades.Contrato;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoContrato;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Exequiel
 */
public class test {
    
    public static void main(String[] args) throws ParseException {
        /* SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaInicio = new Date();
        Date fechaFin = new Date("2024/10/28");
        long tiempo_transcurrido = fechaFin.getTime() - fechaInicio.getTime();
        TimeUnit unidad = TimeUnit.DAYS;
        long dias = unidad.convert(tiempo_transcurrido, TimeUnit.MILLISECONDS);
        System.out.println(fechaInicio + "\t\t" + fechaFin);
        System.out.println(dias + " transcurridos");
        System.out.println(fechaFin.getHours());
        int cant = 4;
        BigDecimal importe = new BigDecimal(350);
        System.out.println("cant: " + cant);
        cant++;
        System.out.println("total: "+importe);
        System.out.println("mensual "+importe.divide(BigDecimal.valueOf(4)));
        System.out.println("cant: " + cant);
        System.out.println("porcentaje "+importe.multiply(BigDecimal.valueOf(0.60).setScale(2,RoundingMode.HALF_UP)));
        /*
         */
    }
}
