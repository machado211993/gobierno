package com.egg.biblioteca.servicios;

import com.egg.biblioteca.entidades.Imagen;
import com.egg.biblioteca.entidades.Aviso;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorios.AvisoRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AvisoServicio {

    @Autowired
    private AvisoRepositorio avisoRepositorio;
    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void crearAviso(MultipartFile archivo, String nombreAviso) throws MiException {

        validar(nombreAviso, archivo, nombreAviso);

        Aviso aviso = new Aviso();

        aviso.setNombreAviso(nombreAviso);

        aviso.setAltaAviso(new Date());

        Imagen imagen = imagenServicio.guardar(archivo);

        aviso.setImagen(imagen);

        avisoRepositorio.save(aviso);
    }

    public List<Aviso> listarAvisos() {

        List<Aviso> avisos = new ArrayList();

        avisos = avisoRepositorio.findAll(); // encuentra todo (findAll)

        return avisos;
    }
    // FUNCIONALIDAD PARA FILTROS DE ACTOS (busqueda)

    public List<Aviso> listAll(String palabraClave) {
        if (palabraClave != null) {
            return avisoRepositorio.findAll(palabraClave);
        }

        return avisoRepositorio.findAll();
    }

    @Transactional // se pasa el idObra porq se necesita en modificarObra
    public void modificarAviso(MultipartFile archivo, String nombreAviso, String idAviso) throws MiException {

        validar(idAviso, archivo, nombreAviso);

        Optional<Aviso> respuesta = avisoRepositorio.findById(idAviso);
        if (respuesta.isPresent()) {
            Aviso aviso = respuesta.get();

            aviso.setNombreAviso(nombreAviso);

            String idImagen = null; // se le asigna null ??

            if (aviso.getImagen() != null) {
                idImagen = aviso.getImagen().getIdImagen();
            }

            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
            aviso.setImagen(imagen);

            avisoRepositorio.save(aviso);
        }
    }

    public Aviso getOne(String idAviso) {
        return avisoRepositorio.getOne(idAviso); // conseguir uno
    }

    @Transactional
    public void borrarPorId(String idAviso) { // eliminar por num de id
        avisoRepositorio.deleteById(idAviso);
    }

    private void validar(String idAviso, MultipartFile archivo, String nombreAviso) throws MiException {
        if (archivo == null) {
            throw new MiException("el archivo no puede ser nulo"); //
        }
        if (archivo.isEmpty() || archivo == null) {
            throw new MiException("el archivo no puede ser nulo o estar vacio");
        }

        if (nombreAviso == null) {
            throw new MiException("el nombre del aviso no puede ser nulo"); //
        }

        if (idAviso.isEmpty() || idAviso == null) {
            throw new MiException("el id no puede ser nulo o estar vacio");
        }

    }
}