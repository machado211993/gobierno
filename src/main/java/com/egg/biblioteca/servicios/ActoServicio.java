package com.egg.biblioteca.servicios;

import com.egg.biblioteca.entidades.Imagen;
import com.egg.biblioteca.entidades.Acto;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorios.ActoRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ActoServicio {

    @Autowired
    private ActoRepositorio actoRepositorio;
    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void crearActo(MultipartFile archivo, String nombreActo) throws MiException {

        validar(nombreActo, archivo, nombreActo);

        Acto acto = new Acto();

        acto.setNombreActo(nombreActo);

        acto.setAltaActo(new Date());

        Imagen imagen = imagenServicio.guardar(archivo);

        acto.setImagen(imagen);

        actoRepositorio.save(acto);
    }

    public List<Acto> listarActos() {

        List<Acto> actos = new ArrayList();

        actos = actoRepositorio.findAll(); // encuentra todo (findAll)

        return actos;
    }
    // FUNCIONALIDAD PARA FILTROS DE ACTOS (busqueda)

    public List<Acto> listAll(String palabraClave) {
        if (palabraClave != null) {
            return actoRepositorio.findAll(palabraClave);
        }

        return actoRepositorio.findAll();
    }

    @Transactional // se pasa el idObra porq se necesita en modificarObra
    public void modificarActo(MultipartFile archivo, String nombreActo, String idActo) throws MiException {

        validar(idActo, archivo, nombreActo);

        Optional<Acto> respuesta = actoRepositorio.findById(idActo);
        if (respuesta.isPresent()) {
            Acto acto = respuesta.get();

            acto.setNombreActo(nombreActo);

            String idImagen = null; // se le asigna null ??

            if (acto.getImagen() != null) {
                idImagen = acto.getImagen().getIdImagen();
            }

            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
            acto.setImagen(imagen);

            actoRepositorio.save(acto);
        }
    }

    public Acto getOne(String idActo) {
        return actoRepositorio.getOne(idActo); // conseguir uno
    }

    @Transactional
    public void borrarPorId(String idActo) { // eliminar por num de id
        actoRepositorio.deleteById(idActo);
    }

    private void validar(String idActo, MultipartFile archivo, String nombreActo) throws MiException {
        if (archivo == null) {
            throw new MiException("el archivo no puede ser nulo"); //
        }
        if (archivo.isEmpty() || archivo == null) {
            throw new MiException("el archivo no puede ser nulo o estar vacio");
        }

        if (nombreActo == null) {
            throw new MiException("el nombre del acto no puede ser nulo"); //
        }
        if (nombreActo.isEmpty() || nombreActo == null) {
            throw new MiException("el nombre del acto no puede ser nulo o estar vacio");

        }

        if (idActo.isEmpty() || idActo == null) {
            throw new MiException("el id no puede ser nulo o estar vacio");
        }

    }
}