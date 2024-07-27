package com.egg.biblioteca.servicios;

import com.egg.biblioteca.entidades.Imagen;
import com.egg.biblioteca.entidades.Obra;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorios.ObraRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ObraServicio {

    @Autowired
    private ObraRepositorio obraRepositorio;
    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void crearObra(MultipartFile archivo, String nombreObra) throws MiException {

        validar(nombreObra, archivo, nombreObra);

        Obra obra = new Obra();

        obra.setNombreObra(nombreObra);

        obra.setAltaObra(new Date());

        Imagen imagen = imagenServicio.guardar(archivo);

        obra.setImagen(imagen);

        obraRepositorio.save(obra);
    }

    public List<Obra> listarObras() {

        List<Obra> obras = new ArrayList();

        obras = obraRepositorio.findAll(); // encuentra todo (findAll)

        return obras;
    }
    // FUNCIONALIDAD PARA FILTROS DE OBRAS (busqueda)

    public List<Obra> listAll(String palabraClave) {
        if (palabraClave != null) {
            return obraRepositorio.findAll(palabraClave);
        }

        return obraRepositorio.findAll();
    }

    @Transactional // se pasa el idObra porq se necesita en modificarObra
    public void modificarObra(MultipartFile archivo, String nombreObra, String idObra) throws MiException {

        validar(idObra, archivo, nombreObra);

        Optional<Obra> respuesta = obraRepositorio.findById(idObra);
        if (respuesta.isPresent()) {
            Obra obra = respuesta.get();

            obra.setNombreObra(nombreObra);

            String idImagen = null; // se le asigna null ??

            if (obra.getImagen() != null) {
                idImagen = obra.getImagen().getIdImagen();
            }

            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
            obra.setImagen(imagen);

            obraRepositorio.save(obra);
        }
    }

    public Obra getOne(String idObra) {
        return obraRepositorio.getOne(idObra); // conseguir uno
    }

    @Transactional
    public void borrarPorId(String idObra) { // eliminar por num de id
        obraRepositorio.deleteById(idObra);
    }

    private void validar(String idObra, MultipartFile archivo, String nombreObra) throws MiException {
        if (archivo == null) {
            throw new MiException("el archivo no puede ser nulo"); //
        }
        if (archivo.isEmpty() || archivo == null) {
            throw new MiException("el archivo no puede ser nulo o estar vacio");
        }

        if (nombreObra == null) {
            throw new MiException("el nombre de la obra no puede ser nulo"); //
        }
        if (nombreObra.isEmpty() || nombreObra == null) {
            throw new MiException("el nombre de la obra no puede ser nulo o estar vacio");

        }

        if (idObra.isEmpty() || idObra == null) {
            throw new MiException("el id no puede ser nulo o estar vacio");
        }

    }
}
// hicimos entidades, repositorio, servicio, ahora vamos por el controlador
