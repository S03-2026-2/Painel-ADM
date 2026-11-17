package br.inatel.painel.servicos.fabricas;

import br.inatel.painel.servicos.IAutenticacao;
import br.inatel.painel.servicos.IServicoCartas;
import br.inatel.painel.servicos.IServicoJogadores;
import br.inatel.painel.servicos.IServicoTrocas;

public interface FabricaServicos {

    IAutenticacao criarAutenticacao();

    IServicoJogadores criarServicoJogadores();

    IServicoCartas criarServicoCartas();

    IServicoTrocas criarServicoTrocas();
}
