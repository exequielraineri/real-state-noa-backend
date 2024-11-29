package com.PracticaProfesional.inmobiliaria.servicios;

import com.PracticaProfesional.inmobiliaria.entidades.Imagen;
import com.PracticaProfesional.inmobiliaria.interfaz.ImagenInterface;
import com.PracticaProfesional.inmobiliaria.repository.ImagenInterfaceRepo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ImagenServicios implements ImagenInterface {

    @Autowired
    private ImagenInterfaceRepo repo;

    @Override
    public Imagen guardar(Imagen imagen) {
        return repo.save(imagen);
    }

    @Override
    public void eliminar(Integer id) {
        System.out.println("Entro en eliminar");
        repo.deleteById(id);
    }

    @Override
    public Optional<Imagen> obtener(Integer id) {
        return repo.findById(id);
    }

    @Override
    public List<Imagen> listar() {
        return repo.findAll();
    }

}
