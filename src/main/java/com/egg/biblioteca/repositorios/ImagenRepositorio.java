
package com.egg.biblioteca.repositorios;

import com.egg.biblioteca.entidades.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagenRepositorio extends JpaRepository<Imagen, String>{
//@Query("SELECT i FROM Imagen i WHERE i.codigo = :codigo")
//    public Imagen buscarPorCodigo(@Param("codigo")String codigo);

}
