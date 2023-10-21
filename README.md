# springboot

### Auto-Config

- 스프링부트 핵심
    - WAS 톰캣 내장
    - 라이브러리 관리 - 손쉬운 빌드 구성을 위하여 스타터 종속성 제공, 스프링과 외부 라이브러리의 버전을 자동으로 관리
    - 자동 구성 - 프로젝트 시작에 필요한 스프링과 외부 라이브러리의 빈을 자동 등록
    - 외부 설정 - 서버에 따라 달라져야 하는 외부 설정 공통화 (dev/qa/release)
    - 프로덕션 준비 - 모니터링을 위한 메트릭, 상태 확인 기능
- 스프링부트 자동구성(Auto configuration)
    - ![Desktop View](/images/1.png) 
    - 자주 사용되는 빈들을 자동으로 등록해주는 기능
    - 스프링 제공 : https://docs.spring.io/spring-boot/docs/current/reference/html/auto-configuration-classes.html
    ```java
    @AutoConfiguration(after = DataSourceAutoConfiguration.class) // 자동설정 애노테이션 (자동설절의 순서를 조정할 수 있음)
    @ConditionalOnClass({ DataSource.class, JdbcTemplate.class }) // 필요 클래스를 선택, 없으면 자동설정 기능 동작하지 않음
    @ConditionalOnSingleCandidate(DataSource.class)
    @EnableConfigurationProperties(JdbcProperties.class)
    @Import({ DatabaseInitializationDependencyConfigurer.class, JdbcTemplateConfiguration.class, NamedParameterJdbcTemplateConfiguration.class })
    public class JdbcTemplateAutoConfiguration {
        ...
    }
  
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnMissingBean(JdbcOperations.class) // JdbcOperations 빈이 없을때만 동작한다. 사용자가 커마할 수 있도록 열린 설정
    class JdbcTemplateConfiguration {
  
        @Bean
        @Primary
        JdbcTemplate jdbcTemplate(DataSource dataSource, JdbcProperties properties) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            JdbcProperties.Template template = properties.getTemplate();
            jdbcTemplate.setFetchSize(template.getFetchSize());
            jdbcTemplate.setMaxRows(template.getMaxRows());
            if (template.getQueryTimeout() != null) {
                jdbcTemplate.setQueryTimeout((int)
                template.getQueryTimeout().getSeconds());
            }
            return jdbcTemplate;
        }
  
    }
    ```
- 스프링부트 라이브러리 버전관리
    - `id 'io.spring.dependency-management' version '1.1.0'` 플러그인을 통한 자동 버전관리(boot 버전 기준)
    - 라이브러리 버전 관리 : 버전을 명시하지 않아도 라이브러리 사이의 호환성이 맞는 버전을 자동세팅 지원
    - 스프링 부트 스타터 : 연관된 의존관계 라이브러리를 한번에 가져오는 방식을 지원 

#### 동적으로 빈 생성여부 결정 직접 만들어보기
```java
// Condition 인터페이스 구현
@Slf4j
public class MemoryCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // -Dmemory=on
        String memory = context.getEnvironment().getProperty("memory");
        log.info("memory={}", memory);
        return "on".equals(memory);
    }

}

```
 - Condition 인터페이스를 구현
 - 특정 조건일 때만 해당 기능이 활성화
 
```text
[조건]
#VM Options
#java -Dmemory=on -jar project.jar
```

```java
@Configuration
@Conditional(MemoryCondition.class) // MemoryCondition 클래스의 빈 생성조건을 참조
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
```
 - 빈 등록 Config 클래스에서 `@Conditional` 애노테이션을 통해 동적으로 빈 등록 여부를 결정할 수 있음
 - 애플리케이션 실행 시, VM option 환경정보에 `-Dmemory=on`이 포함되어 있는 경우에만 해당 클래스가 빈으로 등록됨

#### @ConditionalOnXXX 파악하기
 - 스프링에서 제공하는 `@ConditionalOnProperty`를 사용하면 위처럼 Codition 인터페이스를 구현할 필요도 없다.
 - `@ConditionalOnProperty(name = "memory", havingValue = "on")`
 - 대표적인 @ConditionalOnXXX 예시
   - @ConditionalOnClass : 클래스가 있는 경우 동작
   - @ConditionalOnMissingClass : 클래스가 없는 경우 동작
   - @ConditionalOnBean : 빈이 등록되어 있는 경우 동작
   - @ConditionalOnMissingBean : 빈이 등록되어 있지 않는 경우 동작
   - @ConditionalOnProperty : 환경 정보가 있는 경우 동작
   - @ConditionalOnResource : 리소스가 있는 경우 동작
   - @ConditionalOnWebApplication , @ConditionalOnNotWebApplication : 웹 애플리케이션인 경우 동작
   - @ConditionalOnExpression : SpEL 표현식에 만족하는 경우 동작
 - 거의 스프링부트에서 자동구성을 위해 사용되는 애노테이션

#### 라이브러리 직접 만들기를 통한 `@AutoConfiguration` 이해하기 - 1
 - 외부 라이브러리 `@AutoConfiguration` 미적용
 - classpath에 `libs` 폴더 생성 후 사용할 직접 생성한 외부 라이브러리 넣기
 - build.gradle에 라이브러리 의존성 주입 연결 // 외부 라이브러리 의존성 주입 (libs 폴더명 주의)
   - `implementation files('libs/memory-v1.jar')`

```java
// 외부 라이브러리 사용을 위해 빈으로 등록
@Configuration
public class ExMemoryConfig {

    @Bean
    public MemoryFinder memoryFinder() {
        return new MemoryFinder();
    }

    @Bean
    public MemoryController memoryController() {
        return new MemoryController(memoryFinder());
    }

}
```
 - 외부 라이브러리 사용을 위해 빈으로 등록
 - 여기서 라이브러리를 사용하는 클라이언트 입장에서 라이브러리의 어떤 라이브러리의 어떤 객체를 빈으로 등록해주어야 하는지 알 수 없으므로 공식 문서를 참조해야함

#### 라이브러리 직접 만들기를 통한 `@AutoConfiguration` 이해하기 - 2
 - 외부 라이브러리 `@AutoConfiguration` 적용
 - 단순하게 `build.gradle`에서 라이브러리 의존성만 주입하면 스프링부트 구동 시, 스스로 빈 등록하는 것을 확인하기
 - 라이브러리측에서 자동구성(@AutoConfiguration)을 설정해주면 사용하는 클라이언트 측에서는 스프링부트가 스스로 자동구성을 찾고 빈으로 등록함

```java

/**
 * [자동구성을 위해 라이브러리 측 소스 추가해야하는 부분]
 *  - 클라이언트 측의 스프링부트가 구동될 때 컨디션 조건에 맞으면 빈을 자동으로 생성함
 */
@AutoConfiguration // 자동구성
@ConditionalOnProperty(name = "memory", havingValue = "on") // 컨디션 조건
public class MemoryAutoConfig {
    @Bean
    public MemoryController memoryController() {
        return new MemoryController(memoryFinder());
    }

    @Bean
    public MemoryFinder memoryFinder() {
        return new MemoryFinder();
    }
}
```
```text
// 자동구성 클래스를 입력할 파일생성
src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
```
```text
// AutoConfiguration.imports 파일
memory.MemoryAutoConfig
```

```text
// 외부 라이브러리 의존성 주입 (libs 폴더명 주의) / 라이브러리 @AutoConfiguration 미적용
implementation files('libs/memory-v1.jar')

// 외부 라이브러리 의존성 주입 (libs 폴더명 주의) / 라이브러리 @AutoConfiguration 적용
implementation files('libs/memory-v2.jar')
```
 - 클라이언트 build.gradle

#### 자동구성(@AutoConfiguration)이 적용되는 과정 알아보기
 - 스프링부트는 애플리케이션 동작 시, 모든 라이브러리의 `resources/META-INF/spring/
   org.springframework.boot.autoconfigure.AutoConfiguration.imports` 경로를 먼저 찾는다.
 - 해당 경로 파일에 명시된 클래스를 바탕으로 자동 빈 등록을 실행한다.
 - `spring-boot-autoconfigure` 라이브러리 `imports` 참고할 것
   - ```text
     org.springframework.boot.autoconfigure.aop.AopAutoConfiguration
     org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration
     org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration
     org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration
     org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration
     ...                
     ```
스프링부트는 어떤 방식으로 자동구성(@AutoConfiguration)을 인식할까?
![Desktop View](/images/2.png)
 - @SpringBootApplication
![Desktop View](/images/3.png)
 - @EnableAutoConfiguration - 자동구성을 활성화하는 애노테이션
![Desktop View](/images/4.png)
 - @Import(AutoConfigurationImportSelector.class) - 스프링 설정정보(@Configuration)을 포함

동작 순서는 위순서를 따라서 실행된다.

`AutoConfigurationImportSelector`는 `ImportSelector` 인터페이스를 구현하였다.




