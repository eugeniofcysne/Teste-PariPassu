package dao;

import entidades.Filme;

import java.sql.Connection;
import java.util.Collection;

public interface FilmeDAO {

    void insert(Connection conn, Filme filme) throws Exception;

    Integer getNextId(Connection conn) throws Exception;

    void edit(Connection conn, Integer idFilme) throws Exception;

    void delete(Connection conn, Integer idFilme) throws Exception;

    Filme find(Connection conn, Integer idFilme) throws Exception;

    Collection<Filme> list(Connection conn) throws Exception;
    


}
