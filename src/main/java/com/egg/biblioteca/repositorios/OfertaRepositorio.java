package com.egg.biblioteca.repositorios;

import com.egg.biblioteca.entidades.Oferta;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OfertaRepositorio extends JpaRepository<Oferta, String> {

    @Query("SELECT o FROM Oferta o WHERE"
            + " CONCAT(o.idOferta, o.nombreOferta, o.precio, o.altaOferta)"
            + " LIKE %?1%")
    public List<Oferta> findAll(String palabraClave);

}
