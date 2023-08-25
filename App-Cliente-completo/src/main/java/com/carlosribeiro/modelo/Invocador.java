package com.carlosribeiro.modelo;

import javax.persistence.*;

@Entity
@Table
public class Invocador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String elo;

    public Invocador(String nome, String elo){
        this.nome = nome;
        this.elo = elo;
    }

    public Invocador(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getElo() {
        return elo;
    }

    public void setElo(String elo) {
        this.elo = elo;
    }
}
