package org.sebsy.compta.dao;

import org.sebsy.compta.domain.Fournisseur;

import java.util.List;
import java.util.Optional;

public interface FournisseurDAO {

    List<Fournisseur> findAll() throws Exception;
    Optional<Fournisseur> findById(Long id) throws Exception;
    Optional<Fournisseur> findByNom(String nom) throws Exception;
    Optional<Fournisseur> findByVille(String ville) throws Exception;
}
