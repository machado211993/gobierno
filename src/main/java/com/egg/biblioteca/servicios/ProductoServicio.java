package com.egg.biblioteca.servicios;

import com.egg.biblioteca.entidades.Imagen;
import com.egg.biblioteca.entidades.Producto;
import com.egg.biblioteca.entidades.Proveedor;
import com.egg.biblioteca.entidades.Rubro;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorios.ProductoRepositorio;
import com.egg.biblioteca.repositorios.ProveedorRepositorio;
import com.egg.biblioteca.repositorios.RubroRepositorio;
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
public class ProductoServicio {

    @Autowired
    private ProductoRepositorio productoRepositorio;
    @Autowired
    private ProveedorRepositorio proveedorRepositorio;
    @Autowired
    private RubroRepositorio rubroRepositorio;
    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void crearProducto(MultipartFile archivo, String idProducto, String codigo, String nombre, Integer precio,
            String idProveedor, String idRubro) throws MiException {

        validar(archivo, idProducto, codigo, nombre, precio, idProveedor, idRubro);

        Optional<Producto> respuesta = productoRepositorio.findById(idProducto);
        Optional<Proveedor> respuestaProveedor = proveedorRepositorio.findById(idProveedor);
        Optional<Rubro> respuestaRubro = rubroRepositorio.findById(idRubro);

        Proveedor proveedor = new Proveedor();
        Rubro rubro = new Rubro();

        if (respuestaProveedor.isPresent()) {

            proveedor = respuestaProveedor.get();
        }

        if (respuestaRubro.isPresent()) {

            rubro = respuestaRubro.get();
        }

        Producto producto = new Producto();

        producto.setCodigo(codigo);
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setAlta(new Date());
        producto.setProveedor(proveedor);
        producto.setRubro(rubro);

        Imagen imagen = imagenServicio.guardar(archivo);

        producto.setImagen(imagen);

        productoRepositorio.save(producto);
    }
    // funcionalidad para listado de productos

    public List<Producto> listarProductos() {

        List<Producto> productos = new ArrayList();

        productos = productoRepositorio.findAll();

        return productos;
    }
    // FUNCIONALIDAD PARA FILTROS DE PRODUCTOS (busqueda)

    public List<Producto> listAll(String palabraClave) {
        if (palabraClave != null) {
            return productoRepositorio.findAll(palabraClave);
        }

        return productoRepositorio.findAll();
    }

    /* funcionalidad para paginacion */
    public Page<Producto> listarPaginacion(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productoRepositorio.findAll(pageable);
    }

    @Transactional
    public void modificarProducto(MultipartFile archivo, String idProducto, String codigo, String nombre,
            Integer precio, String idProveedor, String idRubro) throws MiException {

        validar(archivo, idProducto, codigo, nombre, precio, idProveedor, idRubro);

        Optional<Producto> respuesta = productoRepositorio.findById(idProducto);
        Optional<Proveedor> respuestaProveedor = proveedorRepositorio.findById(idProveedor);
        Optional<Rubro> respuestaRubro = rubroRepositorio.findById(idRubro);

        Proveedor proveedor = new Proveedor();
        Rubro rubro = new Rubro();

        if (respuestaProveedor.isPresent()) {

            proveedor = respuestaProveedor.get();
        }

        if (respuestaRubro.isPresent()) {

            rubro = respuestaRubro.get();
        }

        if (respuesta.isPresent()) {

            Producto producto = respuesta.get();

            producto.setCodigo(codigo);
            producto.setNombre(nombre);

            producto.setPrecio(precio);

            producto.setProveedor(proveedor);

            producto.setRubro(rubro);

            String idImagen = null;

            if (producto.getImagen() != null) {
                idImagen = producto.getImagen().getIdImagen();
            }

            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
            producto.setImagen(imagen);

            productoRepositorio.save(producto);

        }
    }

    public Producto getOne(String idProducto) {
        return productoRepositorio.getOne(idProducto); // encontrar uno
    }

    // eleminar producto
    @Transactional
    public void borrarPorId(String idProducto) {
        productoRepositorio.deleteById(idProducto);
    }

    private void validar(MultipartFile archivo, String idProducto, String codigo, String nombre, Integer precio,
            String idProveedor, String idRubro) throws MiException {

        if (idProducto == null) {
            throw new MiException("el idProducto no puede ser nulo"); //
        }
        if (codigo == null) {
            throw new MiException("el codigo no puede ser nulo"); //
        }
        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("el nombre no puede ser nulo o estar vacio");
        }
        if (precio == null) {
            throw new MiException("precio no puede ser nulo");
        }
        if (idProveedor.isEmpty() || idProveedor == null) {
            throw new MiException("el proveedor no puede ser nulo o estar vacio");
        }

        if (idRubro.isEmpty() || idRubro == null) {
            throw new MiException("El Rubro no puede ser nula o estar vacia");
        }
        if (archivo.isEmpty() || archivo == null) {
            throw new MiException("El archivo no puede ser nula o estar vacia");
        }

    }

}
