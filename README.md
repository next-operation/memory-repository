# Memory repository

![GitHub Workflow Status](https://img.shields.io/github/workflow/status/next-operation/memory-repository/Validate%20Gradle%20Wrapper)
![GitHub](https://img.shields.io/github/license/next-operation/memory-repository)
[![](https://jitpack.io/v/next-operation/memory-repository.svg)](https://jitpack.io/#next-operation/memory-repository)

> Inspired by JPA, easy-to-use memory-based storage.

This project was started to share data without using JobExecutionContext in Spring batch.

Of course, Spring batch does not recommend sharing this data, but sometimes you may need to
reference multiple pieces of data to handle a particular task. If we don't have a lot of data to
reference, we choose to store it in memory and read it globally in the job flow. So this project was
created.

## Add dependency

### Gradle

```groovy
repositories {
    mavenCentral()
    maven { url "https://jitpack.io" }
}
dependencies {
    implementation 'com.github.next-operation:memory-repository:1.0.7'
}
```

### Maven

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependency>
  <groupId>com.github.next-operation</groupId>
    <artifactId>memory-repository</artifactId>
  <version>1.0.7</version>
</dependency>
```

## How to use

### with Spring Batch

First, implement `MemoryRepository`. In this example, we will use `SimpleRepository`.

```java
@Repository // or @Component
public class StringRepository extends SimpleRepository<String> {
}
```

Now, we can use it in a Spring Batch job.

Create a new configuration class.

```java
@Configuration
@RequiredArgsConstructor
public class BatchConfiguration {

    private final StringRepository stringRepository;

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<String, String>chunk(10)
                .reader(itemReader())
                .writer(stringRepository::saveAll)
                .build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .start(step1())
                .build();
    }
}
```

Done!

When reading data in repository, you can typically use `ListItemReader`.

```java
@Bean
public ListItemReader<String> repositoryItemReader(){
    return new ListItemReader<>(stringRepository.findAll());
}
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
