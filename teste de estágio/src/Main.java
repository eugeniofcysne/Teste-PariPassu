import dao.AluguelDAO;
import dao.ClienteDAO;
import dao.FilmeDAO;
import dao.jdbc.AluguelDAOImpl;
import dao.jdbc.ClienteDAOImpl;
import dao.jdbc.FilmeDAOImpl;
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
                	System.out.println("Digita 4 para procurar um aluguel espec�fico. \n");
                	System.out.println("Digita 5 para listar todos os alugu�is. \n");
                    Integer decisaoAluguel=scannerAluguel.nextInt();
                    switch(decisaoAluguel) {
                    case 1:
                		Scanner scannerAluguel1 = new Scanner(System.in);
                		System.out.println("Digita o id do cliente: ");
                		Integer idCliente = scannerAluguel1.nextInt();
                		
                		scannerAluguel1 = new Scanner(System.in);
                		System.out.println("Digita a data do aluguel, no formato DD/MM/YYYY: ");
                   		String dataAluguelString = scannerAluguel1.next();
                		SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");		 
                		Date dataAluguel = formatoData.parse(dataAluguelString);

                		Boolean outrosFilmes = false;
                		Integer contaFilmes = 1;
                		Integer idFilme=null;
                		List<Filme> listaFilmesAluguel = new ArrayList<>();
                		while (outrosFilmes == false) {
                			
                			System.out.println("Digita o id do filme "+ Integer.toString(contaFilmes)+" :");
                    		scannerAluguel1 = new Scanner(System.in);
                    		idFilme=scannerAluguel1.nextInt();
                    		
                    		Filme filme = FilmeDAOImpl.find(conn, idFilme); //ERROOOOO mudar m�todo para Static?!?!
                    		
                    		
                    		//gerando a lista de filmes sem usar a fun��o find
                    		String sql = "select * from en_filme where id_filme = ";
                    		sql=sql.concat(Integer.toString(idFilme));
                    		PreparedStatement ps = conn.prepareStatement(sql);
                            ResultSet myRs = ps.executeQuery();
                            String nome = myRs.getString("nome");
                            Date dataLancamento = myRs.getDate("dataLancamento");
                            String descricao=myRs.getString("descricao");
                            
                    		listaFilmesAluguel.add(new Filme(idFilme, dataLancamento, nome, descricao)); //listaFilmesAluguel.add(filme)
                    		
                    		System.out.println("Deseja incluir novo filme? 's' para SIM, qualquer outra coisa para N�O.");
                    		scannerAluguel1 = new Scanner(System.in);
                    		if(scannerAluguel1.next()=="s") {
                    			contaFilmes++;
                    		}else {
                    			outrosFilmes = true;
                    		}
                    		
                		}
                		
                		scannerAluguel1 = new Scanner(System.in);
                		System.out.println("Digita o valor total do aluguel: ");
                   		Float valor = scannerAluguel1.nextFloat();
                   		
                   		
                   		Integer nextId = AluguelDAOImpl.getNextId(conn); //ERROOOOOO mudar m�todo para STATIC?!?!
                		
                		
                		
                		
                		
                		
                		
                    	break;
                    case 2:
                    	break;
                    case 3:
                    	break;
                    case 4:
                    	break;
                    case 5:
                    	break;
                	default:
                    }
                	break;
                case 2:
                	break;
            	default:
                	System.out.println("Valor inv�lido. \n");
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