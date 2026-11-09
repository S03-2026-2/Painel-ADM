package br.inatel.painel.servicos;

import br.inatel.painel.entidades.Proposta;
import br.inatel.painel.entidades.StatusTroca;
import br.inatel.painel.entidades.Troca;

import java.util.List;

public interface IServicoTrocas {

    List<Troca> buscarTrocas(StatusTroca status);

    List<Proposta> buscarPropostas();
}
