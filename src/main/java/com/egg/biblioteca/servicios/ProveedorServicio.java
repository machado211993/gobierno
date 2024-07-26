package com.egg.biblioteca.servicios;

import com.egg.biblioteca.entidades.Proveedor;
import com.egg.biblioteca.excepciones.MiException;

import com.egg.biblioteca.repositorios.ProveedorRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProveedorServicio {

    @Autowired
    ProveedorRepositorio proveedorRepositorio;

    @Transactional
    public void crearProveedor(String nombre) throws MiException {

        validar(nombre);

        Proveedor proveedor = new Proveedor();

        proveedor.setNombre(nombre);

        proveedorRepositorio.save(proveedor);

    }

    public List<Proveedor> listarProveedores() {

        List<Proveedor> proveedores = new ArrayList();

        proveedores = proveedorRepositorio.findAll();

        return proveedores;
    }

    @Transactional
    public void modificarProveedor(String nombre, String idProveedor) throws MiException {

        validar(nombre);

        Optional<Proveedor> respuesta = proveedorRepositorio.findById(idProveedor);

        if (respuesta.isPresent()) {
            Proveedor proveedor = respuesta.get();

            proveedor.setNombre(nombre);

            proveedorRepositorio.save(proveedor);

        }
    }

    public Proveedor getOne(String idProveedor) {
        return proveedorRepositorio.getOne(idProveedor);
    }

    @Transactional
    public void borrarPorId(String idProveedor) {
        proveedorRepositorio.deleteById(idProveedor);
    }

    private void validar(String nombre) throws MiException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("el nombre no puede ser nulo o estar vacio");
        }
    }
}
