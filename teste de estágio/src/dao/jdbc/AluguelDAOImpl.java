package dao.jdbc;

import dao.AluguelDAO;
import dao.FilmeDAO;
import entidades.Cliente;
import entidades.Filme;
import entidades.Aluguel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public class AluguelDAOImpl implements AluguelDAO {

	@Override
	public void insert(Connection conn, Aluguel aluguel) throws Exception {

		// conversão da data

		SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = out.format(in.parse(aluguel.getDataAluguel().toString()));

		// pega proximo id aluguel

		Integer idAluguel = this.getNextId(conn);
		Cliente cliente = aluguel.getCliente();
		Integer idCliente = cliente.getIdCliente();

		// Construção da String 'sql' para realizar o insert

		String sql = "INSERT INTO en_aluguel (id_aluguel,id_cliente, data_aluguel,valor) VALUES (";
		sql = sql.concat(Integer.toString(idAluguel));
		sql = sql.concat(",'");
		sql = sql.concat(Integer.toString(idCliente));
		sql = sql.concat(",'");
		sql = sql.concat(dataFormatada);
		sql = sql.concat("','");
		sql = sql.concat(Float.toString(aluguel.getValor()));
		sql = sql.concat("');");

		// Prepara a instrução SQL

		PreparedStatement ps = conn.prepareStatement(sql);

		// Executa a instrução SQL

		ps.execute();
		conn.commit();

		// Insere a relação de filmes
		
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

		// inserir a relação de filmes daquele aluguel
		
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
			PreparedStatement verificaIdF = conn.prepareStatement(verificaIdFilme);
			ResultSet myRs = verificaIdF.executeQuery();
			if (!myRs.next()) {
				System.out.println("Este id_filme não existe!");
				contadorDeFilmes--;
			} else {
				String inserirFilmeNaRelacao = "INSERT INTO re_aluguel_filme (id_filme, id_aluguel) VALUES (";
				inserirFilmeNaRelacao = inserirFilmeNaRelacao.concat(Integer.toString(idFilme));
				inserirFilmeNaRelacao = inserirFilmeNaRelacao.concat(", ");
				inserirFilmeNaRelacao = inserirFilmeNaRelacao.concat(Integer.toString(idAluguel));
				PreparedStatement InserirFNaR = conn.prepareStatement(inserirFilmeNaRelacao);
				InserirFNaR.execute();
				conn.commit();
			}
			contadorDeFilmes++;
			Scanner scanner2 = new Scanner(System.in);
			System.out.println("Deseja inserir no filme? digite s para sim, ou qualquer outra coisa para não");
			if (scanner2.next() == "s" || scanner2.next() == "S") {
				System.out.println("Ok! Nova inserção. \n");
			} else {
				System.out.println("Ok! Fim da inserção. \n");
				EOL = true;
			}
		}
	}

	@Override
	public void edit(Connection conn, Aluguel aluguel) throws Exception {

		Integer idAluguel = aluguel.getIdAluguel();

		//apagando da tabela de relação
		String sqlDeleteReAluguel = "delete from en_re_aluguel_filme where id_aluguel = ";
		sqlDeleteReAluguel=sqlDeleteReAluguel.concat(Integer.toString(idAluguel));
        PreparedStatement DeleteRe = conn.prepareStatement(sqlDeleteReAluguel);
        DeleteRe.execute();
		
        //inserir nova relação
        System.out.println("Inserir nova relação de filmes. \n");
        this.insereReAluguel(conn, idAluguel);

        conn.commit();
		
		
	}

	@Override
	public void delete(Connection conn, Aluguel aluguel) throws Exception {
		Integer idAluguel = aluguel.getIdAluguel();

		String deleteReAluguel = "delete from en_re_aluguel_filme where id_aluguel = ";
		deleteReAluguel = deleteReAluguel.concat(Integer.toString(idAluguel));

	}

	@Override
	public Aluguel find(Connection conn, Integer idAluguel) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Aluguel> list(Connection conn) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
