
package com.PracticaProfesional.inmobiliaria.interfaz;

import com.PracticaProfesional.inmobiliaria.entidades.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioInterface {
    public Usuario guardar(Usuario usuario);
    
    public void eliminar(Integer id);
    
    public Optional<Usuario> obtener(Integer id);
    
    public List<Usuario> listar();
}
