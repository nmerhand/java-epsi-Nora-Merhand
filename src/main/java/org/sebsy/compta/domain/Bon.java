package org.sebsy.compta.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.sebsy.compta.domain.Statut;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "bon")
public class Bon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private int numero;

    @Column(name = "date_cmde", updatable = false)
    private LocalDateTime dateCommande;

    private Integer delai;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Statut statut;

    protected Bon() {
    }

    public Bon(int numero, LocalDateTime dateCommande, Integer delai, Statut statut) {
        this.numero = numero;
        this.dateCommande = dateCommande;
        this.delai = delai;
        this.statut = statut;
    }

    public Long getId() {
        return id;
    }

    public int getNumero() {
        return numero;
    }

    public LocalDateTime getDateCommande() {
        return dateCommande;
    }

    public Integer getDelai() {
        return delai;
    }

    public void setDelai(Integer delai) {
        this.delai = delai;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Bon bon)) return false;
        return numero == bon.numero && Objects.equals(id, bon.id) && Objects.equals(dateCommande, bon.dateCommande) && Objects.equals(delai, bon.delai) && statut == bon.statut;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numero, dateCommande, delai, statut);
    }
}