import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class SystemSellTickets {

    private List<Client> listaDeClientes;
    private List<Passagem> listaDePassagens;

    public SystemSellTickets() {
        this.listaDeClientes = new ArrayList<>();
        this.listaDePassagens = new ArrayList<>();
    }

    public void cadastrarCliente(Client cliente) {
        listaDeClientes.add(cliente);
    }

    public String validarDadosCliente(String nome, String cpf, String telefone) {

        if (!nome.matches("^[a-zA-Z ]+$")) {
            return "O nome não pode conter números.";
        }

        if (!cpf.matches("^\\d{11}$")) {
            return "O CPF deve conter exatamente 11 números.";
        }

        if (!telefone.matches("^\\d{8,15}$")) {
            return "O telefone deve conter entre 8 e 15 números.";
        }

        return null;
    }


    public Client pesquisarCliente(String cpf) {
        for (Client cliente : listaDeClientes) {
            if (cliente.getCpf().equals(cpf)) {
                return cliente;
            }
        }
        return null;
    }

    public void cadastrarPassagem(Passagem passagem) {
        listaDePassagens.add(passagem);
    }

    public String validarDadosPassagem(String origem, String destino, String dataStr, String valorStr) {

        if (origem.isEmpty() || destino.isEmpty()) {
            return "Origem e destino não podem ser vazios.";
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateFormat.parse(dataStr);
        } catch (ParseException e) {
            return "Formato de data inválido. Use dd/MM/yyyy.";
        }

        try {
            Double.parseDouble(valorStr);
        } catch (NumberFormatException e) {
            return "Formato de valor inválido.";
        }

        return null;
    }

    public List<Passagem> getPassagensDisponiveis() {
        List<Passagem> passagensDisponiveis = new ArrayList<>();
        for (Passagem passagem : listaDePassagens) {
            if (passagem.getData().after(new Date())) {
                passagensDisponiveis.add(passagem);
            } else {
                System.out.println("Passagem com data inválida: " + passagem.getData());
                System.out.printf("Data atual: %s\n", new Date());
            }
        }
        return passagensDisponiveis;
    }

    public Passagem pesquisarPassagem(int id) {
        for (Passagem passagem : listaDePassagens) {
            if (passagem.getId() == id) {
                return passagem;
            }
        }
        return null; // Retorna null se a passagem não for encontrada
    }

    public void visualizarInformacoes() {
        System.out.println("Clientes cadastrados:");
        for (Client cliente : listaDeClientes) {
            System.out.println("Nome: " + cliente.getNome() + ", CPF: " + cliente.getCpf() + ", Telefone: " + cliente.getTelefone());
        }

        System.out.println("\nPassagens cadastradas:");
        for (Passagem passagem : listaDePassagens) {
            System.out.println("ID: " + passagem.getId() + ", Origem: " + passagem.getOrigem() +
                    ", Destino: " + passagem.getDestino() + ", Data: " + passagem.getData() + ", Valor: " + passagem.getValor());
        }
    }

    public List<Client> getClientes() {
        return listaDeClientes;
    }

    public List<Passagem> getPassagens() {
        return listaDePassagens;
    }

    public void efetuarCompra(Passagem passagem, String metodoPagamento, int quantidade) {

        if (!getPassagensDisponiveis().contains(passagem)) {
            JOptionPane.showMessageDialog(null, "A passagem selecionada não está mais disponível para compra.");
            return;
        }

        Iterator<Passagem> iterator = listaDePassagens.iterator();
        while (iterator.hasNext()) {
            Passagem passagemAtual = iterator.next();
            if (passagemAtual.equals(passagem)) {
                iterator.remove();
                quantidade--;

                if (quantidade == 0) {
                    break; // Sai do loop quando a quantidade desejada foi removida
                }
            }
        }

        String mensagem = quantidade > 0 ?
                "Compra efetuada com sucesso para " + (quantidade + 1) + " passagens." :
                "Compra efetuada com sucesso para 1 passagem.";
        JOptionPane.showMessageDialog(null, mensagem);
    }

}

class Client {
    private static int nextId = 1; // variável estática para gerar IDs únicos
    private int id;
    private String nome;
    private String cpf;
    private String telefone;

    public Client(String nome, String cpf, String telefone) {
        this.id = nextId++;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }
}

class Passagem {
    private static int nextId = 1; // variável estática para gerar IDs únicos
    private int id;
    private String origem;
    private String destino;
    private Date data;
    private double valor;

    public Passagem(String origem, String destino, Date data, double valor) {
        this.id = nextId++;
        this.origem = origem;
        this.destino = destino;
        this.data = data;
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Origem: " + origem + ", Destino: " + destino + ", Data: " + data + ", Valor: " + valor;
    }

    public int getId() {
        return id;
    }

    public String getOrigem() {
        return origem;
    }

    public String getDestino() {
        return destino;
    }

    public Date getData() {
        return data;
    }

    public double getValor() {
        return valor;
    }
}
