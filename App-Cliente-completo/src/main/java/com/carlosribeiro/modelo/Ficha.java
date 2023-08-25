package com.carlosribeiro.modelo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
@Entity
@Table(name = "ficha")
public class Ficha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "A 'Data' deve ser informada.")
    private LocalDate dia;

    private String nome;
    private String status;


    @ManyToOne
    private Invocador invocador;

    public Ficha(LocalDate dia, String status, String nome, Invocador invocador ){

        this.dia = dia;
        this.status = status;
        this.nome = nome;
        this.invocador = invocador;

    }
    public Ficha(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDia() {
        return dia;
    }

    public void setDia(LocalDate dia) {
        this.dia = dia;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Invocador getInvocador() {
        return invocador;
    }

    public void setInvocador(Invocador invocador) {
        this.invocador = invocador;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
