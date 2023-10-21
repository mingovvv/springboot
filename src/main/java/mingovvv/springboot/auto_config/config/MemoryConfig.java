package mingovvv.springboot.auto_config.config;

import mingovvv.memory.MemoryCondition;
import mingovvv.memory.MemoryController;
import mingovvv.memory.MemoryFinder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

//@Configuration
//@Conditional(MemoryCondition.class) // MemoryCondition 클래스의 빈 생성조건을 참조
@ConditionalOnProperty(name = "memory", havingValue = "on")
public class MemoryConfig {

    @Bean
    public MemoryController memoryController() {
        return new MemoryController(memoryFinder());
    }

    @Bean
    public MemoryFinder memoryFinder() {
        return new MemoryFinder();
    }

}
