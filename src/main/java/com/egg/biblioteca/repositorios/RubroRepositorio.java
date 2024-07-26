
package com.egg.biblioteca.repositorios;

import com.egg.biblioteca.entidades.Rubro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RubroRepositorio extends JpaRepository<Rubro,String> {

}
