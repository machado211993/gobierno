package com.egg.biblioteca.entidades;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Producto {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Getter
    @Setter
    private String idProducto;
    @Getter
    @Setter
    private String codigo;
    @Getter
    @Setter
    private String nombre;
    @Getter
    @Setter
    private Integer precio;
    @Getter
    @Setter
    @Temporal(TemporalType.DATE)
    private Date alta;
    @Getter
    @Setter
    @ManyToOne
    private Proveedor proveedor;
    @Getter
    @Setter
    @ManyToOne
    private Rubro rubro;
    @Getter
    @Setter
    @OneToOne
    public Imagen imagen;

    public Producto() {
    }

}
