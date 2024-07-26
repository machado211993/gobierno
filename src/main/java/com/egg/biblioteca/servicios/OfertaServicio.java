package com.egg.biblioteca.servicios;

import com.egg.biblioteca.entidades.Imagen;
import com.egg.biblioteca.entidades.Oferta;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorios.OfertaRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class OfertaServicio {

    @Autowired
    private OfertaRepositorio ofertaRepositorio;
    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void crearOferta(MultipartFile archivo, String nombreOferta, String precio) throws MiException {

        validar(nombreOferta, archivo, precio, precio);

        Oferta oferta = new Oferta();

        oferta.setNombreOferta(nombreOferta);
        oferta.setPrecio(precio);

        oferta.setAltaOferta(new Date());

        Imagen imagen = imagenServicio.guardar(archivo);

        oferta.setImagen(imagen);

        ofertaRepositorio.save(oferta);
    }

    public List<Oferta> listarOfertas() {

        List<Oferta> ofertas = new ArrayList();

        ofertas = ofertaRepositorio.findAll(); //encuentra todo (findAll)

        return ofertas;
    }
    //FUNCIONALIDAD PARA FILTROS DE OFERTAS (busqueda)

    public List<Oferta> listAll(String palabraClave) {
        if (palabraClave != null) {
            return ofertaRepositorio.findAll(palabraClave);
        }

        return ofertaRepositorio.findAll();
    }

    @Transactional //se pasa el idOferta porq se necesita en modificarOferta
    public void modificarOferta(MultipartFile archivo, String nombreOferta, String precio, String idOferta) throws MiException {

        validar(idOferta, archivo, nombreOferta, precio);

        Optional<Oferta> respuesta = ofertaRepositorio.findById(idOferta);
        if (respuesta.isPresent()) {
            Oferta oferta = respuesta.get();

            oferta.setPrecio(precio);
            oferta.setNombreOferta(nombreOferta);

            String idImagen = null;  //se le asigna null ??

            if (oferta.getImagen() != null) {
                idImagen = oferta.getImagen().getIdImagen();
            }

            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
            oferta.setImagen(imagen);

            ofertaRepositorio.save(oferta);
        }
    }

    public Oferta getOne(String idOferta) {
        return ofertaRepositorio.getOne(idOferta); //conseguir uno
    }

    @Transactional
    public void borrarPorId(String idOferta) { //eliminar por num de id
        ofertaRepositorio.deleteById(idOferta);
    }

    private void validar(String idOferta, MultipartFile archivo, String nombreOferta, String precio) throws MiException {
        if (archivo == null) {
            throw new MiException("el archivo no puede ser nulo"); //
        }
        if (archivo.isEmpty() || archivo == null) {
            throw new MiException("el archivo no puede ser nulo o estar vacio");
        }

        if (nombreOferta == null) {
            throw new MiException("el nombre no puede ser nulo"); //
        }
        if (nombreOferta.isEmpty() || nombreOferta == null) {
            throw new MiException("el nombre no puede ser nulo o estar vacio");

        }
        if (precio == null) {
            throw new MiException("precio no puede ser nulo");
        }
        if (precio.isEmpty() || precio == null) {
            throw new MiException("el precio no puede ser nulo o estar vacio");
        }
        if (idOferta.isEmpty() || idOferta == null) {
            throw new MiException("el id no puede ser nulo o estar vacio");
        }

    }
}
//hicimos entidades, repositorio, servicio, ahora vamos por el controlador
