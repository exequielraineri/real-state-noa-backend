package com.PracticaProfesional.inmobiliaria.servicios;

import com.PracticaProfesional.inmobiliaria.entidades.Inmueble;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumEstadoInmueble;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoInmuebles;
import com.PracticaProfesional.inmobiliaria.interfaz.InmuebleInterface;
import com.PracticaProfesional.inmobiliaria.repository.InmuebleInterfaceRepo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class InmuebleServicios implements InmuebleInterface {

    @Autowired
    private InmuebleInterfaceRepo repo;

    @Override
    public Inmueble guardar(Inmueble inmueble) {
        inmueble.setActivo(true);
        return repo.save(inmueble);
    }

    @Override
    public void eliminar(Integer id) {
        Inmueble inmueble = obtener(id).get();
        inmueble.setActivo(false);
        repo.save(inmueble);
    }

    @Override
    public Optional<Inmueble> obtener(Integer id) {
        return repo.findById(id);
    }

    @Override
    public List<Inmueble> listar() {
        return repo.findAll(Sort.by(Sort.Direction.DESC, "fechaRegistro"));
    }

    public Long cantidad() {
        return repo.count();
    }

    public List<Inmueble> listar(String tipoInmueble, String direccion, String estado) {

        return repo.filtrarInmuebles(
                tipoInmueble == null ? null : EnumTipoInmuebles.valueOf(tipoInmueble),
                direccion == null ? null : direccion,
                estado == null ? null : EnumEstadoInmueble.valueOf(estado));
    }

    

}
