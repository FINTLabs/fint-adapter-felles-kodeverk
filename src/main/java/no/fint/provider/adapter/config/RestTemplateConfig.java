package no.fint.provider.adapter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    @ConditionalOnProperty(name = "fint.oauth.enabled", havingValue = "false", matchIfMissing = true)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
