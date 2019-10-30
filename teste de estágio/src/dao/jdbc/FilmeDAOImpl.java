package dao.jdbc;

import dao.FilmeDAO;
import entidades.Cliente;
import entidades.Filme;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;


public class FilmeDAOImpl implements FilmeDAO {

	@Override
	public void insert(Connection conn, Filme filme) throws Exception {
		
		//conversão da data
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataFormatada = dateFormat.format(filme.getDataLancamento());
		
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
        
        System.out.println("\nFilme inserido com sucesso. \n");
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
	public void edit(Connection conn, Integer idFilme) throws Exception {
		
		
		//montando a string sql

		
		Scanner scannerNome = new Scanner(System.in);
		System.out.println("Digita o novo nome do filme: ");
		String novoNome = scannerNome.nextLine();
		
		Scanner scannerData = new Scanner(System.in);
		System.out.println("Digita a data do lançamento nova, no formato DD/MM/YYYY: ");
		String novaData = scannerData.next();
		
		Scanner scannerDescricao = new Scanner(System.in);
		System.out.println("Digita a nova descrição do filme: ");
		String novaDescricao = scannerDescricao.next();
		
		//montando a string do update
		String sql = "update en_filme set nome = '";
		sql = sql.concat(novoNome).concat("', descricao = '").concat(novaDescricao).concat("', data_lancamento = '");
		sql=sql.concat(novaData).concat("' where id_filme = ").concat(Integer.toString(idFilme));
	
		//fazendo o update
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.execute();
        conn.commit();
        System.out.println("\nAlteração realizada com sucesso.\n");
  
	}

	@Override
	public void delete(Connection conn, Integer idFilme) throws Exception {
		
		//apagando da tabela de relação
		String sqlDeleteReAluguel = "delete from re_aluguel_filme where id_filme = ";
		sqlDeleteReAluguel=sqlDeleteReAluguel.concat(Integer.toString(idFilme));
        PreparedStatement DeleteRe = conn.prepareStatement(sqlDeleteReAluguel);
        DeleteRe.execute();
		
        //apagando da tabela de filme
		String sql = "delete from en_filme where id_filme = " ;
		sql=sql.concat(Integer.toString(idFilme));
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.execute();

        conn.commit();
		System.out.println("\nExclusão realizada com sucesso.\n");
	}

	@Override
	public Filme find(Connection conn, Integer idFilme) throws Exception {
		
		//montando a string sql
		String sql = "select * from en_filme where id_filme = ";
		sql=sql.concat(Integer.toString(idFilme));
	    PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet myRs = ps.executeQuery();

        if (!myRs.next()) {
        	System.out.println("Erro, ID não encontrado.\n");
            return null;
        }

        String nome = myRs.getString("nome");
        Date dataLancamento = myRs.getDate("data_lancamento");
        String descricao=myRs.getString("descricao");
        return new Filme(idFilme,dataLancamento,nome,descricao);
	}


	@Override
	public Collection<Filme> list(Connection conn) throws Exception {
		
		String sql = "select * from en_filme order by id_filme";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet resultLista = ps.executeQuery();

        Collection<Filme> items = new ArrayList<>();

        while (resultLista.next()) {
            Integer idFilme = resultLista.getInt("id_filme");
            String nome = resultLista.getString("nome");
            Date dataLancamento = resultLista.getDate("data_lancamento");
            String descricao=resultLista.getString("descricao");

            items.add(new Filme(idFilme, dataLancamento,nome,descricao));
        }

        return items;
    }
}




