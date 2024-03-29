package dao.jdbc;

import dao.AluguelDAO;
import dao.FilmeDAO;
import entidades.Cliente;
import entidades.Filme;
import entidades.Aluguel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class AluguelDAOImpl implements AluguelDAO {

	@Override
	public void insert(Connection conn, Aluguel aluguel) throws Exception {

		// convers�o da data
		
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataFormatada = dateFormat.format(aluguel.getDataAluguel());

		// pega proximo id aluguel

		Integer idAluguel = this.getNextId(conn);
		Cliente cliente = aluguel.getCliente();
		Integer idCliente = cliente.getIdCliente();

		// Constru��o da String 'sql' para realizar o insert

		String sql = "INSERT INTO en_aluguel (id_aluguel,id_cliente, data_aluguel,valor) VALUES (";
		sql = sql.concat(Integer.toString(idAluguel));
		sql = sql.concat(",");
		sql = sql.concat(Integer.toString(idCliente));
		sql = sql.concat(",'");
		sql = sql.concat(dataFormatada);
		sql = sql.concat("',");
		sql = sql.concat(Float.toString(aluguel.getValor()));
		sql = sql.concat(");");
		System.out.println(sql);
		// Prepara a instru��o SQL

		PreparedStatement ps = conn.prepareStatement(sql);

		// Executa a instru��o SQL

		ps.execute();
		conn.commit();

		// Insere a rela��o de filmes
		
		this.insereReAluguel(conn, idAluguel);

	}

	@Override
	public Integer getNextId(Connection conn) throws Exception {
		String sql = "select nextval('seq_en_aluguel')";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		rs.next();
		return rs.getInt(1);
	}

	@Override
	public void insereReAluguel(Connection conn, Integer idAluguel) throws Exception {

		// inserir a rela��o de filmes daquele aluguel
		
		Integer contadorDeFilmes = 1;
		Boolean EOL = false;// End of Loop
		while (EOL == false) {
			String fraseDaPergunta = "Digite o id do filme '";
			fraseDaPergunta = fraseDaPergunta.concat(Integer.toString(contadorDeFilmes));
			fraseDaPergunta = fraseDaPergunta.concat("'deste aluguel: ");
			Scanner scanner = new Scanner(System.in);
			System.out.println(fraseDaPergunta);
			Integer idFilme = scanner.nextInt();
			String verificaIdFilme = "select * from en_filme where id_filme = ";
			verificaIdFilme = verificaIdFilme.concat(Integer.toString(idFilme));
			System.out.println(verificaIdFilme);
			PreparedStatement verificaIdF = conn.prepareStatement(verificaIdFilme);
			ResultSet myRs = verificaIdF.executeQuery();
			if (!myRs.next()) {
				System.out.println("Este id_filme n�o existe!");
				contadorDeFilmes--;
			} else {
				String inserirFilmeNaRelacao = "INSERT INTO re_aluguel_filme (id_filme, id_aluguel) VALUES (";
				inserirFilmeNaRelacao = inserirFilmeNaRelacao.concat(Integer.toString(idFilme));
				inserirFilmeNaRelacao = inserirFilmeNaRelacao.concat(", ");
				inserirFilmeNaRelacao = inserirFilmeNaRelacao.concat(Integer.toString(idAluguel)).concat(")");
				PreparedStatement InserirFNaR = conn.prepareStatement(inserirFilmeNaRelacao);
				System.out.println("\n"+inserirFilmeNaRelacao);
				InserirFNaR.execute();
				conn.commit();
			}
			contadorDeFilmes++;
			Scanner scanner2 = new Scanner(System.in);
			System.out.println("Deseja inserir novo filme? digite 0 para sim, ou 1 para n�o");
			if (scanner2.nextInt() == 0) {
				System.out.println("Ok! Nova inser��o. \n");
			} else {
				System.out.println("Ok! Fim da inser��o. \n");
				EOL = true;
			}
		}
	}

	@Override
	public void edit(Connection conn, Aluguel aluguel) throws Exception { //obs.: m�todo para editar (refazer) a rela��o de filmes. para editar o aluguel inteiro, criar m�todo que apague o aluguel e insira um novo

		Integer idAluguel = aluguel.getIdAluguel();

		//apagando da tabela de rela��o
		String sqlDeleteReAluguel = "delete from re_aluguel_filme where id_aluguel = ";
		sqlDeleteReAluguel=sqlDeleteReAluguel.concat(Integer.toString(idAluguel));
        PreparedStatement DeleteRe = conn.prepareStatement(sqlDeleteReAluguel);
        DeleteRe.execute();
		
        //inserir nova rela��o
        System.out.println("Inserir nova rela��o de filmes. \n");
        this.insereReAluguel(conn, idAluguel);

        conn.commit();
				
	}

	@Override
	public void delete(Connection conn, Aluguel aluguel) throws Exception {
		Integer idAluguel = aluguel.getIdAluguel();

		String sqlDeleteReAluguel = "delete from re_aluguel_filme where id_aluguel = ";
		sqlDeleteReAluguel = sqlDeleteReAluguel.concat(Integer.toString(idAluguel));
        PreparedStatement deleteRe = conn.prepareStatement(sqlDeleteReAluguel);
        deleteRe.execute();
        
        String sqlDeleteAluguel = "delete from en_aluguel where id_aluguel = ";
        sqlDeleteAluguel = sqlDeleteAluguel.concat(Integer.toString(idAluguel));
        PreparedStatement deleteAluguel = conn.prepareStatement(sqlDeleteAluguel);
        deleteAluguel.execute();
        conn.commit();
        
        System.out.println("Aluguel id "+ Integer.toString(idAluguel) + " exlcu�do com sucesso. \n");
	}

	@Override
	public Aluguel find(Connection conn, Integer idAluguel) throws Exception {
		//montando a string sql
		String sql = "select * from en_aluguel where id_aluguel = ";
		sql=sql.concat(Integer.toString(idAluguel));

	    PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet myRs = ps.executeQuery();

        if (!myRs.next()) {
            return null;
        }else {

	        Integer idCliente = myRs.getInt("id_cliente");
	        
	        Date dataAluguel = myRs.getDate("data_aluguel");
	        Float valor=myRs.getFloat("valor");           
	        
	        String pegaCliente = "select a.nome from en_cliente a, en_aluguel b where a.id_cliente = b.id_cliente and b.id_cliente = ";
	        pegaCliente=pegaCliente.concat(Integer.toString(idCliente));
	        
		    PreparedStatement ps2 = conn.prepareStatement(pegaCliente);
	        ResultSet myRs2 = ps2.executeQuery();
	        if (!myRs2.next()) {
	            return null;
	        }else {	        
		        String nome = myRs2.getString("nome");
		        Cliente cliente = new Cliente(idCliente, nome);

		        List<Filme> filmes = filmesDoAluguel(conn, idAluguel);     
	               
		        return new Aluguel(idAluguel,filmes,cliente,dataAluguel,valor);
	        }
	        
        }
	}

	@Override
	public Collection<Aluguel> list(Connection conn) throws Exception {
		
		String sql = "select * from en_aluguel";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet resultLista = ps.executeQuery();

        Collection<Aluguel> items = new ArrayList<>();

        while (resultLista.next()) {
            Integer idAluguel = resultLista.getInt("id_aluguel");
            Integer idCliente = resultLista.getInt("id_cliente");
            Date dataAluguel = resultLista.getDate("data_aluguel");
            Float valor = resultLista.getFloat("valor");
            
            String sqlPegaNomeCliente="select b.nome from en_aluguel a, en_cliente b where a.id_cliente=b.id_cliente and a.id_aluguel =  ";
            sqlPegaNomeCliente=sqlPegaNomeCliente.concat((Integer.toString(idAluguel)));
            PreparedStatement pegaNomeCliente=conn.prepareStatement(sqlPegaNomeCliente);
	        ResultSet resultNomeCliente = pegaNomeCliente.executeQuery();
	        String nome=null;
	        while(resultNomeCliente.next()) {
	        	nome=resultNomeCliente.getString("nome");
	        }
            Cliente cliente = new Cliente(idCliente, nome);
            items.add(new Aluguel(idAluguel, filmesDoAluguel(conn, idAluguel),cliente,dataAluguel,valor));
        }

        return items;
	}

	@Override
	public Collection<Filme> criaListaAluguel(Connection conn, Integer idFilme[]) throws Exception {
		
		//m�todo para criar uma Collection<Filme> com os filmes que constam naquele aluguel, 
		//baseado em um vetor de inteiros com os ids de filme
		
		
		Collection<Filme> items = new ArrayList<>();
		
		for(Integer i = 0; i<idFilme.length;i++) {
			
			String sql = "select * from en_filme where id_filme = ";
			sql=sql.concat(Integer.toString(idFilme[i]));
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet myRs = ps.executeQuery();
			
			String nomeFilme = myRs.getString("nome");
            Date dataLancamento = myRs.getDate("data_lancamento");
            String descricao=myRs.getString("descricao");

            items.add(new Filme(idFilme[i], dataLancamento,nomeFilme,descricao));
		}
		return items;
	}
	
	// m�todo criado para economizar c�digo, este bloco aparece mais de 1x
	public List<Filme> filmesDoAluguel(Connection conn, Integer idAluguel) throws Exception{
		String sqlPegaFilmes="select a.id_filme, a.nome, a.data_lancamento, a.descricao from en_filme a,"
				+"re_aluguel_filme b where a.id_filme = b.id_filme and b.id_aluguel = ";
        sqlPegaFilmes=sqlPegaFilmes.concat((Integer.toString(idAluguel)));
        PreparedStatement pegaFilmes=conn.prepareStatement(sqlPegaFilmes);
        ResultSet filmesDoAluguel = pegaFilmes.executeQuery();
        
        List<Filme> filmes = new ArrayList<>();

        while (filmesDoAluguel.next()) {
            Integer idFilme = filmesDoAluguel.getInt("id_filme");
            String nomeFilme = filmesDoAluguel.getString("nome");
            Date dataLancamento = filmesDoAluguel.getDate("data_lancamento");
            String descricao=filmesDoAluguel.getString("descricao");

            filmes.add(new Filme(idFilme, dataLancamento,nomeFilme,descricao));
        }
        return filmes;
	}
}
