package mingovvv.springboot.auto_config.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class HelloImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        // 동적인 클래스 경로
        return new String[]{"mingovvv.springboot.auto_config.selector.HelloConfig"};
    }

}
