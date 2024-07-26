package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Producto;
import com.egg.biblioteca.entidades.Proveedor;
import com.egg.biblioteca.entidades.Rubro;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorios.ProductoRepositorio;
import com.egg.biblioteca.servicios.ProductoServicio;
import com.egg.biblioteca.servicios.ProveedorServicio;
import com.egg.biblioteca.servicios.RubroServicio;
import com.egg.biblioteca.util.reportes.ProductoExporterPDF;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/producto")
public class ProductoControlador {

    @Autowired
    private ProductoServicio productoServicio;
    @Autowired
    private ProveedorServicio proveedorServicio;
    @Autowired
    private RubroServicio rubroServicio;
    

    @GetMapping("/registrar") // localhost:8080/producto/registrar
    public String registrar(ModelMap modelo) {
        List<Proveedor> proveedores = proveedorServicio.listarProveedores();
        List<Rubro> rubros = rubroServicio.listarRubros();

        modelo.addAttribute("proveedores", proveedores);
        modelo.addAttribute("rubros", rubros);

        return "producto_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam(required = false) String codigo, @RequestParam String nombre,
            @RequestParam(required = false) Integer precio, @RequestParam String idProveedor,
            @RequestParam String idRubro, ModelMap modelo, @RequestParam(required = false) MultipartFile archivo,  @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {

            productoServicio.crearProducto(archivo, idRubro, codigo, nombre, precio, idProveedor, idRubro);

            modelo.put("exito", "El Producto fue cargado correctamente!");
            Page<Producto> productosPage = productoServicio.listarPaginacion(page, size);
            modelo.addAttribute("productos", productosPage.getContent());
            modelo.addAttribute("pageable", productosPage);
            
        } catch (MiException ex) {
            List<Proveedor> proveedores = proveedorServicio.listarProveedores();
            List<Rubro> rubros = rubroServicio.listarRubros();

            modelo.addAttribute("proveedores", proveedores);
            modelo.addAttribute("rubros", rubros);
            modelo.put("error", ex.getMessage());

            return "producto_form.html"; // volvemos a cargar el formulario. ///libro form
        }
        return "index.html";
    }

    // paginacion
    @GetMapping("/lista")
    public String listar(ModelMap modelo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Producto> productosPage = productoServicio.listarPaginacion(page, size);
        modelo.addAttribute("productos", productosPage.getContent());
        modelo.addAttribute("pageable", productosPage);

        return "producto_list";
    }

    // filtrado
    @GetMapping("/filtrar")
    public String listar(ModelMap modelo, @Param("palabraClave") String palabraClave) {
        List<Producto> productos = productoServicio.listAll(palabraClave);
        modelo.addAttribute("productos", productos);
        modelo.addAttribute("palabraClave", palabraClave);

        return "producto_list";
    }

    @GetMapping("/modificar/{idProducto}")
    public String modificar(@PathVariable String idProducto, ModelMap modelo) {

        modelo.put("producto", productoServicio.getOne(idProducto));

        List<Proveedor> proveedores = proveedorServicio.listarProveedores();
        List<Rubro> rubros = rubroServicio.listarRubros();

        modelo.addAttribute("proveedores", proveedores);
        modelo.addAttribute("rubros", rubros);

        return "producto_modificar.html";
    }

    @PostMapping("/modificar/{idProducto}")
    public String modificar(@PathVariable String idProducto, MultipartFile archivo, String codigo, String nombre,
            Integer precio, String idProveedor, String idRubro, ModelMap modelo) {
        try {
            List<Proveedor> proveedores = proveedorServicio.listarProveedores();
            List<Rubro> rubros = rubroServicio.listarRubros();

            modelo.addAttribute("proveedores", proveedores);
            modelo.addAttribute("rubros", rubros);

            productoServicio.modificarProducto(archivo, idProducto, codigo, nombre, precio, idProveedor, idRubro);

            return "redirect:../lista";

        } catch (MiException ex) {
            List<Proveedor> proveedores = proveedorServicio.listarProveedores();
            List<Rubro> rubros = rubroServicio.listarRubros();

            modelo.put("error", ex.getMessage());

            modelo.addAttribute("proveedores", proveedores);
            modelo.addAttribute("rubros", rubros);

            return "producto_modificar.html";
        }

    }

    @GetMapping("/imagen/{idProducto}") // para devolver imagen como cartas
    public ResponseEntity<byte[]> imagenProducto(@PathVariable String idProducto) {

        Producto producto = productoServicio.getOne(idProducto);

        byte[] imagen = producto.getImagen().getContenido();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.IMAGE_JPEG); // se va a recibir una imagen de tipo JPEG

        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }

    // PARA ELIMINAR
    @GetMapping("/eliminar/{idProducto}")
    public String eliminar(@PathVariable String idProducto, ModelMap modelo) {

        modelo.put("producto", productoServicio.getOne(idProducto));
        return "eliminar_producto.html";
    }

    // PARA ELIMINAR
    @PostMapping("/eliminado/{idProducto}")
    public String eliminado(@PathVariable String idProducto, ModelMap modelo) {

        productoServicio.borrarPorId(idProducto);

        return "redirect:../lista";
    }

    // PARA REPORTES PDF DE PRODUCTOS
    @GetMapping("/exportarPDF")
    public void exportarListadoDeProductosEnPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormatter.format(new Date());
        String cabecera = "Content-Disposition";
        String valor = "attachment; filename=Clientes_" + fechaActual + ".pdf";
        response.setHeader(cabecera, valor);

        List<Producto> productos = productoServicio.listarProductos(); // cargo la lista
        ProductoExporterPDF exporter = new ProductoExporterPDF(productos);
        exporter.exportar(response);
    }
}
