package br.inatel.painel.servicos;

public class ServicoIndisponivelException extends RuntimeException {

    public ServicoIndisponivelException(String servico) {
        super("O serviço de " + servico + " está indisponível no momento.");
    }
}
