package br.inatel.painel.api;

import br.inatel.painel.controles.CtrlAutenticacao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FiltroDeSessao implements HandlerInterceptor {

    private final CtrlAutenticacao ctrlAutenticacao;

    public FiltroDeSessao(CtrlAutenticacao ctrlAutenticacao) {
        this.ctrlAutenticacao = ctrlAutenticacao;
    }

    @Override
    public boolean preHandle(HttpServletRequest requisicao, HttpServletResponse resposta, Object handler)
            throws IOException {
        if ("OPTIONS".equals(requisicao.getMethod())) return true;

        String token = tokenDe(requisicao);
        if (!ctrlAutenticacao.sessaoValida(token)) {
            resposta.setStatus(HttpStatus.UNAUTHORIZED.value());
            resposta.setContentType(MediaType.APPLICATION_JSON_VALUE);
            resposta.setCharacterEncoding(StandardCharsets.UTF_8.name());
            resposta.getWriter().write("{\"mensagem\":\"Sessão inválida. Faça login novamente.\"}");
            return false;
        }
        TokenDaRequisicao.definir(token);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest requisicao, HttpServletResponse resposta, Object handler,
                                Exception erro) {
        TokenDaRequisicao.limpar();
    }

    static String tokenDe(HttpServletRequest requisicao) {
        String cabecalho = requisicao.getHeader("Authorization");
        if (cabecalho == null || !cabecalho.startsWith("Bearer ")) return null;
        return cabecalho.substring("Bearer ".length());
    }
}
