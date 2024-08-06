package com.egg.biblioteca.repositorios;

import com.egg.biblioteca.entidades.Aviso;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AvisoRepositorio extends JpaRepository<Aviso, String> {
    @Query("SELECT a FROM Aviso a WHERE"
            + " CONCAT(a.idAviso, a.nombreAviso, a.altaAviso)"
            + " LIKE %?1%")
    public List<Aviso> findAll(String palabraClave);
}
