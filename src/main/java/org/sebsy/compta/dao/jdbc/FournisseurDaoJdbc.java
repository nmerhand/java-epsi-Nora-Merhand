package org.sebsy.compta.dao.jdbc;

import org.sebsy.compta.dao.FournisseurDAO;
import org.sebsy.compta.domain.Adresse;
import org.sebsy.compta.domain.Fournisseur;
import org.sebsy.compta.infra.Db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FournisseurDaoJdbc implements FournisseurDAO {

    private static final String SQL_FIND_ALL = "SELECT * FROM compta.fournisseur";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM compta.fournisseur WHERE id = ?";
    private static final String SQL_FIND_BY_NOM = "SELECT * FROM compta.fournisseur WHERE nom = ?";
    private static final String SQL_FIND_BY_VILLE = "SELECT * FROM compta.fournisseur WHERE ville = ?";
    private static final String SQL_CREATE = "INSERT INTO compta.fournisseur (nom, rue, code_postal, ville) VALUES (?, ?, ?, ?)";

    @Override
    public List<Fournisseur> findAll() throws SQLException {

        List<Fournisseur> fournisseurs = new ArrayList<>();

        try(Connection cnx = Db.dataSource().getConnection()) {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(SQL_FIND_ALL);

            while (rs.next()) {
                Long id = rs.getLong("id");
                String nom = rs.getString("nom");
                Adresse adresse = new Adresse(
                        rs.getString("rue"),
                        rs.getString("code_postal"),
                        rs.getString("ville"));
                Fournisseur fournisseur = new Fournisseur(id, nom, adresse);
                fournisseurs.add(fournisseur);
            }
        }
        return fournisseurs;
    }

    @Override
    public Optional<Fournisseur> findById(Long id) throws SQLException {

        try (Connection cnx = Db.dataSource().getConnection();
             PreparedStatement ps = cnx.prepareStatement(SQL_FIND_BY_ID)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    String nom = rs.getString("nom");
                    Adresse adresse = new Adresse(
                            rs.getString("rue"),
                            rs.getString("code_postal"),
                            rs.getString("ville"));
                    Fournisseur fournisseur = new Fournisseur(id, nom, adresse);
                    return Optional.of(fournisseur);
                }
                return  Optional.empty();
            }
        }
    }

    @Override
    public Optional<Fournisseur> findByNom(String nom) throws Exception {

        try (Connection cnx = Db.dataSource().getConnection();
             PreparedStatement ps = cnx.prepareStatement(SQL_FIND_BY_NOM)) {

            ps.setString(1, nom);

            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    Long id = rs.getLong("id");
                    Adresse adresse = new Adresse(
                            rs.getString("rue"),
                            rs.getString("code_postal"),
                            rs.getString("ville"));
                    Fournisseur fournisseur = new Fournisseur(id, nom, adresse);
                    return Optional.of(fournisseur);
                }
                return  Optional.empty();
            }
        }
    }

    @Override
    public Optional<Fournisseur> findByVille(String ville) throws Exception {

        try (Connection cnx = Db.dataSource().getConnection();
             PreparedStatement ps = cnx.prepareStatement(SQL_FIND_BY_VILLE)) {

            ps.setString(1, ville);

            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    Long id = rs.getLong("id");
                    String nom = rs.getString("nom");
                    Adresse adresse = new Adresse(
                            rs.getString("rue"),
                            rs.getString("code_postal"),
                            rs.getString("ville"));
                    Fournisseur fournisseur = new Fournisseur(id, nom, adresse);
                    return Optional.of(fournisseur);
                }
                return  Optional.empty();
            }
        }
    }

    @Override
    public Fournisseur create(Fournisseur f) throws Exception {

        try (Connection cnx = Db.dataSource().getConnection();
             PreparedStatement ps = cnx.prepareStatement(SQL_CREATE,  Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, f.getNom());
            ps.setString(2, f.getAdresse().rue());
            ps.setString(3, f.getAdresse().codePostal());
            ps.setString(4, f.getAdresse().ville());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                Long id = rs.getLong(1);
                Fournisseur fournisseur = new Fournisseur(id, f.getNom(), f.getAdresse());
                return  fournisseur;
            }
        }
    }

}
