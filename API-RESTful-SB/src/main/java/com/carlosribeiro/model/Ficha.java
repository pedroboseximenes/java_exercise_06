package com.carlosribeiro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Ficha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "A 'Data' deve ser informada.")
    private LocalDate dia;

    @ManyToOne
    private Invocador invocador;

    private String nome;
    private String status;

    public Ficha(LocalDate dia, String status, String nome, Invocador invocador ){

        this.dia = dia;
        this.status = status;
        this.nome = nome;
        this.invocador = invocador;

    }
}
