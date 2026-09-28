
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner entrada = new Scanner(System.in);
        Clube clube = new Clube();

        int opcao;

        System.out.println("================================");
        System.out.println(" BEM-VINDO AO " + clube.getNome());
        System.out.println("================================");

        try {

            do {
                System.out.println("\n===== MENU PRINCIPAL =====");
                System.out.println("1. Cadastrar sócio");
                System.out.println("2. Listar sócios");
                System.out.println("3. Atualizar dados");
                System.out.println("4. Cancelar sócio");
                System.out.println("5. Reativar sócio");
                System.out.println("6. Buscar sócio");
                System.out.println("7. Quantidade de sócios");
                System.out.println("0. Sair");

                opcao = lerInteiro(entrada, "Escolha uma opção: ");

                switch (opcao) {

                    case 1:
                        cadastrarUsuario(entrada, clube);
                        break;

                    case 2:
                        clube.listarTorcedores();
                        break;

                    case 3:
                        atualizarUsuario(entrada, clube);
                        break;

                    case 4:
                        alterarStatus(entrada, clube, false);
                        break;

                    case 5:
                        alterarStatus(entrada, clube, true);
                        break;

                    case 6:
                        buscarUsuario(entrada, clube);
                        break;

                    case 7:
                        System.out.println("Total de sócios: "
                                + clube.quantidadeTorcedores());
                        break;

                    case 0:
                        System.out.println("Saindo do sistema...");
                        break;

                    default:
                        System.out.println("Opção inválida!");
                }

            } while (opcao != 0);

        } finally {
            entrada.close();
            System.out.println("Sistema encerrado.");
        }
    }

    private static int lerInteiro(Scanner entrada, String mensagem) {

        while (true) {
            try {
                System.out.print(mensagem);
                return Integer.parseInt(entrada.nextLine());

            } catch (NumberFormatException e) {
                System.out.println("Digite apenas números!");
            }
        }
    }

    private static void cadastrarUsuario(Scanner entrada, Clube clube) {

        SocioTorcedor torcedor = new SocioTorcedor();

        System.out.println("\n===== CADASTRO DE SÓCIO =====");

        System.out.println("1 - Prata - $20 mensal");
        System.out.println("2 - Ouro - $40 mensal");
        System.out.println("3 - Diamante - $60 mensal");

        int plano = lerInteiro(entrada, "Escolha seu plano: ");

        while (plano < 1 || plano > 3) {
            System.out.println("Plano inválido!");
            plano = lerInteiro(entrada, "Escolha novamente: ");
        }

        if (plano == 1) {
            torcedor.setPlano("Prata");
        } else if (plano == 2) {
            torcedor.setPlano("Ouro");
        } else {
            torcedor.setPlano("Diamante");
        }

        System.out.print("Digite seu nome: ");
        torcedor.setNome(entrada.nextLine());

        boolean cpfValido = false;

        while (!cpfValido) {
            System.out.print("Digite seu CPF: ");
            cpfValido = torcedor.setCpf(entrada.nextLine());

            if (!cpfValido) {
                System.out.println("CPF inválido! Tente novamente.");
            }
        }

        if (clube.buscarPorCpf(torcedor.getCpf()) != null) {
            System.out.println("Este CPF já está cadastrado.");
            return;
        }

        System.out.print("Digite seu email: ");
        torcedor.setEmail(entrada.nextLine());

        System.out.print("Digite seu telefone (11 números): ");
        torcedor.setTelefone(entrada.nextLine());

        boolean dataValida = false;

        while (!dataValida) {
            try {
                System.out.print("Data de nascimento (AAAA-MM-DD): ");

                LocalDate data = LocalDate.parse(entrada.nextLine());

                torcedor.setDataNascimento(data);

                if (torcedor.getDataNascimento() != null) {
                    dataValida = true;
                }

            } catch (DateTimeParseException e) {
                System.out.println("Formato de data inválido!");
            }
        }

        int numeroSocio = clube.quantidadeTorcedores() + 1;
        torcedor.setNumeroSocio(numeroSocio);

        clube.adicionarSocio(torcedor);

        System.out.println("\nCadastro realizado com sucesso!");
        System.out.println(torcedor);
    }

    private static void buscarUsuario(Scanner entrada, Clube clube) {

        System.out.print("Digite o CPF do sócio: ");
        String cpf = entrada.nextLine().replaceAll("[^0-9]", "");

        SocioTorcedor torcedor = clube.buscarPorCpf(cpf);

        if (torcedor == null) {
            System.out.println("Sócio não encontrado.");
        } else {
            System.out.println(torcedor);
        }
    }

    private static void atualizarUsuario(Scanner entrada, Clube clube) {

        System.out.print("Digite o CPF do sócio: ");
        String cpf = entrada.nextLine().replaceAll("[^0-9]", "");

        SocioTorcedor torcedor = clube.buscarPorCpf(cpf);

        if (torcedor == null) {
            System.out.println("Sócio não encontrado.");
            return;
        }

        System.out.println("\n1. Nome");
        System.out.println("2. Email");
        System.out.println("3. Telefone");
        System.out.println("4. Data de nascimento");

        int opcao = lerInteiro(entrada, "O que deseja alterar? ");

        switch (opcao) {

            case 1:
                System.out.print("Novo nome: ");
                torcedor.setNome(entrada.nextLine());
                break;

            case 2:
                System.out.print("Novo email: ");
                torcedor.setEmail(entrada.nextLine());
                break;

            case 3:
                System.out.print("Novo telefone: ");
                torcedor.setTelefone(entrada.nextLine());
                break;

            case 4:
                try {
                    System.out.print("Nova data (AAAA-MM-DD): ");
                    LocalDate data = LocalDate.parse(entrada.nextLine());
                    torcedor.setDataNascimento(data);

                } catch (DateTimeParseException e) {
                    System.out.println("Data inválida!");
                }
                break;

            default:
                System.out.println("Opção inválida.");
        }

        System.out.println(torcedor);
    }

    private static void alterarStatus(
            Scanner entrada, Clube clube, boolean reativar) {

        System.out.print("Digite o CPF do sócio: ");
        String cpf = entrada.nextLine().replaceAll("[^0-9]", "");

        SocioTorcedor torcedor = clube.buscarPorCpf(cpf);

        if (torcedor == null) {
            System.out.println("Sócio não encontrado.");
            return;
        }

        if (reativar) {
            torcedor.reativar();
        } else {
            torcedor.cancelar();
        }
    }
}