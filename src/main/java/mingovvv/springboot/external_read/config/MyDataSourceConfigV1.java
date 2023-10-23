package mingovvv.springboot.external_read.config;

import lombok.extern.slf4j.Slf4j;
import mingovvv.springboot.external_read.datasource.MyDataSource;
import mingovvv.springboot.external_read.datasource.MyDataSourcePropertiesV1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
//@Configuration
//@EnableConfigurationProperties(MyDataSourcePropertiesV1.class) // @ConfigurationPropertiesScan 대체 가능
public class MyDataSourceConfigV1 {

    private final MyDataSourcePropertiesV1 properties;

    public MyDataSourceConfigV1(MyDataSourcePropertiesV1 properties) {
        this.properties = properties;
    }

    @Bean
    public MyDataSource myDataSource1() {
        return new MyDataSource(
                properties.getUrl(),
                properties.getUsername(),
                properties.getPassword(),
                properties.getEtc().getMaxConnection(),
                properties.getEtc().getTimeout(),
                properties.getEtc().getOptions());
    }

}