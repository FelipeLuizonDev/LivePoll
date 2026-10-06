package com.livepoll.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "opcao")
public class Opcao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "texto_opcao", nullable = false)
    private String textoOpcao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_enquete", nullable = false)
    private Enquete enquete;

    public Opcao() {
    }

    public Opcao(String textoOpcao) {
        this.textoOpcao = textoOpcao;
    }

    public Long getId() {
        return id;
    }

    public String getTextoOpcao() {
        return textoOpcao;
    }

    public void setTextoOpcao(String textoOpcao) {
        this.textoOpcao = textoOpcao;
    }

    public Enquete getEnquete() {
        return enquete;
    }

    public void setEnquete(Enquete enquete) {
        this.enquete = enquete;
    }
}
