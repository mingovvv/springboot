package mingovvv.springboot.auto_config.selector;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

public class SelectorTest {

    @Test
    void staticConfig() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(StaticConfig.class);
        HelloBean bean = ac.getBean(HelloBean.class);
        Assertions.assertThat(bean).isNotNull();
    }

    @Test
    void selectConfig() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SelectorConfig.class);
        HelloBean bean = ac.getBean(HelloBean.class);
        Assertions.assertThat(bean).isNotNull();
    }

    // 정적으로 설정정보를 import
    @Configuration
    @Import(HelloConfig.class)
    public static class StaticConfig {

    }

    // 동적으로 설정정보를 import
    @Configuration
    @Import(HelloImportSelector.class)
    public static class SelectorConfig {

    }

}
