import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemSellTicketsGUI {

    private final SystemSellTickets sistema;

    public SystemSellTicketsGUI(SystemSellTickets sistema) {
        this.sistema = sistema;
    }

    public static void main(Initial initial) {
        SwingUtilities.invokeLater(() -> {
            SystemSellTicketsGUI gui = new SystemSellTicketsGUI(initial.sistema);
            gui.createAndShowGUI(initial);
        });
    }


    private void createAndShowGUI(Initial initial) {
        JFrame frame = new JFrame("Sistema de Venda de Passagens");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        JButton cadastrarClienteBtn = new JButton("Cadastrar Cliente");
        cadastrarClienteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarFormularioCliente();
            }
        });

        JButton cadastrarPassagemBtn = new JButton("Cadastrar Passagem");
        cadastrarPassagemBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarFormularioPassagem();
            }
        });

        JButton visualizarInfoBtn = new JButton("Visualizar Informações");
        visualizarInfoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarInformacoes();
            }
        });

        JButton sairBtn = new JButton("Voltar");
        sairBtn.setBackground(Color.RED);
        sairBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                initial.start(new String[]{});
            }
        });

        panel.add(cadastrarClienteBtn);
        panel.add(cadastrarPassagemBtn);
        panel.add(visualizarInfoBtn);
        panel.add(sairBtn);

        frame.getContentPane().add(panel);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void mostrarFormularioCliente() {
        JFrame frame = new JFrame("Cadastro de Cliente");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nomeLabel = new JLabel("Nome:");
        JTextField nomeField = new JTextField();
        JLabel cpfLabel = new JLabel("CPF:");
        JTextField cpfField = new JTextField();
        JLabel telefoneLabel = new JLabel("Telefone:");
        JTextField telefoneField = new JTextField();

        panel.add(nomeLabel);
        panel.add(nomeField);
        panel.add(cpfLabel);
        panel.add(cpfField);
        panel.add(telefoneLabel);
        panel.add(telefoneField);

        JButton cadastrarBtn = new JButton("Cadastrar");
        cadastrarBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nome = nomeField.getText();
                String cpf = cpfField.getText();
                String telefone = telefoneField.getText();

                String mensagemErro = sistema.validarDadosCliente(nome, cpf, telefone);
                if (mensagemErro != null) {
                    JOptionPane.showMessageDialog(null, mensagemErro);
                    return;
                }
                sistema.cadastrarCliente(new Client(nome, cpf, telefone));

                JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso.");

                frame.dispose();
            }
        });

        panel.add(cadastrarBtn);

        frame.getContentPane().add(panel);
        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void mostrarFormularioPassagem() {
        JFrame frame = new JFrame("Cadastro de Passagem");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel origemLabel = new JLabel("Origem:");
        JTextField origemField = new JTextField();
        JLabel destinoLabel = new JLabel("Destino:");
        JTextField destinoField = new JTextField();
        JLabel dataLabel = new JLabel("Data (dd/MM/yyyy):");
        JTextField dataField = new JTextField();
        JLabel valorLabel = new JLabel("Valor:");
        JTextField valorField = new JTextField();

        panel.add(origemLabel);
        panel.add(origemField);
        panel.add(destinoLabel);
        panel.add(destinoField);
        panel.add(dataLabel);
        panel.add(dataField);
        panel.add(valorLabel);
        panel.add(valorField);

        JButton cadastrarBtn = new JButton("Cadastrar");
        cadastrarBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String origem = origemField.getText();
                String destino = destinoField.getText();
                String dataStr = dataField.getText();
                String valorStr = valorField.getText();


                String mensagemErro = sistema.validarDadosPassagem(origem, destino, dataStr, valorStr);
                if (mensagemErro != null) {
                    JOptionPane.showMessageDialog(null, mensagemErro);
                    return;
                }

                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date data = dateFormat.parse(dataStr);
                    double valor = Double.parseDouble(valorStr);

                    sistema.cadastrarPassagem(new Passagem(origem, destino, data, valor));

                    JOptionPane.showMessageDialog(null, "Passagem cadastrada com sucesso.");

                    frame.dispose();
                } catch (ParseException | NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao cadastrar passagem. Verifique os dados informados.");
                }
            }
        });

        panel.add(cadastrarBtn);

        frame.getContentPane().add(panel);
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void mostrarInformacoes() {
        JFrame frame = new JFrame("Informações do Sistema");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        String informacoesClientes = obterInformacoesClientes();
        String informacoesPassagens = obterInformacoesPassagens();

        textArea.setText("Clientes:\n" + informacoesClientes + "\n\nPassagens:\n" + informacoesPassagens);

        frame.getContentPane().add(scrollPane);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private String obterInformacoesClientes() {


        StringBuilder info = new StringBuilder();
        for (Client cliente : sistema.getClientes()) {
            info.append("Nome: ").append(cliente.getNome()).append(", CPF: ").append(cliente.getCpf()).append(", Telefone: ").append(cliente.getTelefone()).append("\n");
        }
        return info.toString();
    }

    private String obterInformacoesPassagens() {


        StringBuilder info = new StringBuilder();
        for (Passagem passagem : sistema.getPassagens()) {
            info.append("Origem: ").append(passagem.getOrigem()).append(", Destino: ").append(passagem.getDestino())
                    .append(", Data: ").append(passagem.getData()).append(", Valor: ").append(passagem.getValor()).append("\n");
        }
        return info.toString();
    }

}
