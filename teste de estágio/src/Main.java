import dao.AluguelDAO;
import dao.ClienteDAO;
import dao.FilmeDAO;
import dao.jdbc.AluguelDAOImpl;
import dao.jdbc.ClienteDAOImpl;
import dao.jdbc.FilmeDAOImpl;
import entidades.Aluguel;
import entidades.Cliente;
import entidades.Filme;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Connection conn = null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mydb", "postgres", "123456");
			conn.setAutoCommit(false);

			Boolean teste = false;

			ClienteDAO clienteDAO = new ClienteDAOImpl();
			AluguelDAO aluguelDAO = new AluguelDAOImpl();
			FilmeDAO filmeDAO = new FilmeDAOImpl();
			
			System.out.println("Ol�, esta � a aplica��o de teste do candidato Eug�nio Fiorelli Cysne");
			
			while (teste == false) {
				Scanner scanner1 = new Scanner(System.in);
				System.out.println("digita 1 para alugueis ou 2 para filmes");
				Integer decisao1 = scanner1.nextInt();
				switch (decisao1) {
				// aqui come�a o aluguel
				case 1:
					Scanner scannerAluguel = new Scanner(System.in);
					System.out.println("Digita 1 para inserir um aluguel. \n");
					System.out.println("Digita 2 para excluir um aluguel. \n");
					System.out.println("Digita 3 para editar um aluguel. \n");
					System.out.println("Digita 4 para procurar um aluguel espec�fico. \n");
					System.out.println("Digita 5 para listar todos os alugu�is. \n");
					Integer decisaoAluguel = scannerAluguel.nextInt();
					switch (decisaoAluguel) {
					case 1:
						System.out.println("Inserindo novo aluguel. \n");

						Scanner scannerAluguel1 = new Scanner(System.in);
						System.out.println("Digita o id do cliente: ");
						Integer idCliente = scannerAluguel1.nextInt();

						Cliente cliente = clienteDAO.find(conn, idCliente);

						Scanner scannerAluguel2 = new Scanner(System.in);
						System.out.println("Digita a data do aluguel, no formato DD/MM/YYYY: ");
						String dataAluguelString = scannerAluguel2.next();
						SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
						Date dataAluguel = formatoData.parse(dataAluguelString);

						Filme filmeTeste = new Filme(1, dataAluguel, "nomedofilme", "descricaodofilme");
						List<Filme> filmesAluguel = new ArrayList<>();
						filmesAluguel.add(filmeTeste);

						String testeString = "\n aqui teste" + dataAluguelString + "\n";
						System.out.println(testeString);

						// print de teste. remover depois!
						for (Filme filme : filmesAluguel) {
							System.out.println(filme.toString());
						}

						Scanner scannerAluguel3 = new Scanner(System.in);
						System.out.println("Digita o valor total do aluguel: ");
						Float valor = scannerAluguel3.nextFloat();

						Integer nextId = aluguelDAO.getNextId(conn);

						Aluguel aluguel = new Aluguel(nextId, filmesAluguel, cliente, dataAluguel, valor);

						aluguelDAO.insert(conn, aluguel); // aqui dentro � gerada a lista

						// na pr�x. linha o aluguel � reconstru�do com a lista de filmes. no momento n�o
						// � usada para nada esta linha
						aluguel = aluguelDAO.find(conn, nextId);

						break;
					case 2:// excluir
						System.out.println("Excluindo aluguel. \n");

						Scanner scannerAluguel4 = new Scanner(System.in);
						System.out.println("Digite o id do aluguel que quer excluir: ");

						Integer idAluguel2 = scannerAluguel4.nextInt();
						Aluguel aluguel2 = aluguelDAO.find(conn, idAluguel2);

						aluguelDAO.delete(conn, aluguel2);

						break;
					case 3:// editar
						System.out.println("Editando aluguel. \n");

						Scanner scannerAluguel5 = new Scanner(System.in);
						System.out.println("Digite o id do aluguel que quer excluir: ");
						Integer idAluguel3 = scannerAluguel5.nextInt();
						Aluguel aluguel3 = aluguelDAO.find(conn, idAluguel3);

						aluguelDAO.edit(conn, aluguel3);

						break;
					case 4:// procurar
						System.out.println("Procurando aluguel. \n");
						Scanner scannerAluguel6 = new Scanner(System.in);
						System.out.println("Digite o id do aluguel que quer procurar: ");
						Integer idAluguel4 = scannerAluguel6.nextInt();
						Aluguel aluguel4 = aluguelDAO.find(conn, idAluguel4);
						aluguel4.toString();
						break;
					case 5:// listar
						Collection<Aluguel> aluguelCompleto = aluguelDAO.list(conn);
						System.out.println("Imprimindo lista completa de alugu�is. \n");
						for (Aluguel aluguelTemp : aluguelCompleto) {
							System.out.println(aluguelTemp.toString());
						}

						break;
					default:
					}
					break;
				// Aqui come�a o processamento do filme
				case 2:
					Scanner scannerFilme = new Scanner(System.in);
					System.out.println("Digita 1 para inserir um filme. \n");
					System.out.println("Digita 2 para excluir um filme. \n");
					System.out.println("Digita 3 para editar um filme. \n");
					System.out.println("Digita 4 para procurar um filme espec�fico. \n");
					System.out.println("Digita 5 para listar todos os filmes. \n");
					Integer decisaoFilme = scannerFilme.nextInt();
					switch (decisaoFilme) {
					case 1://inserir
						System.out.println("Inserindo novo filme. \n");

						Integer idFilme = filmeDAO.getNextId(conn);

						Scanner scannerFilme1 = new Scanner(System.in);
						System.out.println("Digita a data do lan�amento, no formato DD/MM/YYYY: ");
						String dataFilmeString = scannerFilme1.next();
						SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
						Date dataFilme = formatoData.parse(dataFilmeString);

						Scanner scannerFilme2 = new Scanner(System.in);
						System.out.println("Digita o nome do filme: ");
						String nomeFilme = scannerFilme2.next();

						Scanner scannerFilme3 = new Scanner(System.in);
						System.out.println("Digita a descri��o do filme: ");
						String descricaoFilme = scannerFilme3.next();

						Filme novoFilme = new Filme(idFilme, dataFilme, nomeFilme, descricaoFilme);

						filmeDAO.insert(conn, novoFilme);

						break;
					case 2:// excluir
						System.out.println("Excluindo filme. \n");

						Scanner scannerFilme4 = new Scanner(System.in);
						System.out.println("Digite o id do filme que quer excluir: ");

						Integer idFilme2 = scannerFilme4.nextInt();

						filmeDAO.delete(conn, idFilme2);

						break;
					case 3:// editar
						System.out.println("Editando filme. \n");

						Scanner scannerFilme5 = new Scanner(System.in);
						System.out.println("Digite o id do filme que quer editar: ");
						Integer idFilme3 = scannerFilme5.nextInt();

						filmeDAO.edit(conn, idFilme3);
						break;
					case 4:// procurar
						System.out.println("Procurando filme. \n");
						Scanner scannerFilme6 = new Scanner(System.in);
						System.out.println("Digite o id do filme que quer procurar: ");
						Integer idFilme4 = scannerFilme6.nextInt();
						Filme filme4 = filmeDAO.find(conn, idFilme4);
						filme4.toString();
						break;
					case 5:// listar
						Collection<Filme> filmeCompleto = filmeDAO.list(conn);
						System.out.println("Imprimindo lista completa de filmes. \n");
						for (Filme filmeTemp : filmeCompleto) {
							System.out.println(filmeTemp.toString());
						}

						break;
					default:
						System.out.println("Valor inv�lido. \n");
					}
					break;
				default:
					System.out.println("Valor inv�lido. \n");
				}
				Scanner scannerTeste = new Scanner(System.in);
				System.out.println("Deseja executar novamente? 's' para sim, qualquer outra coisa para n�o: ");
				String verifTeste = scannerTeste.next();
				if (verifTeste != "s") {
					teste = true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Fim do teste.");
	}
}