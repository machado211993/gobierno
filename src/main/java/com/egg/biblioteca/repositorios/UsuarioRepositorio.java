package com.egg.biblioteca.repositorios;

import com.egg.biblioteca.entidades.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Usuario buscarPorEmail(@Param("email") String email);

    @Query("SELECT u FROM Usuario u WHERE"
            + " CONCAT(u.id, u.nombre, u.email, u.rol)"
            + " LIKE %?1%")
    public List<Usuario> findAll(String palabraClave);

}
