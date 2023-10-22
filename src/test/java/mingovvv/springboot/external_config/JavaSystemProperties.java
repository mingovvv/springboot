package mingovvv.springboot.external_config;

import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
public class JavaSystemProperties {

    public static void main(String[] args) {

        Properties properties = System.getProperties();
        for (Object key : properties.keySet()) {
            log.info("key = {}, value = {}", key, properties.get(key));
        }

        String url = System.getProperty("url");
        String username = System.getProperty("username");
        String password = System.getProperty("password");

        // VM option : -Durl=mingo.com -Dusername=mingo -Dpassword=mingo
        // java 실행 시 : java -Durl=mingo.com -Dusername=mingo -Dpassword=mingo -jar app.jar
        log.info("url = {}, username = {}, password = {}", url, username, password);

        // 런타임 환경에서 추가 가능
        System.setProperty("dialect", "MySQL");

    }

}
