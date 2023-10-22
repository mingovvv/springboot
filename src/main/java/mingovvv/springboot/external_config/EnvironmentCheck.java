package mingovvv.springboot.external_config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EnvironmentCheck {

    private final Environment env;

    public EnvironmentCheck(Environment env) {
        this.env = env;
    }

    @PostConstruct
    public void init() {
        String url = env.getProperty("url");
        String username1 = env.getProperty("username1");
        String password = env.getProperty("password");
        log.info("env url={}", url);
        log.info("env username1={}", username1);
        log.info("env password={}", password);
    }

}
