package com.egg.biblioteca.entidades;

import com.egg.biblioteca.enumeraciones.Rol;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private String nombre;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private Rol rol;

    @Getter
    @Setter
    @OneToOne
    private Imagen imagen;

    public Usuario() {
    }

}
