import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Identidade visual do sistema (cores, fontes e escudo do AFC Richmond).
 * Para mudar o visual de toda a interface, altere aqui.
 */
public final class Estilo {

    // ---------- Cores (tiradas do escudo do clube) ----------
    public static final Color AZUL = new Color(0x243068);
    public static final Color AZUL_ESCURO = new Color(0x1A2350);
    public static final Color AZUL_CLARO = new Color(0x3A4A92);
    public static final Color VERMELHO = new Color(0xC8202F);
    public static final Color VERMELHO_ESCURO = new Color(0xA11925);
    public static final Color AMARELO = new Color(0xF5DC0F);
    public static final Color AMARELO_ESCURO = new Color(0xDCC400);

    public static final Color FUNDO = new Color(0xF3F4FA);
    public static final Color LINHA_ALTERNADA = new Color(0xECEEF8);
    public static final Color BORDA = new Color(0xC9CDE0);
    public static final Color TEXTO = new Color(0x1E2240);
    public static final Color VERDE = new Color(0x1B7F3B);

    // ---------- Fontes (ficam em src/main/resources/fontes) ----------
    private static final Font TITULO = carregarFonte("/fontes/Lora-Variable.ttf", "Serif");
    private static final Font NORMAL = carregarFonte("/fontes/Poppins-Regular.ttf", "SansSerif");
    private static final Font NEGRITO = carregarFonte("/fontes/Poppins-Bold.ttf", "SansSerif");

    private static BufferedImage escudo;

    private Estilo() {
    }

    private static Font carregarFonte(String caminho, String reserva) {
        try (InputStream in = Estilo.class.getResourceAsStream(caminho)) {
            if (in != null) {
                Font fonte = Font.createFont(Font.TRUETYPE_FONT, in);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(fonte);
                return fonte;
            }
        } catch (Exception e) {
            // se a fonte não carregar, usa uma fonte padrão do Java
        }
        return new Font(reserva, Font.PLAIN, 12);
    }

    /** Fonte serifada em negrito, para títulos. */
    public static Font fonteTitulo(float tamanho) {
        return TITULO.deriveFont(Font.BOLD, tamanho);
    }

    /** Fonte normal, para textos. */
    public static Font fonte(float tamanho) {
        return NORMAL.deriveFont(Font.PLAIN, tamanho);
    }

    /** Fonte em negrito, para rótulos e botões. */
    public static Font fonteNegrito(float tamanho) {
        return NEGRITO.deriveFont(Font.PLAIN, tamanho);
    }

    /** Aumenta o espaço entre as letras (bom para textos pequenos em maiúsculas). */
    public static Font comEspacamento(Font fonte, float espacamento) {
        Map<TextAttribute, Object> atributos = new HashMap<>();
        atributos.put(TextAttribute.TRACKING, espacamento);
        return fonte.deriveFont(atributos);
    }

    // ---------- Escudo ----------

    public static BufferedImage escudo() {
        if (escudo == null) {
            try (InputStream in = Estilo.class.getResourceAsStream("/escudo.png")) {
                if (in != null) {
                    escudo = ImageIO.read(in);
                }
            } catch (IOException e) {
                // sem escudo, a tela continua funcionando
            }
        }
        return escudo;
    }

    public static ImageIcon escudoIcone(int altura) {
        BufferedImage imagem = escudo();
        if (imagem == null) {
            return null;
        }
        int largura = Math.round(imagem.getWidth() * (float) altura / imagem.getHeight());
        return new ImageIcon(imagem.getScaledInstance(largura, altura, Image.SCALE_SMOOTH));
    }

    // ---------- Tema geral ----------

    /** Deve ser chamado uma vez, antes de criar qualquer janela. */
    public static void aplicarTema() {
        try {
            UIManager.put("nimbusBase", AZUL);
            UIManager.put("nimbusFocus", VERMELHO);
            UIManager.put("nimbusSelectionBackground", AZUL_CLARO);
            UIManager.put("control", FUNDO);
            UIManager.put("defaultFont", fonte(13f));

            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // se não der, segue com o visual padrão
        }
    }
}
