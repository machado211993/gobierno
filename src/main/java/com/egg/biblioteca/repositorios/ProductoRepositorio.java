package com.egg.biblioteca.repositorios;

import com.egg.biblioteca.entidades.Producto;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, String> {

//@Query("SELECT p FROM Producto p WHERE p.codigo = :codigo")
//    public Producto buscarPorCodigo(@Param("codigo")String codigo);
    @Query("SELECT p FROM Producto p WHERE" 
            + " CONCAT(p.idProducto, p.codigo, p.precio, p.proveedor, p.rubro, p.nombre)"
            + " LIKE %?1%")
    public List<Producto> findAll(String palabraClave);

}

//FUNCIONALIDAD PARA APLICAR FILTROS DE PRODUCTOS 
