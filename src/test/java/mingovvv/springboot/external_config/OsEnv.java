package mingovvv.springboot.external_config;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class OsEnv {

    public static void main(String[] args) {

        Map<String, String> getenv = System.getenv();
        for (String key : getenv.keySet()) {
            log.info("key = {}, value = {}", key, getenv.get(key));
        }

    }

}