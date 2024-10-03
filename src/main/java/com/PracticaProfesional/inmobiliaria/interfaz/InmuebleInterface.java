/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.interfaz;

import com.PracticaProfesional.inmobiliaria.entidades.Inmueble;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Sofia
 */
public interface InmuebleInterface {

    @Transactional
    public Inmueble guardar(Inmueble inmueble);

    @Transactional
    public void eliminar(Integer id);

    @Transactional(readOnly = true)
    public Optional<Inmueble> obtener(Integer id);

    @Transactional(readOnly = true)
    public List<Inmueble> listar();
}
