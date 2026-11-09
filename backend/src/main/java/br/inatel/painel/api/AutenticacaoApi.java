package br.inatel.painel.api;

import br.inatel.painel.api.Dtos.ErroSaida;
import br.inatel.painel.api.Dtos.LoginEntrada;
import br.inatel.painel.api.Dtos.LoginSaida;
import br.inatel.painel.controles.CtrlAutenticacao;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AutenticacaoApi {

    private final CtrlAutenticacao ctrlAutenticacao;

    public AutenticacaoApi(CtrlAutenticacao ctrlAutenticacao) {
        this.ctrlAutenticacao = ctrlAutenticacao;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginEntrada entrada) {
        return ctrlAutenticacao.autenticar(entrada.login(), entrada.senha())
                .<ResponseEntity<?>>map(sessao -> ResponseEntity.ok(new LoginSaida(sessao.getToken())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErroSaida(
                        "Acesso negado: login ou senha inválidos, ou usuário sem perfil de administrador.")));
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest requisicao) {
        ctrlAutenticacao.sair(FiltroDeSessao.tokenDe(requisicao));
    }
}
