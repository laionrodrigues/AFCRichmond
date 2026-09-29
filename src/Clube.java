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

    public void adicionarSocio(SocioTorcedor torcedor) {

        try {
            socioDAO.cadastrar(torcedor);
            System.out.println("Sócio salvo no banco de dados!");

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar sócio: " + e.getMessage());
        }
    }

    public ArrayList<SocioTorcedor> getTorcedores() {

        try {
            return socioDAO.listar();

        } catch (SQLException e) {
            System.out.println("Erro ao buscar sócios: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public SocioTorcedor buscarPorCpf(String cpf) {

        try {
            return socioDAO.buscarPorCpf(cpf);

        } catch (SQLException e) {
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

        try {
            return socioDAO.quantidade();

        } catch (SQLException e) {
            System.out.println("Erro ao contar sócios: " + e.getMessage());
            return 0;
        }
    }
}