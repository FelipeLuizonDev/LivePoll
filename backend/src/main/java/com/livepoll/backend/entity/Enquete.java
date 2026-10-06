package com.livepoll.backend.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "enquete")
public class Enquete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private Boolean aberta;

    @OneToMany(
            mappedBy = "enquete",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Opcao> opcoes = new ArrayList<>();

    public Enquete() {
    }

    public Enquete(String titulo) {
        this.titulo = titulo;
        this.aberta = true;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Boolean getAberta() {
        return aberta;
    }

    public void setAberta(Boolean aberta) {
        this.aberta = aberta;
    }

    public List<Opcao> getOpcoes() {
        return opcoes;
    }

    public void setOpcoes(List<Opcao> opcoes) {
        this.opcoes = opcoes;
    }

    public void adicionarOpcao(Opcao opcao) {
        opcoes.add(opcao);
        opcao.setEnquete(this);
    }
}
