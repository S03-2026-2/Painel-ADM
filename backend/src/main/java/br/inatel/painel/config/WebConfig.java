package br.inatel.painel.config;

import br.inatel.painel.api.FiltroDeSessao;
import br.inatel.painel.controles.CtrlAutenticacao;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final CtrlAutenticacao ctrlAutenticacao;

    public WebConfig(CtrlAutenticacao ctrlAutenticacao) {
        this.ctrlAutenticacao = ctrlAutenticacao;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registro) {
        registro.addInterceptor(new FiltroDeSessao(ctrlAutenticacao))
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/login");
    }
}
