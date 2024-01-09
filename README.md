## Task - Hibernate

1. Создать Web приложение учёта домов и жильцов.

### Описание:

1. 2 сущности: House, Person
2. Система должна предоставлять REST API для выполнения следующих операций:
   a. CRUD для House.
   i. В GET запросах не выводить информацию о Person
   b. CRUD для Person.
   i. В GET запросах не выводить информацию о House
   c. Для GET операций использовать pagination (default size: 15).

### House:

1. У House обязаны быть поля id, uuid, area, country, city, street, number, create_date
2. House может иметь множество жильцов (0-n).
3. У House может быть множество владельцев (0-n).
4. create_date устанавливается один раз при создании.

### Person:

1. У Person обязаны быть id, uuid, name, surname, sex, passport_series, passport_number, create_date,
   update_date
2. Person обязан жить только в одном доме и не может быть бездомным.
3. Person не обязан владеть хоть одним домом и может владеть множеством домов.
4. Сочетание passport_series и passport_number уникально.
5. sex должен быть [Male, Female].
6. Все связи обеспечить через id.
7. Не возвращать id пользователям сервисов, для этого предназначено поле uuid
8. create_date устанавливается один раз при создании.
9. update_date устанавливается при создании и изменяется каждый раз, когда меняется информация
   о Person. При этом, если запрос не изменяет информации, поле не должно обновиться.

### Примечание:

1. Ограничения и нормализацию сделать на своё усмотрение.
2. Поля представлены для хранения в базе данных. В коде могут отличаться.

### Обязательно:

1. GET для всех Person проживающих в House
2. GET для всех House, владельцем которых является Person
3. Конфигурационный файл: application.yml
4. Скрипты для создания таблиц должны лежать в classpath:db/
5. Добавить 5 домов и 10 жильцов (Один дом без жильцов, и как минимум в 1 доме больше 1
   владельца)

### Дополнительно:

1. *Добавить миграцию
2. *Полнотекстовый поиск (любое текстовое поле) для House
3. *Полнотекстовый поиск (любое текстовое поле) для Person
4. *PATCH для Person и House

### Application requirements

1. JDK version: 17 – use Streams, java.time.*, etc. where it is possible.
2. Application packages root: ru.clevertec.ecl.
3. Any widely-used connection pool could be used.
4. Spring JDBC Template should be used for data access.
5. Use transactions where it’s necessary.
6. Java Code Convention is mandatory (exception: margin size – 120 chars).
7. Build tool: Gradle, latest version.
8. Web server: Apache Tomcat.
9. Application container: Spring IoC. Spring Framework, the latest version.
10. Database: PostgreSQL, latest version.
11. Testing: JUnit 5.+, Mockito.
12. Service layer should be covered with unit tests not less than 80%.
13. Repository layer should be tested using integration tests with an in-memory
    embedded database or testcontainers.
14. As a mapper use Mapstruct.
15. Use lombok.

### General requirements

1. Code should be clean and should not contain any “developer-purpose”
   constructions.
2. App should be designed and written with respect to OOD and SOLID principles.
3. Code should contain valuable comments where appropriate.
4. Public APIs should be documented (Javadoc).
5. Clear layered structure should be used with responsibilities of each application
   layer defined.
6. JSON should be used as a format of client-server communication messages.
7. Convenient error/exception handling mechanism should be implemented: all errors
   should be meaningful on backend side. Example: handle 404 error:
   HTTP Status: 404
   response body {
8. “errorMessage”: “Requested resource not found (uuid = f4fe3df1-22cd-49ce-a54d-86f55a7f372e)”,
   “errorCode”: 40401
   }
   where *errorCode” is your custom code (it can be based on http status and
   requested resource - person or house)
   . Abstraction should be used everywhere to avoid code duplication.
   . Several configurations should be implemented (at least two - dev and prod).

### Application restrictions

It is forbidden to use:

1. Spring Boot.
2. Spring Data Repositories.
3. JPA.