### Инструкция по запуску проекта:

### Запуск через Tomcat:

С помощью IDEA:

- Создать базу данных в PostgreSQL с названием "house-manager"
  (все конфигурационные параметры можно изменить в файле application.yml - src/main/resources/application.yml)
- запустить приложение с помощью Apache Tomcat с параметром Context path: /

С помощью сервера Apache Tomcat:

- создать базу данных в PostgreSQL с названием "house-manager"
  (все конфигурационные параметры можно изменить в файле application.yml - src/main/resources/application.yml)
- в корневой папке приложения прописать в консоли: gradle build
- зайти в папку build/libs/ взять файл clevertec.war и переместить в папку webapps от корневой папки расположения Apache
  Tomcat

### CRUD операции в Application классе:

### Пример объекта House в формате JSON:

    {
    "uuid": "ac602dde-9556-4ef5-954c-aeebc42c5056",
    "area": "Минская область",
    "country": "Беларусь",
    "city": "Минск",
    "street": "Разинская",
    "number": "111"
    {

### Пример объекта Person в формате JSON:

    {
        "uuid": "79e17dfd-a27d-45b6-8c72-a15538b8216e",
        "name": "Лазарева",
        "surname": "Мария",
        "sex": "FEMALE",
        "passportSeries": "MC",
        "passportNumber": "7777777",
        "house": {
        "uuid": "a85a721b-c62b-428e-a777-3daa72fc5e3a"
    }
    }

##### Пример Create House:

- Отправляем POST запрос с телом HouseRequest объекта в формате JSON по ссылке: http://localhost:8080/houses
- Получаем результат созданный объект HouseResponse.

##### Пример Create Person:

- Отправляем POST запрос с телом PersonRequest объекта в формате JSON по ссылке: http://localhost:8080/persons
- Получаем результат созданный объект PersonResponse.

##### Пример GetAll Houses:

- Отправляем GET запрос по ссылке: http://localhost:8080/houses
- Также можно указать дополнительные параметры pagesize, offset, page
  Например: http://localhost:8080/houses?pagesize=2
- Получаем результатом ответ от сервера список House из количества элементов не больше указанного (в application.yml
  файле в параметре pagination.limit) в формате JSON.

##### Пример GetAll Persons:

- Отправляем GET запрос по ссылке: http://localhost:8080/persons
- Также можно указать дополнительные параметры pagesize, offset, page
  Например: http://localhost:8080/persons?pagesize=2
- Получаем результатом ответ от сервера список Peron из количества элементов не больше указанного (в application.yml
  файле в параметре pagination.limit) в формате JSON.

##### Пример Get by UUID House:

- Отправляем GET запрос по ссылке с параметром UUID.
  Например: http://localhost:8080/houses/36da71f5-173f-431a-9844-0a33eed3b7c5
- Получаем результатом объект HouseResponse в формате JSON.

##### Пример Get by UUID House:

- Отправляем GET запрос по ссылке с параметром ID.
  Например: http://localhost:8080/clevertec/36da71f5-173f-431a-9844-0a33eed3b7c5
- Получаем результатом объект PersonResponse в формате JSON.

##### Пример Update House:

- Отправляем PUT запрос HouseRequest в теле запроса параметром uuid передаём UUID объекта для изменения.
  Например: http://localhost:8080/houses
- Результатом придёт ответ с изменённым объектом House в формате JSON.

##### Пример Update Person:

- Отправляем PUT запрос PersonRequest в теле запроса параметром uuid передаём UUID объекта для изменения.
  Например: http://localhost:8080/persons
- Результатом придёт ответ с изменённым объектом Person в формате JSON.

##### Пример DeleteById House:

- Отправляем DELETE запрос по ссылке с параметром UUID.
  Например: http://localhost:8080/houses/36da71f5-173f-431a-9844-0a33eed3b7c5
- После отправки данного запроса ответом от сервера будет результирующий статус NO CONTENT.

##### Пример DeleteById Person:

- Отправляем DELETE запрос по ссылке с параметром UUID.
  Например: http://localhost:8080/persons/36da71f5-173f-431a-9844-0a33eed3b7c5
- После отправки данного запроса ответом от сервера будет результирующий статус NO CONTENT.

##### Пример getByPersonId Houses:

- Отправляем GET запрос по ссылке с параметром UUID объекта Person.
  Например: http://localhost:8080/houses/person/ea00be28-1ce2-48e4-b4b3-c0e98c258201
- Получаем результатом ответ от сервера список House владельцем которых является Person.

##### Пример getByHouseId Persons:

- Отправляем GET запрос по ссылке с параметром UUID объекта House.
  Например: http://localhost:8080/persons/house/79e17dfd-a27d-45b6-8c72-a15538b8216e
- Получаем результатом ответ от сервера список Person проживающих в House.