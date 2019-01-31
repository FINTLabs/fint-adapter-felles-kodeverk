package no.fint;

import com.github.springfox.loader.EnableSpringfox;
import lombok.extern.slf4j.Slf4j;
import no.fint.sse.oauth.OAuthConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

@Slf4j
@EnableSpringfox
@EnableScheduling
@Import(OAuthConfig.class)
@SpringBootApplication
public class Application {

    @Value("${springfox.version}")
    private String version;

    @PostConstruct
    public void init() {
        log.info("Starting version: {}", version);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
