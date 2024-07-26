package com.egg.biblioteca.entidades;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Imagen {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Getter
    @Setter
    private String idImagen;
    @Getter
    @Setter
    private String mime;
    @Getter
    @Setter
    private String nombre;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "contenido", columnDefinition = "LONGBLOB")
    @Getter
    @Setter
    private byte[] contenido;

    public Imagen() {
    }
}
