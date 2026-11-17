import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Janela principal do sistema de sócio-torcedor do AFC Richmond.
 * Para abrir a interface gráfica, execute esta classe (ela tem o método main).
 * Cores e fontes ficam na classe Estilo.
 */
public class TelaPrincipal extends JFrame {

    private static final String[] COLUNAS = {
            "Nº", "Nome", "CPF", "E-mail", "Telefone",
            "Nascimento", "Cadastro", "Plano", "Status"};

    static final int COL_NUMERO = 0;
    static final int COL_NOME = 1;
    static final int COL_CPF = 2;
    static final int COL_EMAIL = 3;
    static final int COL_TELEFONE = 4;
    static final int COL_NASCIMENTO = 5;
    static final int COL_CADASTRO = 6;
    static final int COL_PLANO = 7;
    static final int COL_STATUS = 8;

    private final Clube clube = new Clube();

    // Mesma ordem das linhas do modelo da tabela
    private List<SocioTorcedor> socios = new ArrayList<>();

    private final DefaultTableModel modelo = new DefaultTableModel(COLUNAS, 0) {
        @Override
        public boolean isCellEditable(int linha, int coluna) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int coluna) {
            return switch (coluna) {
                case COL_NUMERO -> Integer.class;
                case COL_NASCIMENTO, COL_CADASTRO -> LocalDate.class;
                default -> String.class;
            };
        }
    };

    private final JTable tabela = new JTable(modelo);
    private final TableRowSorter<TableModel> ordenador = new TableRowSorter<>(modelo);

    private final JTextField txtBusca = new JTextField(24);
    private final JComboBox<String> cbSituacao =
            new JComboBox<>(new String[]{"Todos", "Ativos", "Cancelados"});

    private final BotaoEstilizado btnNovo =
            new BotaoEstilizado("Novo sócio", BotaoEstilizado.Tipo.AMARELO);
    private final BotaoEstilizado btnEditar =
            new BotaoEstilizado("Editar", BotaoEstilizado.Tipo.AZUL);
    private final BotaoEstilizado btnCancelar =
            new BotaoEstilizado("Cancelar sócio", BotaoEstilizado.Tipo.VERMELHO);
    private final BotaoEstilizado btnReativar =
            new BotaoEstilizado("Reativar sócio", BotaoEstilizado.Tipo.AZUL);
    private final BotaoEstilizado btnAtualizar =
            new BotaoEstilizado("Atualizar lista", BotaoEstilizado.Tipo.CINZA);

    private final JLabel lblTotais = new JLabel(" ");

    public TelaPrincipal() {
        super(clubeNome() + " - Sócio Torcedor");

        montarTela();
        estilizarTabela(tabela);
        tabela.setRowSorter(ordenador);
        ordenador.setSortKeys(List.of(new RowSorter.SortKey(COL_NUMERO, SortOrder.ASCENDING)));
        configurarEventos();

        if (Estilo.escudo() != null) {
            setIconImage(Estilo.escudo());
        }

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1180, 680);
        setMinimumSize(new Dimension(960, 520));
        setLocationRelativeTo(null);

        // Carrega os dados depois que a janela aparecer
        SwingUtilities.invokeLater(this::carregarSocios);
    }

    private static String clubeNome() {
        return new Clube().getNome();
    }

    // ---------------------------------------------------------------- layout

    private void montarTela() {
        getContentPane().setBackground(Estilo.FUNDO);
        getContentPane().setLayout(new BorderLayout());

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(criarCabecalho(clube), BorderLayout.NORTH);
        norte.add(criarBarraBusca(), BorderLayout.CENTER);

        JScrollPane rolagem = new JScrollPane(tabela);
        rolagem.setBorder(BorderFactory.createLineBorder(Estilo.BORDA));
        rolagem.getViewport().setBackground(Color.WHITE);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(Estilo.FUNDO);
        centro.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));
        centro.add(rolagem, BorderLayout.CENTER);

        getContentPane().add(norte, BorderLayout.NORTH);
        getContentPane().add(centro, BorderLayout.CENTER);
        getContentPane().add(criarRodape(), BorderLayout.SOUTH);
    }

    /** Faixa azul do topo, com o escudo, o nome do clube e o ano de fundação. */
    static JPanel criarCabecalho(Clube clube) {
        JPanel cabecalho = new JPanel(new BorderLayout(16, 0));
        cabecalho.setBackground(Estilo.AZUL);
        cabecalho.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 4, 0, Estilo.VERMELHO),
                BorderFactory.createEmptyBorder(12, 20, 12, 24)));

        ImageIcon icone = Estilo.escudoIcone(78);
        cabecalho.add(new JLabel(icone), BorderLayout.WEST);

        JLabel titulo = new JLabel(clube.getNome().toUpperCase());
        titulo.setFont(Estilo.fonteTitulo(30f));
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("PROGRAMA SÓCIO TORCEDOR");
        subtitulo.setFont(Estilo.comEspacamento(Estilo.fonteNegrito(11.5f), 0.14f));
        subtitulo.setForeground(Estilo.AMARELO);

        JLabel local = new JLabel(clube.getEstadio() + "  •  " + clube.getCidade());
        local.setFont(Estilo.fonte(12f));
        local.setForeground(new Color(0xB9C2EE));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        for (JLabel rotulo : new JLabel[]{titulo, subtitulo, local}) {
            rotulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        textos.add(Box.createVerticalGlue());
        textos.add(titulo);
        textos.add(subtitulo);
        textos.add(Box.createVerticalStrut(2));
        textos.add(local);
        textos.add(Box.createVerticalGlue());
        cabecalho.add(textos, BorderLayout.CENTER);

        JLabel desde = new JLabel("DESDE");
        desde.setFont(Estilo.comEspacamento(Estilo.fonteNegrito(11f), 0.2f));
        desde.setForeground(new Color(0xB9C2EE));
        JLabel ano = new JLabel("1897");
        ano.setFont(Estilo.fonteTitulo(34f));
        ano.setForeground(Estilo.AMARELO);

        JPanel fundacao = new JPanel();
        fundacao.setOpaque(false);
        fundacao.setLayout(new BoxLayout(fundacao, BoxLayout.Y_AXIS));
        desde.setAlignmentX(Component.RIGHT_ALIGNMENT);
        ano.setAlignmentX(Component.RIGHT_ALIGNMENT);
        fundacao.add(Box.createVerticalGlue());
        fundacao.add(desde);
        fundacao.add(ano);
        fundacao.add(Box.createVerticalGlue());
        cabecalho.add(fundacao, BorderLayout.EAST);

        return cabecalho;
    }

    private JPanel criarBarraBusca() {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 14));
        barra.setBackground(Estilo.FUNDO);
        barra.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

        txtBusca.setToolTipText("Digite nome, CPF, e-mail ou número do sócio");
        txtBusca.setFont(Estilo.fonte(13f));
        cbSituacao.setFont(Estilo.fonte(13f));

        barra.add(rotulo("Buscar por nome, CPF, e-mail ou nº:"));
        barra.add(txtBusca);
        barra.add(rotulo("Situação:"));
        barra.add(cbSituacao);
        return barra;
    }

    private JLabel rotulo(String texto) {
        JLabel rotulo = new JLabel(texto);
        rotulo.setFont(Estilo.fonteNegrito(12f));
        rotulo.setForeground(Estilo.AZUL);
        return rotulo;
    }

    private JPanel criarRodape() {
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 12));
        botoes.setOpaque(false);
        botoes.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        botoes.add(btnNovo);
        botoes.add(btnEditar);
        botoes.add(btnCancelar);
        botoes.add(btnReativar);
        botoes.add(btnAtualizar);

        lblTotais.setFont(Estilo.fonte(12f));
        lblTotais.setForeground(Estilo.TEXTO);
        lblTotais.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setBackground(Estilo.FUNDO);
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Estilo.BORDA));
        rodape.add(botoes, BorderLayout.WEST);
        rodape.add(lblTotais, BorderLayout.EAST);
        return rodape;
    }

    /** Aplica as cores, fontes e larguras da tabela. */
    static void estilizarTabela(JTable tabela) {
        tabela.setRowHeight(32);
        tabela.setIntercellSpacing(new Dimension(0, 0));
        tabela.setShowGrid(false);
        tabela.setBackground(Color.WHITE);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setSelectionBackground(Estilo.AZUL_CLARO);
        tabela.setSelectionForeground(Color.WHITE);
        tabela.setFillsViewportHeight(true);

        JTableHeader cabecalho = tabela.getTableHeader();
        cabecalho.setReorderingAllowed(false);
        cabecalho.setDefaultRenderer(new CabecalhoTabela());
        cabecalho.setPreferredSize(new Dimension(cabecalho.getPreferredSize().width, 40));

        int[] larguras = {50, 190, 125, 210, 130, 100, 100, 85, 105};
        for (int i = 0; i < larguras.length; i++) {
            var coluna = tabela.getColumnModel().getColumn(i);
            coluna.setPreferredWidth(larguras[i]);
            coluna.setCellRenderer(new CelulaTabela(
                    centralizada(i) ? SwingConstants.CENTER : SwingConstants.LEFT,
                    i == COL_STATUS));
        }
    }

    private static boolean centralizada(int coluna) {
        return coluna == COL_NUMERO || coluna == COL_NASCIMENTO || coluna == COL_CADASTRO
                || coluna == COL_PLANO || coluna == COL_STATUS;
    }

    /** Desenha as células: linhas zebradas, datas formatadas e status colorido. */
    static class CelulaTabela extends DefaultTableCellRenderer {
        private final boolean status;
        private final Font fonteNormal = Estilo.fonte(12f);
        private final Font fonteStatus = Estilo.fonteNegrito(11.5f);

        CelulaTabela(int alinhamento, boolean status) {
            this.status = status;
            setHorizontalAlignment(alinhamento);
        }

        @Override
        protected void setValue(Object valor) {
            if (valor instanceof LocalDate data) {
                setText(Formatos.data(data));
            } else {
                setText(valor == null ? "" : valor.toString());
            }
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object valor,
                boolean selecionada, boolean foco, int linha, int coluna) {
            super.getTableCellRendererComponent(t, valor, selecionada, false, linha, coluna);
            setOpaque(true);
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            setFont(status ? fonteStatus : fonteNormal);

            if (selecionada) {
                setBackground(Estilo.AZUL_CLARO);
                setForeground(Color.WHITE);
            } else {
                setBackground(linha % 2 == 0 ? Color.WHITE : Estilo.LINHA_ALTERNADA);
                if (status) {
                    setForeground("ATIVO".equals(valor) ? Estilo.VERDE : Estilo.VERMELHO);
                } else {
                    setForeground(Estilo.TEXTO);
                }
            }
            return this;
        }
    }

    /** Cabeçalho azul da tabela, com a seta amarela de ordenação. */
    static class CabecalhoTabela extends DefaultTableCellRenderer {
        private final Font fonte = Estilo.fonteNegrito(11.5f);

        @Override
        public Component getTableCellRendererComponent(JTable t, Object valor,
                boolean selecionada, boolean foco, int linha, int coluna) {
            super.getTableCellRendererComponent(t, valor, false, false, linha, coluna);
            setOpaque(true);
            setBackground(Estilo.AZUL);
            setForeground(Color.WHITE);
            setFont(fonte);
            setHorizontalAlignment(centralizada(coluna) ? SwingConstants.CENTER : SwingConstants.LEFT);
            setHorizontalTextPosition(SwingConstants.LEFT);
            setIconTextGap(6);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 3, 0, Estilo.VERMELHO),
                    BorderFactory.createEmptyBorder(0, 10, 0, 10)));

            Icon seta = null;
            RowSorter<?> ordenacao = t.getRowSorter();
            if (ordenacao != null && !ordenacao.getSortKeys().isEmpty()) {
                RowSorter.SortKey chave = ordenacao.getSortKeys().get(0);
                if (chave.getColumn() == t.convertColumnIndexToModel(coluna)) {
                    if (chave.getSortOrder() == SortOrder.ASCENDING) {
                        seta = new SetaOrdenacao(true);
                    } else if (chave.getSortOrder() == SortOrder.DESCENDING) {
                        seta = new SetaOrdenacao(false);
                    }
                }
            }
            setIcon(seta);
            return this;
        }
    }

    private static class SetaOrdenacao implements Icon {
        private final boolean crescente;

        SetaOrdenacao(boolean crescente) {
            this.crescente = crescente;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Estilo.AMARELO);
            int[] xs = {x, x + 9, x + 4};
            int[] ys = crescente ? new int[]{y + 6, y + 6, y} : new int[]{y, y, y + 6};
            g2.fillPolygon(xs, ys, 3);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return 9;
        }

        @Override
        public int getIconHeight() {
            return 6;
        }
    }

    private void configurarEventos() {
        btnNovo.addActionListener(e -> novoSocio());
        btnEditar.addActionListener(e -> editarSocio());
        btnCancelar.addActionListener(e -> alterarStatus(false));
        btnReativar.addActionListener(e -> alterarStatus(true));
        btnAtualizar.addActionListener(e -> carregarSocios());

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                atualizarBotoes();
            }
        });

        // Duplo clique na linha = editar
        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tabela.getSelectedRow() >= 0) {
                    editarSocio();
                }
            }
        });

        txtBusca.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                aplicarFiltro();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                aplicarFiltro();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                aplicarFiltro();
            }
        });
        cbSituacao.addActionListener(e -> aplicarFiltro());

        atualizarBotoes();
    }

    // ---------------------------------------------------------------- dados

    private void carregarSocios() {
        socios = clube.getTorcedores();
        String erro = clube.getUltimoErro();

        preencherTabela();

        if (erro != null) {
            JOptionPane.showMessageDialog(this,
                    "Não foi possível carregar os sócios do banco de dados.\n\n" + erro
                            + "\n\nVerifique se o PostgreSQL está em execução (localhost:5433)"
                            + " e se o banco 'socio_torcedor' e a tabela 'socios' existem"
                            + " (veja o arquivo schema.sql).",
                    "Erro de banco de dados", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherTabela() {
        modelo.setRowCount(0);

        for (SocioTorcedor s : socios) {
            modelo.addRow(new Object[]{
                    s.getNumeroSocio(),
                    s.getNome(),
                    Formatos.cpf(s.getCpf()),
                    s.getEmail(),
                    Formatos.telefone(s.getTelefone()),
                    s.getDataNascimento(),
                    s.getDataCadastro(),
                    s.getPlano(),
                    s.getStatus().name()
            });
        }

        atualizarRodape();
        atualizarBotoes();
    }

    private SocioTorcedor socioSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return null;
        }
        int indice = tabela.convertRowIndexToModel(linha);
        return indice < socios.size() ? socios.get(indice) : null;
    }

    private void selecionarSocio(int numeroSocio) {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            if ((int) modelo.getValueAt(i, COL_NUMERO) == numeroSocio) {
                int linha = tabela.convertRowIndexToView(i);
                if (linha >= 0) {
                    tabela.setRowSelectionInterval(linha, linha);
                    tabela.scrollRectToVisible(tabela.getCellRect(linha, 0, true));
                }
                return;
            }
        }
    }

    // ---------------------------------------------------------------- ações

    private void novoSocio() {
        TelaCadastroSocio tela = new TelaCadastroSocio(this, clube, null);
        tela.setVisible(true);

        if (tela.foiSalvo()) {
            carregarSocios();
            selecionarSocio(tela.getSocioSalvo().getNumeroSocio());
        }
    }

    private void editarSocio() {
        SocioTorcedor socio = socioSelecionado();
        if (socio == null) {
            return;
        }

        TelaCadastroSocio tela = new TelaCadastroSocio(this, clube, socio);
        tela.setVisible(true);

        if (tela.foiSalvo()) {
            carregarSocios();
            selecionarSocio(socio.getNumeroSocio());
        }
    }

    private void alterarStatus(boolean reativar) {
        SocioTorcedor socio = socioSelecionado();
        if (socio == null) {
            return;
        }

        String acao = reativar ? "reativar" : "cancelar";
        int resposta = JOptionPane.showConfirmDialog(this,
                "Deseja " + acao + " o sócio nº " + socio.getNumeroSocio()
                        + " - " + socio.getNome() + "?",
                "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }

        int numero = socio.getNumeroSocio();

        // As próprias regras da classe SocioTorcedor impedem cancelar quem já
        // está cancelado (e reativar quem já está ativo)
        if (reativar) {
            socio.reativar();
        } else {
            socio.cancelar();
        }

        boolean ok = clube.atualizarSocio(socio);
        String erro = clube.getUltimoErro();

        // Recarrega do banco: mostra o estado real, inclusive se a gravação falhou
        carregarSocios();
        selecionarSocio(numero);

        if (!ok) {
            JOptionPane.showMessageDialog(this,
                    "Não foi possível gravar a alteração no banco de dados.\n\n" + erro,
                    "Erro de banco de dados", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------------------------------------------------------------- filtro / estado da tela

    private void aplicarFiltro() {
        List<RowFilter<TableModel, Integer>> filtros = new ArrayList<>();

        String texto = txtBusca.getText().trim();
        if (!texto.isEmpty()) {
            String termo = semAcento(texto);
            String digitos = Formatos.apenasDigitos(texto);

            filtros.add(new RowFilter<TableModel, Integer>() {
                @Override
                public boolean include(Entry<? extends TableModel, ? extends Integer> linha) {
                    if (semAcento(linha.getStringValue(COL_NOME)).contains(termo)
                            || semAcento(linha.getStringValue(COL_EMAIL)).contains(termo)) {
                        return true;
                    }
                    if (digitos.isEmpty()) {
                        return false;
                    }
                    return Formatos.apenasDigitos(linha.getStringValue(COL_CPF)).contains(digitos)
                            || linha.getStringValue(COL_NUMERO).equals(digitos);
                }
            });
        }

        switch (cbSituacao.getSelectedIndex()) {
            case 1 -> filtros.add(RowFilter.regexFilter("^ATIVO$", COL_STATUS));
            case 2 -> filtros.add(RowFilter.regexFilter("^CANCELADO$", COL_STATUS));
            default -> { }
        }

        ordenador.setRowFilter(filtros.isEmpty() ? null : RowFilter.andFilter(filtros));
        atualizarRodape();
        atualizarBotoes();
    }

    private static String semAcento(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();
    }

    private void atualizarBotoes() {
        SocioTorcedor socio = socioSelecionado();

        btnEditar.setEnabled(socio != null);
        btnCancelar.setEnabled(socio != null && socio.getStatus() == StatusTorcedor.ATIVO);
        btnReativar.setEnabled(socio != null && socio.getStatus() == StatusTorcedor.CANCELADO);
    }

    private void atualizarRodape() {
        long ativos = socios.stream()
                .filter(s -> s.getStatus() == StatusTorcedor.ATIVO)
                .count();

        lblTotais.setText("<html>Exibindo <b>" + tabela.getRowCount() + "</b> de <b>"
                + socios.size() + "</b> &nbsp;|&nbsp; Ativos: <b><font color='#1B7F3B'>"
                + ativos + "</font></b> &nbsp;|&nbsp; Cancelados: <b><font color='#C8202F'>"
                + (socios.size() - ativos) + "</font></b></html>");
    }

    // ---------------------------------------------------------------- main

    public static void main(String[] args) {
        Estilo.aplicarTema();
        SwingUtilities.invokeLater(() -> new TelaPrincipal().setVisible(true));
    }
}
