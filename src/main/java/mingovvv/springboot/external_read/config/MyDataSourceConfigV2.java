
package mingovvv.springboot.external_read.config;

import lombok.extern.slf4j.Slf4j;
import mingovvv.springboot.external_read.datasource.MyDataSource;
import mingovvv.springboot.external_read.datasource.MyDataSourcePropertiesV1;
import mingovvv.springboot.external_read.datasource.MyDataSourcePropertiesV2;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
//@Configuration
//@EnableConfigurationProperties(MyDataSourcePropertiesV2.class) // @ConfigurationPropertiesScan 대체 가능
public class MyDataSourceConfigV2 {

    private final MyDataSourcePropertiesV2 properties;

    public MyDataSourceConfigV2(MyDataSourcePropertiesV2 properties) {
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