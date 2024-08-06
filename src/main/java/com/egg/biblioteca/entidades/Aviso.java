package com.egg.biblioteca.entidades;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Aviso {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Getter
    @Setter
    private String idAviso;
    @Getter
    @Setter
    private String nombreAviso;
    @Getter
    @Setter
    @Temporal(TemporalType.DATE)
    private Date altaAviso;
    @Getter
    @Setter
    @OneToOne
    public Imagen imagen;

    public Aviso() {
    }

}
