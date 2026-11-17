import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Janela (modal) de cadastro e edição de sócio.
 * Se "original" for null, cadastra um novo sócio; caso contrário, edita o sócio informado.
 */
public class TelaCadastroSocio extends JDialog {

    private static final String[] PLANOS = {"Prata", "Ouro", "Diamante"};
    private static final String[] PLANOS_TEXTO = {
            "Prata - $20 mensal", "Ouro - $40 mensal", "Diamante - $60 mensal"};

    private final Clube clube;
    private final SocioTorcedor original;

    private boolean salvo = false;
    private SocioTorcedor socioSalvo;

    private final JComboBox<String> cbPlano = new JComboBox<>(PLANOS_TEXTO);
    private final JTextField txtNome = new JTextField(24);
    private final JFormattedTextField txtCpf = campoComMascara("###.###.###-##");
    private final JTextField txtEmail = new JTextField(24);
    private final JFormattedTextField txtTelefone = campoComMascara("(##) #####-####");
    private final JFormattedTextField txtNascimento = campoComMascara("##/##/####");

    public TelaCadastroSocio(JFrame dono, Clube clube, SocioTorcedor original) {
        super(dono, original == null
                ? "Novo sócio"
                : "Editar sócio nº " + original.getNumeroSocio(), true);

        this.clube = clube;
        this.original = original;

        montarTela();
        if (original != null) {
            preencherCampos();
        }
        if (Estilo.escudo() != null) {
            setIconImage(Estilo.escudo());
        }

        pack();
        setResizable(false);
        setLocationRelativeTo(dono);
    }

    public boolean foiSalvo() {
        return salvo;
    }

    public SocioTorcedor getSocioSalvo() {
        return socioSalvo;
    }

    // ---------------------------------------------------------------- layout

    private void montarTela() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(Estilo.FUNDO);
        formulario.setBorder(BorderFactory.createEmptyBorder(14, 18, 6, 18));

        adicionarLinha(formulario, 0, "Plano:", cbPlano);
        adicionarLinha(formulario, 1, "Nome:", txtNome);
        adicionarLinha(formulario, 2, "CPF:", txtCpf);
        adicionarLinha(formulario, 3, "E-mail:", txtEmail);
        adicionarLinha(formulario, 4, "Telefone:", txtTelefone);
        adicionarLinha(formulario, 5, "Nascimento:", txtNascimento);

        BotaoEstilizado btnSalvar = new BotaoEstilizado(
                original == null ? "Cadastrar" : "Salvar alterações",
                BotaoEstilizado.Tipo.VERMELHO);
        BotaoEstilizado btnCancelar = new BotaoEstilizado("Cancelar", BotaoEstilizado.Tipo.CINZA);
        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        botoes.setBackground(Estilo.FUNDO);
        botoes.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 10));
        botoes.add(btnCancelar);
        botoes.add(btnSalvar);

        getContentPane().setBackground(Estilo.FUNDO);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(criarCabecalho(), BorderLayout.NORTH);
        getContentPane().add(formulario, BorderLayout.CENTER);
        getContentPane().add(botoes, BorderLayout.SOUTH);

        // Enter = salvar, Esc = fechar
        getRootPane().setDefaultButton(btnSalvar);
        getRootPane().registerKeyboardAction(e -> dispose(),
                KeyStroke.getKeyStroke("ESCAPE"), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private JPanel criarCabecalho() {
        JPanel cabecalho = new JPanel(new BorderLayout(14, 0));
        cabecalho.setBackground(Estilo.AZUL);
        cabecalho.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, Estilo.VERMELHO),
                BorderFactory.createEmptyBorder(10, 16, 10, 16)));

        ImageIcon icone = Estilo.escudoIcone(48);
        cabecalho.add(new JLabel(icone), BorderLayout.WEST);

        JLabel titulo = new JLabel(original == null ? "Novo sócio" : "Editar sócio");
        titulo.setFont(Estilo.fonteTitulo(22f));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitulo = new JLabel(original == null
                ? "NOVO CADASTRO"
                : "SÓCIO Nº " + original.getNumeroSocio());
        subtitulo.setFont(Estilo.comEspacamento(Estilo.fonteNegrito(11f), 0.14f));
        subtitulo.setForeground(Estilo.AMARELO);
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(Box.createVerticalGlue());
        textos.add(titulo);
        textos.add(subtitulo);
        textos.add(Box.createVerticalGlue());
        cabecalho.add(textos, BorderLayout.CENTER);

        return cabecalho;
    }

    private void adicionarLinha(JPanel painel, int linha, String texto, JComponent campo) {
        JLabel rotulo = new JLabel(texto);
        rotulo.setFont(Estilo.fonteNegrito(12f));
        rotulo.setForeground(Estilo.AZUL);

        GridBagConstraints g = new GridBagConstraints();
        g.gridy = linha;
        g.insets = new Insets(6, 4, 6, 4);

        g.gridx = 0;
        g.anchor = GridBagConstraints.WEST;
        painel.add(rotulo, g);

        g.gridx = 1;
        g.weightx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        painel.add(campo, g);
    }

    private static JFormattedTextField campoComMascara(String mascara) {
        try {
            MaskFormatter formatador = new MaskFormatter(mascara);
            formatador.setPlaceholderCharacter('_');

            JFormattedTextField campo = new JFormattedTextField(formatador);
            // Mantém o que o usuário digitou mesmo se a máscara ainda estiver incompleta
            campo.setFocusLostBehavior(JFormattedTextField.PERSIST);

            // Ao entrar num campo vazio, o cursor vai para o começo da máscara
            campo.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        if (Formatos.apenasDigitos(campo.getText()).isEmpty()) {
                            campo.setCaretPosition(0);
                        }
                    });
                }
            });

            return campo;

        } catch (ParseException e) {
            throw new IllegalArgumentException("Máscara inválida: " + mascara, e);
        }
    }

    private void preencherCampos() {
        int indice = 0;
        for (int i = 0; i < PLANOS.length; i++) {
            if (PLANOS[i].equals(original.getPlano())) {
                indice = i;
            }
        }
        cbPlano.setSelectedIndex(indice);

        txtNome.setText(original.getNome() == null ? "" : original.getNome());
        txtEmail.setText(original.getEmail() == null ? "" : original.getEmail());

        definirValor(txtCpf, Formatos.cpf(original.getCpf()));
        definirValor(txtTelefone, Formatos.telefone(original.getTelefone()));
        definirValor(txtNascimento, Formatos.data(original.getDataNascimento()));

        // O CPF identifica o sócio no banco, então não pode ser alterado
        txtCpf.setEnabled(false);
    }

    private void definirValor(JFormattedTextField campo, String texto) {
        if (!texto.isEmpty()) {
            campo.setValue(texto);
        }
    }

    // ---------------------------------------------------------------- salvar

    private void salvar() {

        String nome = txtNome.getText().trim();
        String email = txtEmail.getText().trim();
        String cpf = original == null
                ? Formatos.apenasDigitos(txtCpf.getText())
                : original.getCpf();
        String telefone = Formatos.apenasDigitos(txtTelefone.getText());

        List<String> erros = new ArrayList<>();

        if (!SocioTorcedor.nomeValido(nome)) {
            erros.add("Nome: informe ao menos 3 caracteres.");
        }
        if (!SocioTorcedor.cpfValido(cpf)) {
            erros.add("CPF: número inválido.");
        }
        if (!SocioTorcedor.emailValido(email)) {
            erros.add("E-mail: formato inválido (ex.: nome@dominio.com).");
        }
        if (!SocioTorcedor.telefoneValido(telefone)) {
            erros.add("Telefone: informe o DDD e os 9 dígitos.");
        }

        LocalDate nascimento = null;
        try {
            nascimento = Formatos.lerData(txtNascimento.getText());
            if (!SocioTorcedor.dataNascimentoValida(nascimento)) {
                erros.add("Nascimento: a data não pode estar no futuro nem ter mais de 120 anos.");
            }
        } catch (DateTimeParseException e) {
            erros.add("Nascimento: informe uma data válida no formato DD/MM/AAAA.");
        }

        if (!erros.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Corrija os campos abaixo:\n\n• " + String.join("\n• ", erros),
                    "Dados inválidos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // CPF repetido (só no cadastro)
        if (original == null) {
            SocioTorcedor existente = clube.buscarPorCpf(cpf);

            if (clube.getUltimoErro() != null) {
                mostrarErroBanco(clube.getUltimoErro());
                return;
            }
            if (existente != null) {
                JOptionPane.showMessageDialog(this,
                        "Já existe um sócio com este CPF (nº " + existente.getNumeroSocio()
                                + " - " + existente.getNome() + ").",
                        "CPF já cadastrado", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // Monta um objeto novo, para não alterar o da lista caso o banco falhe
        SocioTorcedor socio = new SocioTorcedor();
        if (original != null) {
            socio.setNumeroSocio(original.getNumeroSocio());
            socio.setDataCadastro(original.getDataCadastro());
            socio.setStatus(original.getStatus());
        }
        socio.setCpf(cpf);
        socio.setNome(nome);
        socio.setEmail(email);
        socio.setTelefone(telefone);
        socio.setDataNascimento(nascimento);
        socio.setPlano(PLANOS[cbPlano.getSelectedIndex()]);

        boolean ok = original == null
                ? clube.adicionarSocio(socio)
                : clube.atualizarSocio(socio);

        if (!ok) {
            mostrarErroBanco(clube.getUltimoErro());
            return;
        }

        socioSalvo = socio;
        salvo = true;

        if (original == null) {
            JOptionPane.showMessageDialog(this,
                    "Sócio cadastrado com sucesso!\n\nNúmero de sócio: " + socio.getNumeroSocio(),
                    "Cadastro realizado", JOptionPane.INFORMATION_MESSAGE);
        }

        dispose();
    }

    private void mostrarErroBanco(String mensagem) {
        JOptionPane.showMessageDialog(this,
                "Não foi possível gravar no banco de dados.\n\n" + mensagem,
                "Erro de banco de dados", JOptionPane.ERROR_MESSAGE);
    }
}
