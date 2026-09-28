
import java.util.ArrayList;

public class Clube {

    private String nome = "AFC Richmond";
    private String cnpj = "63.984.512/0001-50";
    private String email = "afc@richmond.com.br";
    private String telefone = "+44 7911 123456";
    private String estadio = "Nelson Road";
    private String cidade = "Londres";
    private String endereco = "Selhurst Park, London, SE25 6PU, Inglaterra";

    private ArrayList<SocioTorcedor> torcedores = new ArrayList<>();

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
        torcedores.add(torcedor);
    }

    public ArrayList<SocioTorcedor> getTorcedores() {
        return torcedores;
    }

    public SocioTorcedor buscarPorCpf(String cpf) {

        for (SocioTorcedor torcedor : torcedores) {

            if (torcedor.getCpf().equals(cpf)) {
                return torcedor;
            }
        }

        return null;
    }

    public void listarTorcedores() {

        if (torcedores.isEmpty()) {
            System.out.println("Nenhum sócio cadastrado.");
            return;
        }

        for (SocioTorcedor torcedor : torcedores) {
            System.out.println(torcedor);
        }
    }

    public int quantidadeTorcedores() {
        return torcedores.size();
    }
}