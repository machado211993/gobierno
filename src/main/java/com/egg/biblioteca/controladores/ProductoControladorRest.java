/*package com.egg.biblioteca.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.egg.biblioteca.entidades.Producto;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorios.ProductoRepositorio;
import com.egg.biblioteca.servicios.ProductoServicio;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
@RequestMapping("/api")
public class ProductoControladorRest {

    @Autowired
    private ProductoServicio productoServicio;
    @Autowired
    private ProductoRepositorio productoRepositorio;

    @RequestMapping(value = "/productorest")
    public List<Producto> listar() {

        return productoServicio.listarProductos();
    }

    @GetMapping("/{idProducto}")
    public Producto getUserById(@PathVariable String idProducto) {
        return productoServicio.getOne(idProducto);
    }

    @PostMapping("/crear")
    public ResponseEntity<Producto> createProduct(
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam("idProducto") String idProducto,
            @RequestParam("codigo") String codigo,
            @RequestParam("nombre") String nombre,
            @RequestParam("precio") Integer precio,
            @RequestParam("idProveedor") String idProveedor,
            @RequestParam("idRubro") String idRubro) {

        try {
            productoServicio.crearProducto(archivo, idProducto, codigo, nombre, precio, idProveedor, idRubro);
            Optional<Producto> producto = productoRepositorio.findById(idProducto);

            if (producto.isPresent()) {
                return ResponseEntity.ok(producto.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

        } catch (MiException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/actualizar")
    public ResponseEntity<Producto> updateProduct(
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam("idProducto") String idProducto,
            @RequestParam("codigo") String codigo,
            @RequestParam("nombre") String nombre,
            @RequestParam("precio") Integer precio,
            @RequestParam("idProveedor") String idProveedor,
            @RequestParam("idRubro") String idRubro) {

        try {
            productoServicio.modificarProducto(archivo, idProducto, codigo, nombre, precio, idProveedor, idRubro);
            Optional<Producto> producto = productoRepositorio.findById(idProducto);

            if (producto.isPresent()) {
                return ResponseEntity.ok(producto.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

        } catch (MiException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/eliminar/{idProducto}")
    public void deleteUser(@PathVariable String idProducto) {
        productoServicio.borrarPorId(idProducto);
    }

}*/
