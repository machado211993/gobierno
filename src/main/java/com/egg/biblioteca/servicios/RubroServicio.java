package com.egg.biblioteca.servicios;


import com.egg.biblioteca.entidades.Rubro;
import com.egg.biblioteca.excepciones.MiException;

import com.egg.biblioteca.repositorios.RubroRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RubroServicio {

    @Autowired
    RubroRepositorio rubroRepositorio;

    @Transactional
    public void crearRubro(String nombre) throws MiException {

        validar(nombre);

        Rubro rubro = new Rubro();

        rubro.setNombre(nombre);

        rubroRepositorio.save(rubro);
    }

    public List<Rubro> listarRubros() {

        List<Rubro> rubros = new ArrayList();

        rubros = rubroRepositorio.findAll();

        return rubros;
    }

    public Rubro getOne(String idRubro) {
        return rubroRepositorio.getOne(idRubro);
    }

    @Transactional
    public void borrarPorId(String idRubro) {
        rubroRepositorio.deleteById(idRubro);
    }

    @Transactional
    public void modificarRubros(String idRubro, String nombre) throws MiException {
        validar(nombre);

        Optional<Rubro> respuesta = rubroRepositorio.findById(idRubro);

        if (respuesta.isPresent()) {

            Rubro rubro = respuesta.get();

            rubro.setNombre(nombre);

            rubroRepositorio.save(rubro);
        }
    }

    private void validar(String nombre) throws MiException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("el nombre del rubro no puede ser nulo o estar vacio");
        }
    }
}
