import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ClienteArea {

    private final SystemSellTickets sistema;
    private final Initial initial;

    public ClienteArea(SystemSellTickets sistema, Initial initial) {
        this.sistema = sistema;
        this.initial = initial;
    }

    public void mostrarTelaInicial() {
        JFrame frame = new JFrame("Área do Cliente");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton verificarPassagensBtn = new JButton("Verificar Passagens Disponíveis");
        verificarPassagensBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarPassagensDisponiveis();
            }
        });

        JButton visualizarInfoBtn = new JButton("Visualizar Informações");
        visualizarInfoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarInformacoes();
            }
        });

        JButton comprarPassagemBtn = new JButton("Comprar Passagem");
        comprarPassagemBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarFormularioCompra();
            }
        });

        JButton voltarBtn = new JButton("Voltar");
        voltarBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                frame.dispose();
                initial.main(new String[]{});
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(verificarPassagensBtn);
        panel.add(visualizarInfoBtn);
        panel.add(comprarPassagemBtn);
        panel.add(voltarBtn);

        frame.getContentPane().add(panel);
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void mostrarPassagensDisponiveis() {
        JFrame frame = new JFrame("Passagens Disponíveis");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        List<Passagem> passagensDisponiveis = sistema.getPassagensDisponiveis();

        if (passagensDisponiveis.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há passagens disponíveis no momento.");
            initial.main(new String[]{});
        } else {
            StringBuilder mensagem = new StringBuilder("Passagens Disponíveis:\n");
            for (Passagem passagem : passagensDisponiveis) {
                mensagem.append("Origem: ").append(passagem.getOrigem()).append(", Destino: ").append(passagem.getDestino())
                        .append(", Data: ").append(passagem.getData()).append(", Valor: ").append(passagem.getValor()).append("\n");
            }

            JTextArea textArea = new JTextArea(mensagem.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);

            JButton voltarBtn = new JButton("Voltar");
            voltarBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    frame.dispose();
                    mostrarTelaInicial();
                }
            });

            JPanel panel = new JPanel(new BorderLayout());
            panel.add(scrollPane, BorderLayout.CENTER);
            panel.add(voltarBtn, BorderLayout.SOUTH);

            frame.getContentPane().add(panel);
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }

    private void mostrarInformacoes() {

        String informacoesClientes = obterInformacoesClientes();
        String informacoesPassagens = obterInformacoesPassagens();
        JFrame frame = new JFrame("Informações do Sistema");

        JTextArea textArea = new JTextArea("Informações do Sistema:\n\nClientes:\n" + informacoesClientes +
                "\n\nPassagens:\n" + informacoesPassagens);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        JButton voltarBtn = new JButton("Voltar");
        voltarBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                mostrarTelaInicial();
            }
        });


        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(voltarBtn, BorderLayout.SOUTH);

        frame.getContentPane().add(panel);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void mostrarFormularioCompra() {
        JFrame frame = new JFrame("Compra de Passagens");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        List<Passagem> passagensDisponiveis = sistema.getPassagensDisponiveis();

        if (passagensDisponiveis.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há passagens disponíveis para compra.");
            frame.dispose();
            return;
        }

        JComboBox<Passagem> passagensComboBox = new JComboBox<>(passagensDisponiveis.toArray(new Passagem[0]));

        JLabel quantidadeLabel = new JLabel("Quantidade de Passagens:");
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 10, 1); // Mínimo 1, máximo 10, incremento 1
        JSpinner quantidadeSpinner = new JSpinner(spinnerModel);

        JButton comprarBtn = new JButton("Comprar");
        comprarBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Passagem passagemSelecionada = (Passagem) passagensComboBox.getSelectedItem();
                int quantidadeDesejada = (int) quantidadeSpinner.getValue();

                ImageIcon pixImage = new ImageIcon("assets/qr_code.png");
                JLabel imagemPix = new JLabel(pixImage);

                JFrame pixFrame = new JFrame("Pagamento via PIX");
                pixFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JPanel pixPanel = new JPanel(new FlowLayout());
                pixPanel.add(imagemPix);

                JButton confirmarBtn = new JButton("Confirmar");
                confirmarBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sistema.efetuarCompra(passagemSelecionada, "pix", quantidadeDesejada);
                        pixFrame.dispose();
                    }
                });

                pixPanel.add(confirmarBtn);

                pixFrame.getContentPane().add(pixPanel);

                pixFrame.pack();

                pixFrame.setLocationRelativeTo(null);
                pixFrame.setVisible(true);
            }
        });

        JButton voltarBtn = new JButton("Voltar");
        voltarBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                frame.dispose();
                mostrarTelaInicial();
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(passagensComboBox);
        panel.add(quantidadeLabel);
        panel.add(quantidadeSpinner);
        panel.add(comprarBtn);
        panel.add(voltarBtn);

        frame.getContentPane().add(panel);
        frame.setSize(300, 250);
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
