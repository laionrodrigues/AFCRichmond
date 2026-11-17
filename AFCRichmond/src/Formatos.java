import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Funções de formatação usadas pela interface gráfica.
 */
public final class Formatos {

    // dd/MM/aaaa, rejeitando datas inexistentes (ex.: 31/02/2000)
    public static final DateTimeFormatter DATA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private Formatos() {
    }

    public static String apenasDigitos(String texto) {
        return texto == null ? "" : texto.replaceAll("[^0-9]", "");
    }

    public static String cpf(String cpf) {
        if (cpf == null) {
            return "";
        }
        if (cpf.length() != 11) {
            return cpf;
        }
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "."
                + cpf.substring(6, 9) + "-" + cpf.substring(9);
    }

    public static String telefone(String telefone) {
        if (telefone == null) {
            return "";
        }
        if (telefone.length() != 11) {
            return telefone;
        }
        return "(" + telefone.substring(0, 2) + ") "
                + telefone.substring(2, 7) + "-" + telefone.substring(7);
    }

    public static String data(LocalDate data) {
        return data == null ? "" : data.format(DATA);
    }

    public static LocalDate lerData(String texto) throws DateTimeParseException {
        return LocalDate.parse(texto, DATA);
    }
}
