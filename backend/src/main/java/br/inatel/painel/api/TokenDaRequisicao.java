package br.inatel.painel.api;

public final class TokenDaRequisicao {

    private static final ThreadLocal<String> ATUAL = new ThreadLocal<>();

    private TokenDaRequisicao() {
    }

    public static String atual() {
        return ATUAL.get();
    }

    static void definir(String token) {
        ATUAL.set(token);
    }

    static void limpar() {
        ATUAL.remove();
    }
}
