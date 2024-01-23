import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class Initial {
    private static Initial instance;
    SystemSellTickets sistema;

    public static void main(String[] args) {
        instance = new Initial();
        instance.sistema = new SystemSellTickets();
        instance.start(new String[]{});
    }

    public void start(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Tela Inicial");
            //debug
            instance.sistema.cadastrarPassagem(new Passagem("Maceió", "São Paulo", new Date("03/05/2024"), 1000));

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel panel = new JPanel(new GridLayout(3, 1));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JButton areaVendedorBtn = new JButton("Área do Vendedor");
            areaVendedorBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Abre a tela da área do vendedor (SystemSellTicketsGUI)
                    SystemSellTicketsGUI systemSellTicketsGUI = new SystemSellTicketsGUI(instance.sistema);
                    systemSellTicketsGUI.main(instance);
                    // Fecha a tela inicial após escolher a área do vendedor
                    frame.dispose();
                }
            });

            JButton areaClienteBtn = new JButton("Área do Cliente");
            areaClienteBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Abre a tela da área do cliente
                    ClienteArea clienteArea = new ClienteArea(instance.sistema, instance);
                    clienteArea.mostrarTelaInicial();
                    // Fecha a tela inicial após escolher a área do cliente
                    frame.dispose();
                }
            });

            JButton sairBtn = getjButton();

            panel.add(areaVendedorBtn);
            panel.add(areaClienteBtn);
            panel.add(sairBtn);

            frame.getContentPane().add(panel);
            frame.setSize(300, 200);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
    private static JButton getjButton() {
        JButton sairBtn = new JButton("Sair");
        sairBtn.setFocusPainted(false);
        sairBtn.setBackground(Color.RED);
        sairBtn.setForeground(Color.WHITE);
        sairBtn.setPreferredSize(new Dimension(100, 30));
        sairBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Fecha o programa ao clicar no botão Sair
                System.exit(0);
            }
        });
        return sairBtn;
    }
}
