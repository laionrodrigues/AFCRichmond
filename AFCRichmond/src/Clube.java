import java.sql.SQLException;
import java.util.ArrayList;

public class Clube {

    private String nome = "AFC Richmond";
    private String cnpj = "63.984.512/0001-50";
    private String email = "afc@richmond.com.br";
    private String telefone = "+44 7911 123456";
    private String estadio = "Nelson Road";
    private String cidade = "Londres";
    private String endereco = "Selhurst Park, London, SE25 6PU, Inglaterra";

    private SocioDAO socioDAO = new SocioDAO();

    // Mensagem do último erro de banco (null = a última operação deu certo).
    // A interface gráfica usa isso para avisar o usuário.
    private String ultimoErro;

    public String getNome() {
        return nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEstadio() {
        return estadio;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getUltimoErro() {
        return ultimoErro;
    }

    public boolean adicionarSocio(SocioTorcedor torcedor) {

        ultimoErro = null;

        try {
            socioDAO.cadastrar(torcedor);
            System.out.println("Sócio salvo no banco de dados!");
            return true;

        } catch (SQLException e) {
            ultimoErro = e.getMessage();
            System.out.println("Erro ao cadastrar sócio: " + e.getMessage());
            return false;
        }
    }

    public boolean atualizarSocio(SocioTorcedor torcedor) {

        ultimoErro = null;

        try {
            if (!socioDAO.atualizar(torcedor)) {
                ultimoErro = "Sócio não encontrado no banco de dados.";
                System.out.println("Erro ao atualizar sócio: " + ultimoErro);
                return false;
            }
            return true;

        } catch (SQLException e) {
            ultimoErro = e.getMessage();
            System.out.println("Erro ao atualizar sócio: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<SocioTorcedor> getTorcedores() {

        ultimoErro = null;

        try {
            return socioDAO.listar();

        } catch (SQLException e) {
            ultimoErro = e.getMessage();
            System.out.println("Erro ao buscar sócios: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public SocioTorcedor buscarPorCpf(String cpf) {

        ultimoErro = null;

        try {
            return socioDAO.buscarPorCpf(cpf);

        } catch (SQLException e) {
            ultimoErro = e.getMessage();
            System.out.println("Erro ao buscar sócio: " + e.getMessage());
            return null;
        }
    }

    public void listarTorcedores() {

        ArrayList<SocioTorcedor> torcedores = getTorcedores();

        if (torcedores.isEmpty()) {
            System.out.println("Nenhum sócio cadastrado.");
            return;
        }

        for (SocioTorcedor torcedor : torcedores) {
            System.out.println(torcedor);
        }
    }

    public int quantidadeTorcedores() {

        ultimoErro = null;

        try {
            return socioDAO.quantidade();

        } catch (SQLException e) {
            ultimoErro = e.getMessage();
            System.out.println("Erro ao contar sócios: " + e.getMessage());
            return 0;
        }
    }
}
