public class SocioTorcedor {

    private String id;
    private String nome;
    private String Cpf;
    private String email;
    private String telefone;
    private String dataNascimento;
    private String dataCadastro;
    private String plano;
    private StatusTorcedor status;

    public SocioTorcedor() {
        this.status = StatusTorcedor.ATIVO;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return Cpf;
    }

    public boolean setCpf(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");
        if (cpf.length() != 11) return false;
        int soma = 0, resto;
        for (int i = 1; i <= 9; i++) soma += Integer.parseInt(cpf.substring(i-1,i)) * (11 - i);
        resto = (soma * 10) % 11;
        if (resto == 10 || resto == 11) resto = 0;
        if (resto != Integer.parseInt(cpf.substring(9,10))) return false;
        soma = 0;
        for (int i = 1; i <= 10; i++) soma += Integer.parseInt(cpf.substring(i-1,i)) * (12 - i);
        resto = (soma * 10) % 11;
        if (resto == 10 || resto == 11) resto = 0;
        return resto == Integer.parseInt(cpf.substring(10,11));
    }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public StatusTorcedor getStatus() {
        return status;
    }

    public void setStatus(StatusTorcedor status) {
        this.status = status;
    }

    public String getPlano() {
        return plano;
    }

    public void setPlano(String plano) {
        this.plano = plano;
    }

    @Override
    public String toString(){
        return "Nome: " + getNome() +
                "\nCpf: " + getCpf() +
                "\nEmail: " + getEmail() +
                "\nTelefone: " + getTelefone() +
                "\nData de nascimento: " + getDataNascimento()+
                "\nPlano de socio: " + getPlano();
    }

}
