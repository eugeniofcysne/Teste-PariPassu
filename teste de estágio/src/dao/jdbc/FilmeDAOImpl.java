package dao.jdbc;

import dao.FilmeDAO;
import entidades.Cliente;
import entidades.Filme;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;


public class FilmeDAOImpl implements FilmeDAO {

	@Override
	public void insert(Connection conn, Filme filme) throws Exception {
		
		//conversão da data
		SimpleDateFormat in= new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");		 
		String dataFormatada = out.format(in.parse(filme.getDataLancamento().toString()));
		
		//pega proximo id filme
		
		Integer idFilme = this.getNextId(conn);
		
		//Construção da String 'sql' para realizar o insert
		String sql = "INSERT INTO en_filme (id_filme,data_lancamento,nome,descricao) VALUES (";
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
		
		
		//montando a string sql
		String sql = "update en_filme set nome = '";
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
		
		//apagando da tabela de relação
		String sqlDeleteReAluguel = "delete from en_re_aluguel_filme where id_filme = ";
		sqlDeleteReAluguel=sqlDeleteReAluguel.concat(Integer.toString(idFilme));
        PreparedStatement DeleteRe = conn.prepareStatement(sqlDeleteReAluguel);
        DeleteRe.execute();
		
        //apagando da tabela de filme
		String sql = "delete from en_filme where id_filme = " ;
		sql=sql.concat(Integer.toString(idFilme));
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.execute();

        conn.commit();
		
	}

	@Override
	public Filme find(Connection conn, Integer idFilme) throws Exception {
		
		//montando a string sql
		String sql = "select * from en_filme where id_filme = ";
		sql=sql.concat(Integer.toString(idFilme));
	    PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet myRs = ps.executeQuery();

        if (!myRs.next()) {
            return null;
        }

        String nome = myRs.getString("nome");
        Date dataLancamento = myRs.getDate("dataLancamento");
        String descricao=myRs.getString("descricao");
        return new Filme(idFilme,dataLancamento,nome,descricao);
	}


	@Override
	public Collection<Filme> list(Connection conn) throws Exception {
		
		String sql = "select * from en_filme order by nome";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet resultLista = ps.executeQuery();

        Collection<Filme> items = new ArrayList<>();

        while (resultLista.next()) {
            Integer idFilme = resultLista.getInt("id_filme");
            String nome = resultLista.getString("nome");
            Date dataLancamento = resultLista.getDate("dataLancamento");
            String descricao=resultLista.getString("descricao");

            items.add(new Filme(idFilme, dataLancamento,nome,descricao));
        }

        return items;
    }
}




