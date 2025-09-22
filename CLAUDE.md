# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is the eGovFrame Runtime Environment 4.3.0 (전자정부 표준프레임워크 실행환경), a comprehensive Java-based framework for Korean government applications. It's built on Spring Framework 5.3.37, Spring Boot 2.7.18, and Java 8+.

## Architecture

The project follows a layered architecture organized into four main categories:

### Foundation Layer (`Foundation/`)
Core utilities and common services:
- `fdl.cmmn`: Common functionality, exception handling, abstract service implementations
- `fdl.logging`: Logging services using SLF4J
- `fdl.property`: Configuration management
- `fdl.security`: Spring Security integration
- `fdl.crypto`: ARIA encryption/decryption services
- `fdl.string`: String utilities and validation
- `fdl.excel`: Excel file handling
- `fdl.filehandling`: File upload/download services
- `fdl.idgnr`: ID generation services
- `fdl.xml`: XML processing utilities
- `fdl.access`: Session-based access control
- `fdl.reactive`: WebFlux reactive support

### Persistence Layer (`Persistence/`)
Data access and database services:
- `psl.dataaccess`: Database connection, transaction management (MyBatis/iBATIS)
- `psl.data.jpa`: Spring JPA integration
- `psl.data.mongodb`: MongoDB integration
- `psl.reactive.*`: Reactive database support (R2DBC, Redis, MongoDB, Cassandra)

### Presentation Layer (`Presentation/`)
Web and UI services:
- `ptl.mvc`: Spring MVC support
- `ptl.reactive`: Reactive web support
- `spring-modules-validation`: Form validation

### Integration Layer (`Integration/`)
System integration services:
- `itl.integration`: Inter-system integration standards
- `itl.webservice`: Web service client/server support

### Batch Layer (`Batch/`)
Batch processing:
- `bat.core`: Spring Batch integration for bulk data processing

## Development Commands

### Building the Project
```bash
# Build all modules
mvn clean install

# Build specific module
cd Foundation/org.egovframe.rte.fdl.cmmn
mvn clean install

# Skip tests during build
mvn clean install -DskipTests
```

### Running Tests
```bash
# Run all tests in a module
mvn test

# Run specific test class
mvn test -Dtest=FdlExceptionTest

# Run tests with coverage reporting
mvn emma:emma
```

### Code Quality
```bash
# Run PMD code analysis
mvn pmd:pmd

# Generate JavaDoc
mvn javadoc:javadoc

# Generate project reports
mvn site
```

## Common Patterns

### Service Implementation
- All business services should extend `EgovAbstractServiceImpl`
- Use the provided `egovLogger` for logging instead of creating new loggers
- Handle exceptions using `processException()` methods for consistent error handling
- Use `leaveaTrace()` for audit trails without exceptions

### Exception Handling
- Use `EgovBizException` for business logic errors
- Use `FdlException` for framework-level errors
- Always provide meaningful error messages using message resources
- Include proper exception chaining when wrapping exceptions

### Testing
- All tests use JUnit 4 with Spring Test Context
- Test classes should extend `AbstractJUnit4SpringContextTests`
- Use `@ContextConfiguration` to load Spring contexts from `META-INF/spring/*.xml`
- Test configuration files are located in `src/test/resources`
- Tests are disabled by default in Maven build (`<skipTests>true</skipTests>`)

### Configuration
- Spring XML configuration files are in `src/main/resources/META-INF/spring/`
- Message resources use Spring MessageSource
- Dependency injection uses `@Resource` annotations
- Java 8 source/target compatibility

## Module Dependencies

Each module defines its own dependencies in its `pom.xml`. Common dependencies include:
- Spring Framework 5.3.37
- AspectJ for AOP
- Apache Commons Lang3
- SLF4J for logging
- JUnit 4.13.2 for testing

## Version Information

- Framework Version: 4.3.0
- Java Version: 1.8+
- Spring Framework: 5.3.37
- Spring Security: 5.8.13
- Maven Compiler: Java 8 source/target
- Encoding: UTF-8

## Testing Notes

- Tests are currently disabled in the Maven build configuration
- Each module has comprehensive JUnit test suites
- Test databases use HSQLDB for integration testing
- Some modules include performance and load testing scenarios
- don't commit anything. I don't want to request AI workflow PR to original repository