package br.inatel.painel.api;

import br.inatel.painel.api.Dtos.CartaDto;
import br.inatel.painel.api.Dtos.CartasDoJogadorDto;
import br.inatel.painel.api.Dtos.JogadorDto;
import br.inatel.painel.api.Dtos.JogadorResumidoDto;
import br.inatel.painel.api.Dtos.PropostaDto;
import br.inatel.painel.api.Dtos.ResumoDto;
import br.inatel.painel.api.Dtos.TrocaAbertaDto;
import br.inatel.painel.api.Dtos.TrocaFinalizadaDto;
import br.inatel.painel.controles.CtrlCartasJogadores;
import br.inatel.painel.controles.CtrlJogadores;
import br.inatel.painel.controles.CtrlResumo;
import br.inatel.painel.controles.CtrlTrocas;
import br.inatel.painel.entidades.ResumoPainel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PainelApi {

    private final CtrlResumo ctrlResumo;
    private final CtrlJogadores ctrlJogadores;
    private final CtrlCartasJogadores ctrlCartasJogadores;
    private final CtrlTrocas ctrlTrocas;

    public PainelApi(CtrlResumo ctrlResumo, CtrlJogadores ctrlJogadores,
                     CtrlCartasJogadores ctrlCartasJogadores, CtrlTrocas ctrlTrocas) {
        this.ctrlResumo = ctrlResumo;
        this.ctrlJogadores = ctrlJogadores;
        this.ctrlCartasJogadores = ctrlCartasJogadores;
        this.ctrlTrocas = ctrlTrocas;
    }

    @GetMapping("/resumo")
    public ResumoDto resumo() {
        ResumoPainel r = ctrlResumo.obterResumo();
        return new ResumoDto(r.getTotalJogadores(), r.getTotalCartas(), r.getTrocasAbertas(),
                r.getPropostasPendentes(), r.getTrocasFinalizadas());
    }

    @GetMapping("/jogadores")
    public List<JogadorDto> jogadores() {
        return ctrlJogadores.listarComQuantidadeDeCartas().stream()
                .map(j -> new JogadorDto(j.jogador().getId(), j.jogador().getNick(), j.jogador().getEmail(),
                        j.jogador().getDataCadastro(), j.quantidadeCartas()))
                .toList();
    }

    @GetMapping("/cartas")
    public List<CartasDoJogadorDto> cartas() {
        return ctrlCartasJogadores.listarCartasPorJogador().entrySet().stream()
                .map(e -> new CartasDoJogadorDto(
                        new JogadorResumidoDto(e.getKey().getId(), e.getKey().getNick()),
                        e.getValue().stream().map(CartaDto::de).toList()))
                .toList();
    }

    @GetMapping("/trocas/abertas")
    public List<TrocaAbertaDto> trocasAbertas() {
        return ctrlTrocas.listarTrocasAbertas().stream()
                .map(t -> new TrocaAbertaDto(t.troca().getId(), t.nickOfertante(), CartaDto.de(t.cartaOferecida()),
                        t.troca().getDataAbertura(), t.tempoEmAberto().toMillis(), t.quantidadePropostas()))
                .toList();
    }

    @GetMapping("/propostas")
    public List<PropostaDto> propostas() {
        return ctrlTrocas.listarPropostas().stream()
                .map(p -> new PropostaDto(p.proposta().getId(), p.proposta().getTrocaId(), p.nickProponente(),
                        CartaDto.de(p.cartaProposta()), p.proposta().getStatus().name(),
                        p.proposta().getDataProposta()))
                .toList();
    }

    @GetMapping("/trocas/historico")
    public List<TrocaFinalizadaDto> historico() {
        return ctrlTrocas.listarHistorico().stream()
                .map(t -> new TrocaFinalizadaDto(t.troca().getId(), t.troca().getDataFinalizacao(),
                        t.nickOfertante(), CartaDto.de(t.cartaOferecida()), t.nickReceptor(),
                        CartaDto.de(t.cartaRecebida())))
                .toList();
    }
}
