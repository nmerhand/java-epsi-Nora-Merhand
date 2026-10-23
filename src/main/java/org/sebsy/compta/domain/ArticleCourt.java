package org.sebsy.compta.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "ArticleCourt")
@Table(name = "article")
public class ArticleCourt {

    @Id
    String id;     // la colonne est un BIGINT
    String ref;

    @Override
    public String toString() {
        return "ArticleCourt{" +
                "id=" + id +
                ", ref='" + ref + '\'' +
                '}';
    }
}