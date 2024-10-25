/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import org.hibernate.type.descriptor.java.CalendarTimeJavaType;

/**
 *
 * @author Exequiel
 */
public class test {

    public static void main(String[] args) {
        Date fecha = new Date();
        Date fecha2 = new Date();
        fecha2.setHours(0);

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

        System.out.println(fecha);
        System.out.println(fecha2);
        System.out.println("Resultado: " + sf.format(fecha).equals(sf.format(fecha2)));
    }
}
