package mingovvv.springboot.external_config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;

import java.util.List;
import java.util.Set;

@Slf4j
public class CommandLineV2 {

    public static void main(String[] args) {

        // program arguments
        // `--url=mingo.com --username=mingo --password=ming dialect=MySQL`
        for (String arg : args) {
            log.info("arg : {}", arg);
        }

        ApplicationArguments appArgs = new DefaultApplicationArguments(args);
        log.info("SourceArgs = {}", List.of(appArgs.getSourceArgs()));
        log.info("NonOptionArgs = {}", appArgs.getNonOptionArgs());
        log.info("OptionNames = {}", appArgs.getOptionNames());

        Set<String> optionNames = appArgs.getOptionNames();
        for (String optionName : optionNames) {
            log.info("option args {}={}", optionName, appArgs.getOptionValues(optionName));
        }

        List<String> url = appArgs.getOptionValues("url");
        List<String> username = appArgs.getOptionValues("username");
        List<String> password = appArgs.getOptionValues("password");
        List<String> mode = appArgs.getOptionValues("dialect");
        log.info("url={}", url);
        log.info("username={}", username);
        log.info("password={}", password);
        log.info("dialect={}", mode);

        //  arg : --url=mingo.com
        //  arg : --username=mingo
        //  arg : --password=ming
        //  arg : dialect=MySQL
        //  SourceArgs = [--url=mingo.com, --username=mingo, --password=ming, dialect=MySQL]
        //  NonOptionArgs = [dialect=MySQL]
        //  OptionNames = [password, url, username]
        //  option args password=[ming]
        //  option args url=[mingo.com]
        //  option args username=[mingo]
        //  url=[mingo.com]
        //  username=[mingo]
        //  password=[ming]
        //  dialect=null

    }

}
