# Spring-Security-User-and-Post
Spring security example with role Based access for User posts and user access approval

Annotation used:

@Enumerated(EnumType.String)
We can't directly store enums in DB hence we use the above annotation to store as String

@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
The above needs to be added in spring security config for using annotations like @PreAuthorize and @PostAuthorize

@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")


The below properties are different for each version of spring
server.port=8124
spring.datasource.username=root
spring.datasource.password=Kavinkumar9499@
spring.datasource.url=jdbc:mysql://localhost:3306/security
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.show-sql=true
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
logging.level.root=DEBUG
