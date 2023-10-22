package mingovvv.springboot.external_config;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandLineV1 {

    public static void main(String[] args) {

        // program arguments
        // `url username password`
        for (String arg : args) {
            log.info("arg : {}", arg);
        }

    }

}
