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
import java.util.Date;
import java.util.List;
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

            //Demonstrar o funcionamento aqui
            
            Boolean teste = false;
            
            ClienteDAO clienteDAO = new ClienteDAOImpl();
            AluguelDAO aluguelDAO = new AluguelDAOImpl();
            FilmeDAO filmeDAO = new FilmeDAOImpl();
            
            
            while(teste==false) {
        		Scanner scanner1 = new Scanner(System.in);
        		System.out.println("digita 1 para alugueis ou 2 para filmes");
                Integer decisao1=scanner1.nextInt();
                switch(decisao1) {
                case 1:
                	Scanner scannerAluguel = new Scanner(System.in);
                	System.out.println("Digita 1 para inserir um aluguel. \n");
                	System.out.println("Digita 2 para excluir um aluguel. \n");
                	System.out.println("Digita 3 para editar um aluguel. \n");
                	System.out.println("Digita 4 para procurar um aluguel específico. \n");
                	System.out.println("Digita 5 para listar todos os aluguéis. \n");
                    Integer decisaoAluguel=scannerAluguel.nextInt();
                    switch(decisaoAluguel) {
                    case 1:
                    	System.out.println("Inserindo novo aluguel. \n");
                    	
                		Scanner scannerAluguel1 = new Scanner(System.in);
                		System.out.println("Digita o id do cliente: ");
                		Integer idCliente = scannerAluguel1.nextInt();
                		
                		Cliente cliente = ClienteDAOImpl.find(conn, idCliente);//ERROOOOO mudar método para Static?!?!
                		
                		scannerAluguel1 = new Scanner(System.in);
                		System.out.println("Digita a data do aluguel, no formato DD/MM/YYYY: ");
                   		String dataAluguelString = scannerAluguel1.next();
                		SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");		 
                		Date dataAluguel = formatoData.parse(dataAluguelString);

                		Boolean outrosFilmes = false;
                		Integer contaFilmes = 1;
                		Integer idFilme=null;
                		List<Filme> listaFilmesAluguel = new ArrayList<>();
                		//passando lista vazia e inserindo os filmes da relação dentro do método reInsereAluguel
//                		while (outrosFilmes == false) {
//                			
//                			System.out.println("Digita o id do filme "+ Integer.toString(contaFilmes)+" :");
//                    		scannerAluguel1 = new Scanner(System.in);
//                    		idFilme=scannerAluguel1.nextInt();
//                    		
//                    		Filme filme = FilmeDAOImpl.find(conn, idFilme); //ERROOOOO mudar método para Static?!?!
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
                		
                		scannerAluguel1 = new Scanner(System.in);
                		System.out.println("Digita o valor total do aluguel: ");
                   		Float valor = scannerAluguel1.nextFloat();                   		
                   		
                   		Integer nextId = AluguelDAOImpl.getNextId(conn); //ERROOOOOO mudar método para STATIC?!?!
                		
                		Aluguel aluguel = new Aluguel(nextId, listaFilmesAluguel, cliente, dataAluguel, valor);
                		
                		AluguelDAOImpl.insert(conn, aluguel); //ERROOOOOO mudar método para STATIC?!?!
                		
                		//reconstruir aluguel com a lista de filmes completa gerada no método reInsereAluguel
                		
                		aluguel = AluguelDAOImpl.find(conn, idAluguel);//ERROOOOOO mudar método para STATIC?!?!
                		
                    	break;
                    case 2://excluir
                    	System.out.println("Excluindo aluguel. \n");
                    	
                    	Scanner scannerAluguel2 = new Scanner(System.in);
                    	System.out.println("Digite o id do aluguel que quer excluir: ");
                    	
                    	Integer idAluguel2 = scannerAluguel2.nextInt();
                    	Aluguel aluguel2 = AluguelDAOImpl.find(conn, idAluguel2);//ERROOOOOO mudar método para STATIC?!?!
                    	
                    	AluguelDAOImpl.delete(conn, aluguel2);//ERROOOOOO mudar método para STATIC?!?!
                    	
                    	break;
                    case 3://editar
                    	System.out.println("Editando aluguel. \n");
                    	
                    	Scanner scannerAluguel3 = new Scanner(System.in);
                    	System.out.println("Digite o id do aluguel que quer excluir: ");
                    	Integer idAluguel3 = scannerAluguel3.nextInt();
                    	Aluguel aluguel3 = AluguelDAOImpl.find(conn, idAluguel3);//ERROOOOOO mudar método para STATIC?!?!
                    	
                    	AluguelDAOImpl.edit(conn, aluguel3);//ERROOOOOO mudar método para STATIC?!?!
                    	
                    	
                    	break;
                    case 4://procurar
                    	System.out.println("Procurando aluguel. \n");
                    	
                    	Scanner scannerAluguel4 = new Scanner(System.in);
                    	System.out.println("Digite o id do aluguel que quer procurar: ");
                    	Integer idAluguel4 = scannerAluguel4.nextInt();
                    	Aluguel aluguel4 = AluguelDAOImpl.find(conn, idAluguel4);//ERROOOOOO mudar método para STATIC?!?!
                    	
                    	
                    	break;
                    case 5://listar
                    	break;
                	default:
                    }
                	break;
                case 2:
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