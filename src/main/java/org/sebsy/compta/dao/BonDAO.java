package org.sebsy.compta.dao;

import java.util.Optional;

public interface BonDAO {

    Optional<Integer> FindDelaiByIdBon(long idBon) throws Exception;
}
