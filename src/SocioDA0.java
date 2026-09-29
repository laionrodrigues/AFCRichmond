import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

class SocioDAO {


    public void cadastrar(SocioTorcedor socio) throws SQLException {

        String sqlNumero = "SELECT COALESCE(MAX(numero_socio), 0) + 1 FROM socios";

        String sqlInsert = """
                INSERT INTO socios
                (numero_socio, nome, cpf, email, telefone,
                 data_nascimento, data_cadastro, plano, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement consulta = conexao.prepareStatement(sqlNumero);
             ResultSet resultado = consulta.executeQuery()) {

            if (resultado.next()) {
                socio.setNumeroSocio(resultado.getInt(1));
            }
        }

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sqlInsert)) {

            comando.setInt(1, socio.getNumeroSocio());
            comando.setString(2, socio.getNome());
            comando.setString(3, socio.getCpf());
            comando.setString(4, socio.getEmail());
            comando.setString(5, socio.getTelefone());
            comando.setObject(6, socio.getDataNascimento());
            comando.setObject(7, socio.getDataCadastro());
            comando.setString(8, socio.getPlano());
            comando.setString(9, socio.getStatus().name());

            comando.executeUpdate();
        }
    }

    public ArrayList<SocioTorcedor> listar() throws SQLException {

        ArrayList<SocioTorcedor> socios = new ArrayList<>();

        String sql = "SELECT * FROM socios ORDER BY numero_socio";

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {

            while (resultado.next()) {

                SocioTorcedor socio = new SocioTorcedor();

                socio.setNumeroSocio(resultado.getInt("numero_socio"));
                socio.setNome(resultado.getString("nome"));
                socio.setCpf(resultado.getString("cpf"));
                socio.setEmail(resultado.getString("email"));
                socio.setTelefone(resultado.getString("telefone"));
                socio.setDataNascimento(
                        resultado.getObject("data_nascimento",
                                java.time.LocalDate.class));

                socio.setDataCadastro(
                        resultado.getObject("data_cadastro",
                                java.time.LocalDate.class));

                socio.setPlano(resultado.getString("plano"));
                socio.setStatus(
                        StatusTorcedor.valueOf(resultado.getString("status")));

                socios.add(socio);
            }
        }

        return socios;
    }

    public SocioTorcedor buscarPorCpf(String cpf) throws SQLException {

        String sql = "SELECT * FROM socios WHERE cpf = ?";

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, cpf);

            try (ResultSet resultado = comando.executeQuery()) {

                if (resultado.next()) {

                    SocioTorcedor socio = new SocioTorcedor();

                    socio.setNumeroSocio(resultado.getInt("numero_socio"));
                    socio.setNome(resultado.getString("nome"));
                    socio.setCpf(resultado.getString("cpf"));
                    socio.setEmail(resultado.getString("email"));
                    socio.setTelefone(resultado.getString("telefone"));
                    socio.setDataNascimento(
                            resultado.getObject("data_nascimento",
                                    java.time.LocalDate.class));
                    socio.setDataCadastro(
                            resultado.getObject("data_cadastro",
                                    java.time.LocalDate.class));
                    socio.setPlano(resultado.getString("plano"));
                    socio.setStatus(
                            StatusTorcedor.valueOf(resultado.getString("status")));

                    return socio;
                }
            }
        }

        return null;
    }

    public int quantidade() throws SQLException {

        String sql = "SELECT COUNT(*) FROM socios";

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {

            if (resultado.next()) {
                return resultado.getInt(1);
            }
        }

        return 0;
    }
}