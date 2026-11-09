package br.inatel.painel.controles;

import br.inatel.painel.Cenario;
import br.inatel.painel.entidades.Perfil;
import br.inatel.painel.entidades.Sessao;
import br.inatel.painel.servicos.IAutenticacao;
import br.inatel.painel.servicos.ServicoIndisponivelException;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CtrlAutenticacaoTest {

    private final Cenario cenario = new Cenario();
    private final CtrlAutenticacao ctrl = new CtrlAutenticacao(cenario.fakeJogadores);

    @Test
    void administradorEntraERecebeUmaSessaoValida() {
        Optional<Sessao> sessao = ctrl.autenticar("admin", "admin");
        assertTrue(sessao.isPresent());
        assertEquals(Perfil.ADMIN, sessao.get().getPerfil());
        assertTrue(ctrl.sessaoValida(sessao.get().getToken()));
    }

    @Test
    void jogadorSemPerfilAdminTemAcessoNegado() {
        assertTrue(ctrl.autenticar("ash", "ash").isEmpty());
        assertFalse(ctrl.sessaoValida("token-fake-ash"));
    }

    @Test
    void senhaErradaTemAcessoNegado() {
        assertTrue(ctrl.autenticar("admin", "errada").isEmpty());
    }

    @Test
    void depoisDeSairOTokenDeixaDeValer() {
        String token = ctrl.autenticar("admin", "admin").orElseThrow().getToken();
        ctrl.sair(token);
        assertFalse(ctrl.sessaoValida(token));
    }

    @Test
    void servicoForaDoArNaoEhConfundidoComAcessoNegado() {
        IAutenticacao foraDoAr = (login, senha) -> {
            throw new ServicoIndisponivelException("Jogadores");
        };
        CtrlAutenticacao ctrlSemServico = new CtrlAutenticacao(foraDoAr);
        assertThrows(ServicoIndisponivelException.class, () -> ctrlSemServico.autenticar("admin", "admin"));
    }
}
