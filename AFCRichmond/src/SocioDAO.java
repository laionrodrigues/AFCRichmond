import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SocioDAO {

    public void cadastrar(SocioTorcedor socio) throws SQLException {

        String sqlNumero = "SELECT COALESCE(MAX(numero_socio), 0) + 1 FROM socios";

        String sqlInsert = """
                INSERT INTO socios
                (numero_socio, nome, cpf, email, telefone,
                 data_nascimento, data_cadastro, plano, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conexao = ConexaoBanco.conectar()) {

            try (PreparedStatement consulta = conexao.prepareStatement(sqlNumero);
                 ResultSet resultado = consulta.executeQuery()) {

                if (resultado.next()) {
                    socio.setNumeroSocio(resultado.getInt(1));
                }
            }

            try (PreparedStatement comando = conexao.prepareStatement(sqlInsert)) {

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
    }

    /**
     * Grava no banco as alterações de um sócio já cadastrado (identificado pelo CPF).
     * Atualiza nome, e-mail, telefone, data de nascimento, plano e status.
     *
     * @return true se o sócio foi encontrado e atualizado
     */
    public boolean atualizar(SocioTorcedor socio) throws SQLException {

        String sql = """
                UPDATE socios
                SET nome = ?, email = ?, telefone = ?, data_nascimento = ?,
                    plano = ?, status = ?
                WHERE cpf = ?
                """;

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, socio.getNome());
            comando.setString(2, socio.getEmail());
            comando.setString(3, socio.getTelefone());
            comando.setObject(4, socio.getDataNascimento());
            comando.setString(5, socio.getPlano());
            comando.setString(6, socio.getStatus().name());
            comando.setString(7, socio.getCpf());

            return comando.executeUpdate() > 0;
        }
    }

    public ArrayList<SocioTorcedor> listar() throws SQLException {

        ArrayList<SocioTorcedor> socios = new ArrayList<>();

        String sql = "SELECT * FROM socios ORDER BY numero_socio";

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {

            while (resultado.next()) {
                socios.add(mapear(resultado));
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
                    return mapear(resultado);
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

    private SocioTorcedor mapear(ResultSet resultado) throws SQLException {

        SocioTorcedor socio = new SocioTorcedor();

        socio.setNumeroSocio(resultado.getInt("numero_socio"));
        socio.setNome(resultado.getString("nome"));
        socio.setCpf(resultado.getString("cpf"));
        socio.setEmail(resultado.getString("email"));
        socio.setTelefone(resultado.getString("telefone"));
        socio.setDataNascimento(
                resultado.getObject("data_nascimento", java.time.LocalDate.class));
        socio.setDataCadastro(
                resultado.getObject("data_cadastro", java.time.LocalDate.class));
        socio.setPlano(resultado.getString("plano"));
        socio.setStatus(StatusTorcedor.valueOf(resultado.getString("status")));

        return socio;
    }
}
