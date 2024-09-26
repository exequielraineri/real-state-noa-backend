/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.servicios;

import com.PracticaProfesional.inmobiliaria.entidades.Inmueble;
import com.PracticaProfesional.inmobiliaria.interfaz.InmuebleInterface;
import com.PracticaProfesional.inmobiliaria.repository.InmuebleInterfaceRepo;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Sofia
 */
@Service
public class InmuebleServicios implements InmuebleInterface {

    @Autowired
    private InmuebleInterfaceRepo repo;

    @Override
    public Inmueble guardar(Inmueble inmueble) {
        inmueble.setFechaRegistro(new Date());
        return repo.save(inmueble);
    }

    @Override
    public void eliminar(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public Optional<Inmueble> obtener(Integer id) {
        return repo.findById(id);
    }

    @Override
    public List<Inmueble> listar() {
        return repo.findAll();
    }
    public List<Inmueble> listarInmuebles(String tipoinmueble, String ubicacion,String estado){
        return  repo.filtrarInmuebles(tipoinmueble, ubicacion, estado);
    }

}
