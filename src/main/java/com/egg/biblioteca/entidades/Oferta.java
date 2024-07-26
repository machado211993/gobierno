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
public class Oferta {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Getter
    @Setter
    private String idOferta;
    @Getter
    @Setter
    private String precio;
    @Getter
    @Setter
    private String nombreOferta;

    @OneToOne
    @Getter
    @Setter
    public Imagen imagen;
    //queremos que cuando la OFERTA se registre en el sistema se guarde la fecha de alta 
    //por lo tanto vamos a crear un atributo del tipo date 
    @Temporal(TemporalType.DATE)
    @Getter
    @Setter
    private Date altaOferta;

    public Oferta() {
    }

}
