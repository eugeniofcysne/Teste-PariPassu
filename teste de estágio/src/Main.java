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

			while (teste == false) {
				Scanner scanner1 = new Scanner(System.in);
				System.out.println("digita 1 para alugueis ou 2 para filmes");
				Integer decisao1 = scanner1.nextInt();
				switch (decisao1) {
				// aqui começa o aluguel
				case 1:
					Scanner scannerAluguel = new Scanner(System.in);
					System.out.println("Digita 1 para inserir um aluguel. \n");
					System.out.println("Digita 2 para excluir um aluguel. \n");
					System.out.println("Digita 3 para editar um aluguel. \n");
					System.out.println("Digita 4 para procurar um aluguel específico. \n");
					System.out.println("Digita 5 para listar todos os aluguéis. \n");
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

						// passando lista vazia e inserindo os filmes da relação dentro do método
						// reInsereAluguel
//                		
//                		Boolean outrosFilmes = false;
//                		Integer contaFilmes = 1;
//                		Integer idFilme=null;
//                		while (outrosFilmes == false) {
//                			
//                			System.out.println("Digita o id do filme "+ Integer.toString(contaFilmes)+" :");
//                    		scannerAluguel1 = new Scanner(System.in);
//                    		idFilme=scannerAluguel1.nextInt();
//                    		
//                    		Filme filme = FilmeDAOImpl.find(conn, idFilme); 

//                    		
//                    		
//		                    		//gerando a lista de filmes sem usar a função find
//		                    		String sql = "select * from en_filme where id_filme = ";
//		                    		sql=sql.concat(Integer.toString(idFilme));
//		                    		PreparedStatement ps = conn.prepareStatement(sql);
//		                            ResultSet myRs = ps.executeQuery();
//		                            String nome = myRs.getString("nome");
//		                            Date dataLancamento = myRs.getDate("dataLancamento");
//		                            String descricao=myRs.getString("descricao");
//		                            
//                    		listaFilmesAluguel.add(new Filme(idFilme, dataLancamento, nome, descricao)); //listaFilmesAluguel.add(filme)
//                    		
//                    		System.out.println("Deseja incluir novo filme? 's' para SIM, qualquer outra coisa para NÃO.");
//                    		scannerAluguel1 = new Scanner(System.in);
//                    		if(scannerAluguel1.next()=="s") {
//                    			contaFilmes++;
//                    		}else {
//                    			outrosFilmes = true;
//                    		}
//                    		
//                		}

						Scanner scannerAluguel3 = new Scanner(System.in);
						System.out.println("Digita o valor total do aluguel: ");
						Float valor = scannerAluguel3.nextFloat();

						Integer nextId = aluguelDAO.getNextId(conn);

						Aluguel aluguel = new Aluguel(nextId, filmesAluguel, cliente, dataAluguel, valor);

						aluguelDAO.insert(conn, aluguel); // aqui dentro é gerada a lista

						aluguel = aluguelDAO.find(conn, nextId); // o aluguel é reconstruído com a lista de filmes. no
																	// momento não é usada para nada esta linha

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
						Collection<Aluguel> aluguelCompleto =aluguelDAO.list(conn);
						System.out.println("Imprimindo lista completa de aluguéis. \n");
						for (Aluguel aluguelTemp : aluguelCompleto) {
							System.out.println(aluguelTemp.toString());
						}
						
						break;
					default:
					}
					break;
				// Aqui começa o processamento do filme
				case 2:
					Scanner scannerFilme = new Scanner(System.in);
					System.out.println("Digita 1 para inserir um filme. \n");
					System.out.println("Digita 2 para excluir um filme. \n");
					System.out.println("Digita 3 para editar um filme. \n");
					System.out.println("Digita 4 para procurar um filme específico. \n");
					System.out.println("Digita 5 para listar todos os filmes. \n");
					Integer decisaoFilme = scannerFilme.nextInt();
					switch (decisaoFilme) {
					case 1:
						System.out.println("Inserindo novo filme. \n");

						Scanner scannerFilme1 = new Scanner(System.in);
						System.out.println("Digita o id do filme novo: ");
						Integer idFilme = scannerFilme1.nextInt();


						Scanner scannerFilme2 = new Scanner(System.in);
						System.out.println("Digita a data do lançamento, no formato DD/MM/YYYY: ");
						String dataFilmeString = scannerFilme2.next();
						SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
						Date dataFilme = formatoData.parse(dataFilmeString);

						Filme filmeTeste = new Filme(1, dataFilme, "nomedofilme", "descricaodofilme");


						break;
					case 2:// excluir
						break;
					case 3:// editar
						break;
					case 4:// procurar
						break;
					case 5:// listar
						break;
					default:
						System.out.println("Valor inválido. \n");
					}
					break;
				default:
					System.out.println("Valor inválido. \n");
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