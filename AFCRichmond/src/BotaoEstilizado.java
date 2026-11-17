import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Botão arredondado com as cores do clube. Desenhado à mão, então a cor
 * aparece igual em qualquer tema do Java.
 */
public class BotaoEstilizado extends JButton {

    public enum Tipo { AMARELO, AZUL, VERMELHO, CINZA }

    private static final Color FUNDO_DESATIVADO = new Color(0xDADDEA);
    private static final Color TEXTO_DESATIVADO = new Color(0x9AA0BC);

    private final Color fundo;
    private final Color fundoSobre;
    private final Color borda;
    private final Color corTexto;
    private boolean mouseSobre = false;

    public BotaoEstilizado(String texto, Tipo tipo) {
        super(texto);

        switch (tipo) {
            case AMARELO -> {
                fundo = Estilo.AMARELO;
                fundoSobre = Estilo.AMARELO_ESCURO;
                borda = Estilo.AMARELO_ESCURO;
                corTexto = Estilo.AZUL_ESCURO;
            }
            case VERMELHO -> {
                fundo = Estilo.VERMELHO;
                fundoSobre = Estilo.VERMELHO_ESCURO;
                borda = Estilo.VERMELHO_ESCURO;
                corTexto = Color.WHITE;
            }
            case AZUL -> {
                fundo = Estilo.AZUL;
                fundoSobre = Estilo.AZUL_CLARO;
                borda = Estilo.AZUL_ESCURO;
                corTexto = Color.WHITE;
            }
            default -> {
                fundo = new Color(0xE3E5F0);
                fundoSobre = new Color(0xD3D6E8);
                borda = Estilo.BORDA;
                corTexto = Estilo.AZUL;
            }
        }

        setFont(Estilo.fonteNegrito(12f));
        setForeground(corTexto);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(9, 16, 9, 16));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                mouseSobre = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mouseSobre = false;
                repaint();
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics fm = getFontMetrics(getFont());
        return new Dimension(fm.stringWidth(getText()) + 34, fm.getHeight() + 20);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int largura = getWidth();
        int altura = getHeight();

        if (isEnabled()) {
            g2.setColor(mouseSobre ? fundoSobre : fundo);
            g2.fillRoundRect(0, 0, largura, altura, 12, 12);
            g2.setColor(borda);
            g2.drawRoundRect(0, 0, largura - 1, altura - 1, 12, 12);
            g2.setColor(corTexto);
        } else {
            g2.setColor(FUNDO_DESATIVADO);
            g2.fillRoundRect(0, 0, largura, altura, 12, 12);
            g2.setColor(TEXTO_DESATIVADO);
        }

        g2.setFont(getFont());
        FontMetrics fm = g2.getFontMetrics();
        int x = (largura - fm.stringWidth(getText())) / 2;
        int y = (altura - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(getText(), x, y);

        g2.dispose();
    }
}
