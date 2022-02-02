
package com.ejercicio2.Estancias.repositorios;

import com.ejercicio2.Estancias.entidades.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario,String>{
  
    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Usuario buscarUsuarioPorMail (@Param("email") String email);
    
    @Query("SELECT u FROM Usuario u WHERE u.alta = TRUE")
    public List<Usuario> ListarUsuariosActivos ();
    
    @Query("SELECT u FROM Usuario u WHERE u.alta = FALSE")
    public List<Usuario> ListarUsuariosInactivos ();
}
