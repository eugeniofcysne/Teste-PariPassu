package dao.jdbc;

import dao.FilmeDAO;
import entidades.Filme;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;


public class FilmeDAOImpl implements FilmeDAO {

	@Override
	public void insert(Connection conn, Filme filme) throws Exception {
		
		//conversão da data
		SimpleDateFormat in= new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");		 
		String dataFormatada = out.format(in.parse(filme.getDataLancamento().toString()));
		
		//pega id filme
		
		Integer idFilme = this.getNextId(conn);
		
		//Construção da String 'sql' para realizar o insert
		String sql = "INSERT INTO filme (id_filme,data_lancamento,nome,descricao) VALUES (";
		sql = sql.concat(Integer.toString(idFilme));
		sql = sql.concat(",'");
		sql = sql.concat(dataFormatada);
		sql = sql.concat("','");
		sql = sql.concat(filme.getNome());
		sql = sql.concat("','");	
		sql = sql.concat(filme.getDescricao());
		sql = sql.concat("');");
		//Prepara a instrução SQL
		PreparedStatement ps = conn.prepareStatement(sql);
		//Executa a instrução SQL
		ps.execute();
        conn.commit();
	}

	@Override
	public Integer getNextId(Connection conn) throws Exception {
		String sql = "select nextval('seq_en_cliente')";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1);

	}

	@Override
	public void edit(Connection conn, Integer idFilme, String novoNome) throws Exception {
		
		
		//motando a string sql
		String sql = "update en_filme set nome = ('";
		sql = sql.concat(novoNome);
		sql = sql.concat("' where id_filme = ");	
		sql = sql.concat(Integer.toString(idFilme));
		
		//fazendo o update
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.execute();
        conn.commit();
  
	}

	@Override
	public void delete(Connection conn, Integer idFilme) throws Exception {
		
		String sql = "delete from en_filme where id_filme = " ;
		sql=sql.concat(Integer.toString(idFilme));
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.execute();
        conn.commit();
		
	}

	@Override
	public Filme find(Connection conn, Integer idFilme) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Filme> list(Connection conn) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
