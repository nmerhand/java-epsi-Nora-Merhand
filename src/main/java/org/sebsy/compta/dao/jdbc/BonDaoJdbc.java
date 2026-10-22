package org.sebsy.compta.dao.jdbc;

import org.sebsy.compta.dao.BonDAO;
import org.sebsy.compta.domain.Adresse;
import org.sebsy.compta.domain.Fournisseur;
import org.sebsy.compta.infra.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class BonDaoJdbc implements BonDAO {

    private static final String SQL_FIND_DELAI_BY_ID_BON = "SELECT delai FROM bon WHERE id = ?" ;

    @Override
    public Optional<Integer> FindDelaiByIdBon(long idBon) throws SQLException {

        try (Connection cnx = Db.dataSource().getConnection();
             PreparedStatement ps = cnx.prepareStatement(SQL_FIND_DELAI_BY_ID_BON)) {

            ps.setLong(1, idBon);

            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return Optional.ofNullable(rs.getObject("delai", Integer.class));
                }
                return  Optional.empty();
            }
        }
    }
}
