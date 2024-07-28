package com.egg.biblioteca.repositorios;

import com.egg.biblioteca.entidades.Acto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ActoRepositorio extends JpaRepository<Acto, String> {
    @Query("SELECT a FROM Acto a WHERE"
            + " CONCAT(a.idActo, a.nombreActo, a.altaActo)"
            + " LIKE %?1%")
    public List<Acto> findAll(String palabraClave);
}
