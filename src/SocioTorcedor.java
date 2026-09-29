
import java.time.LocalDate;

public class SocioTorcedor {

    private int numeroSocio;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private LocalDate dataNascimento;
    private LocalDate dataCadastro;
    private String plano;
    private StatusTorcedor status;

    // Construtor padrão
    public SocioTorcedor() {
        this.status = StatusTorcedor.ATIVO;
        this.dataCadastro = LocalDate.now();
    }

    // Construtor sobrecarregado
    public SocioTorcedor(String nome, String cpf, String email) {
        this();

        setNome(nome);
        setCpf(cpf);
        setEmail(email);
    }

    // Getters e setters

    public int getNumeroSocio() {
        return numeroSocio;
    }

    public void setNumeroSocio(int numeroSocio) {
        this.numeroSocio = numeroSocio;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().length() < 3) {
            System.out.println("Erro: nome inválido.");
            return;
        }

        this.nome = nome.trim();
    }

    public String getCpf() {
        return cpf;
    }

    public boolean setCpf(String cpf) {
        if (cpf == null) {
            return false;
        }
        cpf = cpf.replaceAll("[^0-9]", "");
        if (cpf.length() != 11) {
            return false;
        }
        boolean todosIguais = true;
        for (int i = 1; i < cpf.length(); i++) {
            if (cpf.charAt(i) != cpf.charAt(0)) {
                todosIguais = false;
                break;
            }
        }
        if (todosIguais) {
            return false;
        }
        int soma = 0;
        int resto;
        for (int i = 1; i <= 9; i++) {
            soma += Integer.parseInt(cpf.substring(i - 1, i)) * (11 - i);
        }
        resto = (soma * 10) % 11;
        if (resto == 10 || resto == 11) {
            resto = 0;
        }
        if (resto != Integer.parseInt(cpf.substring(9, 10))) {
            return false;
        }
        soma = 0;
        for (int i = 1; i <= 10; i++) {
            soma += Integer.parseInt(cpf.substring(i - 1, i)) * (12 - i);
        }
        resto = (soma * 10) % 11;
        if (resto == 10 || resto == 11) {
            resto = 0;
        }
        if (resto != Integer.parseInt(cpf.substring(10, 11))) {
            return false;
        }
        this.cpf = cpf;
        return true;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")
                || !email.substring(email.indexOf("@")).contains(".")) {
            System.out.println("Erro: email inválido.");
            return;
        }

        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        if (telefone == null) {
            System.out.println("Erro: telefone inválido.");
            return;
        }

        telefone = telefone.replaceAll("[^0-9]", "");

        if (telefone.length() != 11) {
            System.out.println("Erro: telefone deve conter 11 números.");
            return;
        }

        this.telefone = telefone;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {

        if (dataNascimento == null) {
            System.out.println("Erro: data inválida.");
            return;
        }

        LocalDate hoje = LocalDate.now();

        if (dataNascimento.isAfter(hoje)) {
            System.out.println("Erro: data não pode estar no futuro.");
            return;
        }

        if (dataNascimento.isBefore(hoje.minusYears(120))) {
            System.out.println("Erro: data fora do intervalo permitido.");
            return;
        }

        this.dataNascimento = dataNascimento;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public void setStatus(StatusTorcedor status) {
        this.status = status;
    }

    public String getPlano() {
        return plano;
    }

    public void setPlano(String plano) {
        if (plano == null || plano.trim().isEmpty()) {
            System.out.println("Erro: plano inválido.");
            return;
        }

        this.plano = plano;
    }

    public StatusTorcedor getStatus() {
        return status;
    }

    // Comportamentos do sócio

    public void cancelar() {
        if (status == StatusTorcedor.CANCELADO) {
            System.out.println("Este sócio já está cancelado.");
            return;
        }

        status = StatusTorcedor.CANCELADO;
        System.out.println("Sócio cancelado com sucesso!");
    }

    public void reativar() {
        if (status == StatusTorcedor.ATIVO) {
            System.out.println("Este sócio já está ativo.");
            return;
        }

        status = StatusTorcedor.ATIVO;
        System.out.println("Sócio reativado com sucesso!");
    }

    @Override
    public String toString() {
        return "\n===== DADOS DO SÓCIO =====" +
                "\nNúmero: " + numeroSocio +
                "\nNome: " + nome +
                "\nCPF: " + cpf +
                "\nEmail: " + email +
                "\nTelefone: " + telefone +
                "\nNascimento: " + dataNascimento +
                "\nCadastro: " + dataCadastro +
                "\nPlano: " + plano +
                "\nStatus: " + status;
    }
}