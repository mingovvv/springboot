# springboot

- 스프링부트 핵심
    - WAS 톰캣 내장
    - 라이브러리 관리 - 손쉬운 빌드 구성을 위하여 스타터 종속성 제공, 스프링과 외부 라이브러리의 버전을 자동으로 관리
    - 자동 구성 - 프로젝트 시작에 필요한 스프링과 외부 라이브러리의 빈을 자동 등록
    - 외부 설정 - 서버에 따라 달라져야 하는 외부 설정 공통화 (dev/qa/release)
    - 프로덕션 준비 - 모니터링을 위한 메트릭, 상태 확인 기능

## 라이브러리 관리
 > 스프링부트 라이브러리 버전관리

- `id 'io.spring.dependency-management' version '1.1.0'` 플러그인을 통한 자동 버전관리(boot 버전 기준)
- 라이브러리 버전 관리 : 버전을 명시하지 않아도 라이브러리 사이의 호환성이 맞는 버전을 자동세팅해서 지원
- 스프링 부트 스타터 : 연관된 의존관계 라이브러리를 한번에 가져오는 방식을 지원

## 자동 구성(Auto-Config)
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

여기서 `AutoConfigurationImportSelector` 클래스는 `ImportSelector` 인터페이스를 구현한 구현체이다.

스프링부트는 2가지 방식으로 설정정보를 import 할 수 있다.
1. 정적인 방법
  - `@Import(className.class)` : 정적으로 명시된 클래스를 설정정보로 import
2. 동적인 방법
  - `@Import(ImportSelector)` : 동적으로 클래스를 인식하여 import

`AutoConfigurationImportSelector`는 모든 라이브러리의 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
경로를 탐색하고 내용을 읽어서 동적으로 설정정보를 import 하고 있는 것이다. 그것이 바로 스프링 컨테이너에 빈으로 등록되고 사용되는 것!

[테스트 코드](src/test/java/mingovvv/springboot/auto_config/selector/SelectorTest.java)
[Selector 구현체 코드](src/test/java/mingovvv/springboot/auto_config/selector/HelloImportSelector.java)

## 외부 설정
 - `OS 환경 변수`: OS에서 지원하는 외부 설정, 해당 OS를 사용하는 모든 프로세스에서 사용
 - `자바 시스템 속성`: 자바에서 지원하는 외부 설정, 해당 JVM안에서 사용
 - `자바 커맨드 라인 인수`: 커맨드 라인에서 전달하는 외부 설정, 실행시 main(args) 메서드에서 사용
 - `외부 파일(설정 데이터)`: 프로그램에서 외부 파일을 직접 읽어서 사용

#### OS 환경 변수
> OS를 사용하는 모든 프로그램에서 읽을 수 있는 설정값이다. 한마디로 다른 외부 설정과 비교해서 사용 범위가 가장 넓음

```java
Map<String, String> getenv = System.getenv();
for (String key : getenv.keySet()) {
    log.info("key = {}, value = {}", key, getenv.get(key));
}
```
 - 서버 자체의 OS 환경변수를 이용해서 prod/qa 애플리케이션을 구분
   - 운영서버 환경변수 : dburl = prod.com 
   - 개발서버 환경변수 : dburl = qa.com
 - 사용안함


#### 자바 시스템 속성
> 자바 시스템 속성은 실행되고 있는 `JVM` 안에서 접근 가능한 외부 설정

```java
Properties properties = System.getProperties();
for (Object key : properties.keySet()) {
    log.info("key = {}, value = {}", key, properties.get(key));
}

String url = System.getProperty("url");
String username = System.getProperty("username");
String password = System.getProperty("password");

log.info("url = {}, username = {}, password = {}", url, username, password);

// 런타임 환경에서 추가 가능
System.setProperty("dialect", "MySQL");
```
 - `-D` 옵션을 통해 실행 시점에서 외부 설정을 추가
   - VM option : `-Durl=mingo.com -Dusername=mingo -Dpassword=mingo`
   - java 실행 시 : `java -Durl=mingo.com -Dusername=mingo -Dpassword=mingo -jar app.jar`
 - `System.setProperty(key, value)` 를 통해 런타임 환경에서 추가 


#### 자바 커맨드 라인 인수
> 커맨드 라인 인수(Command line arguments)는 애플리케이션 실행 시점에 외부 설정값을 `main(args)` 메서드의 `args` 파라미터로 전달하는 방법

```java
public static void main(String[] args) {
    for (String arg : args) {
        log.info("arg : {}", arg);
    }

}
```

 - program arguments option : `url username password`
 - java 실행 시 : `java -jar app.jar url username password`
 - 통 문자열을 공백을 기준으로 split(공백을 연결하려면 double quotation으로 문자를 감싸면 됨)
 - key-value 형태가 아니라서 번거로움

**커맨드 라인 옵션 인수 등장**
```java
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
```
 - 커맨드 라인 인수를 key=value 형식으로 구분하는 방법으로 나온 `커맨드 라인 옵션 인수`
 - Spring 표준
 - `--` dash 2개를 연결해서 사용하면 key=value 형식으로 program arguments를 받을 수 있음
 - `DefaultApplicationArguments` 를 구현해야 함

**커맨드 라인 옵션 + 스프링을 통해 사용하는 방법**
```java
@Slf4j
@Component
public class CommandLineBean {

    private final ApplicationArguments arguments;

    public CommandLineBean(ApplicationArguments arguments) {
        this.arguments = arguments;
    }

    @PostConstruct
    public void init() {
        log.info("source {}", List.of(arguments.getSourceArgs()));
        log.info("optionNames {}", arguments.getOptionNames());
        Set<String> optionNames = arguments.getOptionNames();
        for (String optionName : optionNames) {
            log.info("option args {}={}", optionName, arguments.getOptionValues(optionName));
        }
    }

}
```
 - 스프링부트는 커맨드라인, 커맨드라인옵션 인수 모두를 사용할 수 있는 `ApplicationArguments`를 스프링 빈으로 등록해준다.
 - 의존성 주입만 받는다면 어디서든 커맨드라인, 커맨드라인옵션를 사용할 수 있음

##### 외부설정 스프링 통합
> OS 환경변수, 자바 시스템 속성, 커맨드 라인 옵션 인수 모두 외부 설정을 key-value 형식으로 사용할 수 있지만 제공해주는 곳에 따라 읽는 법이 다르다. getenv(), getProperty() 라던지.. 이것을 스프링부트는 통합하였다. 

![Desktop View](/images/5.png)
 - 부트는 `org.springframework.core.env.PropertySource` 추상 클래스를 만들고 모든 외부 환경 제공자의 구현체를 만들어 둠
 - 스프링은 `Environment` 인터페이스를 통해 동일한 명령어로 외부 환경 구현체에 접근할 수 있음
 - 특정 외부 환경에 종속적인 코딩에서 벗어날 수 있음
 - 동일한 key가 존재할 수 있으므로 부트는 외부 환경 `우선순위`를 정해두었음


## 외부 파일(설정 데이터) -> 내부 파일
> OS 환경변수, 자바 시스템 속성, 커맨드 라인 옵션 인수가 증가할수록 관리하기 힘들어지므로 파일로 관리하는 방법이 생김. 외부 파일로 관리하는 것보다 내부 파일로 관리하는 장점이 훨씬 많음.

#### `.properties` 파일 사용하기
 - key-value 형식
 - 프로젝트 내에서 소스코드와 함께 관리됨 -> 빌드 시 포함됨
 - 빌드된 프로젝트는 개발과 운영 모두의 설정 데이터를 가지고 있음. 환경변수에 따라 특정 데이터를 읽어서 적용함.
   - 규칙 : `application-{profile}.properties`
   - 개발용 설정파일 : `application-dev.properties`
   - 운영용 설정파일 : `application-prod.properties`
 - `profile` 을 적용해서 설정파일을 선택
   - 커멘드 라인 옵션 인수 : `--spring.profiles.active=dev`
   - VM 시스템 속성 : `-Dspring.profiles.active=dev`

#### `.properties` 파일 하나로 사용하기
 - 물리적인 `.properties` 파일 하나로 논리적인 프로필 영역을 구성할 수 있음
 - ```properties
    spring.config.activate.on-profile=dev
    url=dev.mingovvv.com
    username=dev_mingo
    password=dev_mingo
    #---
    spring.config.activate.on-profile=prod
    url=prod.mingovvv.com
    username=prod_mingo
    password=prod_mingo
   ```
 - `#---`를 통해서 논리적으로 영역을 구분 / yml은 `---`
 - `spring.config.activate.on-profile` 로 프로필을 지정해 주어야 함

## 우선순위
 - https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config

자주 사용하는 우선순위 (내려갈 수록 우선순위가 높다)
- 설정 데이터( application.properties )
- OS 환경변수
- 자바 시스템 속성
- 커맨드 라인 옵션 인수
- @TestPropertySource (테스트에서 사용)
설정 데이터 우선순위
- jar 내부 application.properties
- jar 내부 프로필 적용 파일 application-{profile}.properties
- jar 외부 application.properties
- jar 외부 프로필 적용 파일 application-{profile}.properties

더 유연한 것이 우선권을 가진다. (변경하기 어려운 파일 보다 실행시 원하는 값을 줄 수 있는 자바 시스템 속성이 더 우선권을 가진다.)
범위가 넒은 것 보다 좁은 것이 우선권을 가진다. 
 - OS 환경변수 보다 자바 시스템 속성이 우선권이 있다.
 - 자바 시스템 속성 보다 커맨드 라인 옵션 인수가 우선권이 있다

## 외부설정 조회해오기
- Environment
- @Value
- @ConfigurationProperties

#### Environment
```java
@Slf4j
//@Configuration
public class MyDataSourceEnvConfig {

    private final Environment env;

    public MyDataSourceEnvConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public MyDataSource myDataSource() {
        String url = env.getProperty("my.datasource.url");
        String username = env.getProperty("my.datasource.username");
        String password = env.getProperty("my.datasource.password");
        int maxConnection = env.getProperty("my.datasource.etc.max-connection", Integer.class);
        Duration timeout = env.getProperty("my.datasource.etc.timeout", Duration.class);
        List<String> options = env.getProperty("my.datasource.etc.options", List.class);
        return new MyDataSource(url, username, password, maxConnection, timeout, options);
    }


}
```
 - 타입변환 제공 : `Environment.getProperty(key, Type);`
   - https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config.typesafe-configuration-properties.conversion

#### @Value
```java
@Slf4j
@Configuration
public class MyDataSourceValueConfig {

    // 필드주입
    @Value("${my.datasource.url}")
    private String url;
    @Value("${my.datasource.username}")
    private String username;
    @Value("${my.datasource.password}")
    private String password;
    @Value("${my.datasource.etc.max-connection}")
    private int maxConnection;
    @Value("${my.datasource.etc.timeout}")
    private Duration timeout;
    @Value("${my.datasource.etc.options}")
    private List<String> options;
    
    @Bean
    public MyDataSource myDataSource1() {
        return new MyDataSource(url, username, password, maxConnection, timeout, options);
    }

    // 파라미터 주입
    @Bean
    public MyDataSource myDataSource2(
            @Value("${my.datasource.url}") String url,
            @Value("${my.datasource.username}") String username,
            @Value("${my.datasource.password}") String password,
            @Value("${my.datasource.etc.max-connection}") int maxConnection,
            @Value("${my.datasource.etc.timeout}") Duration timeout,
            @Value("${my.datasource.etc.options}") List<String> options) {
        return new MyDataSource(url, username, password, maxConnection, timeout, options);
    }

}
```
 - `@Value(${...})` 를 통해 외부설정 조회
 - 필드주입 / 파라미터 주입 둘 다 지원
 - default값 설정 가능 : `@Value(${test:mingo})` / default = mingo

#### @ConfigurationProperties
> 외부설정을 묶음으로 가져와 객체와 매핑하는 기능을 제공한다. type-safety한 설정 속성이다.

```properties
# application.properties
my.datasource.url=local.db.com
my.datasource.username=local_user
my.datasource.password=local_pw
my.datasource.etc.max-connection=1
my.datasource.etc.timeout=3500ms
my.datasource.etc.options=CACHE,ADMIN

```

#### @ConfigurationProperties 세터 주입

```java
@Data
@ConfigurationProperties("my.datasource")
public class MyDataSourcePropertiesV1 {

    private String url;
    private String username;
    private String password;
    private Etc etc = new Etc();

    @Data
    public static class Etc {
        private int maxConnection;
        private Duration timeout;
        private List<String> options = new ArrayList<>();
    }

}
```
 - 외부 설정을 받을 객체를 생성
 - `@ConfigurationProperties("my.datasource")` : 설정의 공통 prefix 명시적으로 작성
 - 자바빈 프로퍼티 방식이라 `getter`, `setter` 가 필요함

```java
@Configuration
@EnableConfigurationProperties(MyDataSourcePropertiesV1.class)
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
```
 - `@EnableConfigurationProperties(MyDataSourcePropertiesV1.class)` 사용할 프로퍼티 객체 명시
   - 명시된 class는 스프링 빈으로 등록되고 DI 받아서 사용할 수 있음
 - 표기법 변환 : `max-connection`는 `maxConnection`로 자동 컨버팅
 - `@ConfigurationPropertiesScan` 를 통해 범위 스캔 가능 (`@EnableConfigurationProperties` 생략 가능)

#### @ConfigurationProperties 생성자 주입

```java
@Getter
@ConfigurationProperties("my.datasource")
public class MyDataSourcePropertiesV2 {

    private String url;
    private String username;
    private String password;
    private Etc etc = new Etc();

    public MyDataSourcePropertiesV2(String url, String username, String password, Etc etc) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.etc = etc;
    }

    @Getter
    public static class Etc {

        private int maxConnection;
        private Duration timeout;
        private List<String> options = new ArrayList<>();

        public Etc(int maxConnection, Duration timeout, @DefaultValue("DEFAULT") List<String> options) {
            this.maxConnection = maxConnection;
            this.timeout = timeout;
            this.options = options;
        }

    }

}
```
 - 생성자를 통한 외부설정 값 주입
 - `@Getter` /  `@DefaultValue("DEFAULT")` 디폴트 값 사용 가능
 - 스프링부트 3.0 이하는 생성자에 `@ConstructorBinding` 넣어주어야 함

## 프로덕션 준비 기능

> 지표(metric), 추적(trace), 감사(auditing), 모니터링

#### Actuator

라이브러리 추가
 - `implementation 'org.springframework.boot:spring-boot-starter-actuator'`

```text
// http://localhost:8080/actuator

{
  "_links": {
    "self": {
      "href": "http://localhost:8080/actuator",
      "templated": false
    },
    "health-path": {
      "href": "http://localhost:8080/actuator/health/{*path}",
      "templated": true
    },
    "health": {
      "href": "http://localhost:8080/actuator/health",
      "templated": false
    }
  }
}
```
 - `management.endpoints.web.exposure.include=*` : properties 활성화시키면 더 많은 정보를 제공받음

```text
// http://localhost:8080/actuator/health

{
  "status": "UP"
}
```
 - 서버 상태를 의미함

기본적으로 `엔드포인트 활성화`, `엔드포인트 노출` 두 단계가 필요하다.
 - `엔드포인트 활성화` : 기능 on/off 
 - `엔드포인트 노출` : 엔드포인트 제공할 소스 선택 -> HTTP / JMX 

#### 다양한 엔드포인트

- beans : 스프링 컨테이너에 등록된 스프링 빈을 보여준다.
- conditions : condition 을 통해서 빈을 등록할 때 평가 조건과 일치하거나 일치하지 않는 이유를 표시한다.
- configprops : @ConfigurationProperties 를 보여준다.
- env : Environment 정보를 보여준다.
- health : 애플리케이션 헬스 정보를 보여준다.
- httpexchanges : HTTP 호출 응답 정보를 보여준다. `HttpExchangeRepository` 를 구현한 빈을 별도로 등록해야 한다.
- info : 애플리케이션 정보를 보여준다.
- loggers : 애플리케이션 로거 설정을 보여주고 변경도 할 수 있다.
- metrics : 애플리케이션의 메트릭 정보를 보여준다.
- mappings : @RequestMapping 정보를 보여준다.
- threaddump : 쓰레드 덤프를 실행해서 보여준다.
- shutdown : 애플리케이션을 종료한다. 이 기능은 기본으로 비활성화 되어 있다.

스프링 공식 문서
> https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints


