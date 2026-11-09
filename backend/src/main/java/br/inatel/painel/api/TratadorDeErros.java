package br.inatel.painel.api;

import br.inatel.painel.api.Dtos.ErroSaida;
import br.inatel.painel.servicos.ServicoIndisponivelException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(ServicoIndisponivelException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErroSaida servicoIndisponivel(ServicoIndisponivelException erro) {
        return new ErroSaida(erro.getMessage());
    }
}
