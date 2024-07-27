package com.egg.biblioteca.servicios;

import com.egg.biblioteca.entidades.Imagen;
import com.egg.biblioteca.entidades.Evento;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorios.EventoRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EventoServicio {

    @Autowired
    private EventoRepositorio eventoRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void crearEvento(MultipartFile archivo, String idEvento, String nombreEvento) throws MiException {

        validar(archivo, idEvento, nombreEvento);

        Evento evento = new Evento();
        evento.setNombreEvento(nombreEvento);
        evento.setAltaEvento(new Date());

        Imagen imagen = imagenServicio.guardar(archivo);

        evento.setImagen(imagen);

        eventoRepositorio.save(evento);
    }
    // funcionalidad para listado de eventos

    public List<Evento> listarEventos() {

        List<Evento> eventos = new ArrayList();

        eventos = eventoRepositorio.findAll();

        return eventos;
    }
    // FUNCIONALIDAD PARA FILTROS DE EVENTOS (busqueda)

    public List<Evento> listAll(String palabraClave) {
        if (palabraClave != null) {
            return eventoRepositorio.findAll(palabraClave);
        }

        return eventoRepositorio.findAll();
    }

    /* funcionalidad para paginacion */
    public Page<Evento> listarPaginacion(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return eventoRepositorio.findAll(pageable);
    }

    @Transactional
    public void modificarEvento(MultipartFile archivo, String idEvento, String nombreEvento) throws MiException {

        validar(archivo, idEvento, nombreEvento);

        Optional<Evento> respuesta = eventoRepositorio.findById(idEvento);

        if (respuesta.isPresent()) {

            Evento evento = respuesta.get();

            evento.setNombreEvento(nombreEvento);

            String idImagen = null;

            if (evento.getImagen() != null) {
                idImagen = evento.getImagen().getIdImagen();
            }

            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
            evento.setImagen(imagen);

            eventoRepositorio.save(evento);

        }
    }

    public Evento getOne(String idEvento) {
        return eventoRepositorio.getOne(idEvento); // encontrar uno
    }

    // eleminar evento
    @Transactional
    public void borrarPorId(String idEvento) {
        eventoRepositorio.deleteById(idEvento);
    }

    private void validar(MultipartFile archivo, String idEvento, String nombreEvento) throws MiException {

        if (idEvento == null) {
            throw new MiException("el idEvento no puede ser nulo"); //
        }

        if (nombreEvento.isEmpty() || nombreEvento == null) {
            throw new MiException("el nombre del evento no puede ser nulo o estar vacio");
        }

        if (archivo.isEmpty() || archivo == null) {
            throw new MiException("El archivo no puede ser nula o estar vacia");
        }

    }

}
