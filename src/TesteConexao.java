
import java.sql.Connection;
import java.sql.SQLException;

public class TesteConexao {

    public static void main(String[] args) {

        try {
            Connection conexao = ConexaoBanco.conectar();

            System.out.println("Conexão com PostgreSQL realizada!");

            conexao.close();

        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco!");
            System.out.println(e.getMessage());
        }
    }
}