### Инструкция по запуску проекта:

### Запуск через Tomcat:

С помощью IDE:

- Создать базу данных в PostgreSQL с названием "house-manager"
  (все конфигурационные параметры можно изменить в файле application.yml - src/main/resources/application.yml)
- запустить Application.class

### CRUD операции:

##### Пример Create House:

Отправляем POST запрос с телом HouseRequest объекта в формате JSON по ссылке: http://localhost:8080/houses

    {
    "country": "Беларусь",
    "area": "Витебская область",
    "city": "Витебск",
    "street": "Центральная",
    "number": "1"
    }

Получаем результат созданный объект HouseResponse:

    {
    "uuid": "23f2c16f-7011-4c1a-b96e-097dba86aa10",
    "country": "Беларусь",
    "area": "Витебская область",
    "city": "Витебск",
    "street": "Центральная",
    "number": "1",
    "createDate": "2024-01-25T23:27:00.045"
    }

##### Пример Create Person:

Отправляем POST запрос с телом PersonRequest объекта в формате JSON по ссылке: http://localhost:8080/persons

    {
    "name": "Михаил",
    "surname": "Инванов",
    "sex": "MALE",
    "passportSeries": "MC",
    "passportNumber": "9999999",
    "tenantHouseUuidRequest": "a85a721b-c62b-428e-a777-3daa72fc5e3a",
    "ownerHousesUuidRequest": [
    "a85a721b-c62b-428e-a777-3daa72fc5e3a"
    ]
    }

Получаем результат созданный объект PersonResponse:

    {
    "uuid": "73764f2d-4a80-4646-8da6-05206d70ecb9",
    "name": "Михаил",
    "surname": "Инванов",
    "sex": "MALE",
    "passportSeries": "MC",
    "passportNumber": "9999999",
    "createDate": "2024-01-25T23:29:13.639",
    "updateDate": "2024-01-25T23:29:13.639"
    }

##### Пример GetAll Houses:

Отправляем GET запрос по ссылке: http://localhost:8080/houses
Также можно указать дополнительные параметры page, size, sort

Например: http://localhost:8080/houses?pagesize=2

Получаем результатом ответ от сервера список House из количества элементов не больше указанного (в application.yml файле
в параметре pagination.limit) в формате JSON в параметре content:

    {
    "content": [
    {
    "uuid": "ac602dde-9556-4ef5-954c-aeebc42c5056",
    "country": "Беларусь",
    "area": "Минская область",
    "city": "Минск",
    "street": "Разинская",
    "number": "99",
    "createDate": "2024-01-03T09:12:15.156"
    },
    {
    "uuid": "061783b1-c63b-4fb2-a9d0-9d90842911a2",
    "country": "Беларусь",
    "area": "Минская область",
    "city": "Солигорск",
    "street": "К.Заслонова",
    "number": "52",
    "createDate": "2024-01-03T09:12:15.156"
    }
    ],
    "pageable": {
    "pageNumber": 0,
    "pageSize": 2,
    "sort": {
    "empty": true,
    "unsorted": true,
    "sorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
    },
    "last": false,
    "totalPages": 6,
    "totalElements": 11,
    "size": 2,
    "number": 0,
    "sort": {
    "empty": true,
    "unsorted": true,
    "sorted": false
    },
    "first": true,
    "numberOfElements": 2,
    "empty": false
    }

##### Пример GetAll Persons:

Отправляем GET запрос по ссылке: http://localhost:8080/persons
Также можно указать дополнительные параметры page, size, sort

Например: http://localhost:8080/persons?pagesize=2

Получаем результатом ответ от сервера список Peron из количества элементов не больше указанного (в application.yml файле
в параметре pagination.limit) в формате JSON в параметре content:

    {
    "content": [
    {
    "uuid": "03736b7f-3ca4-4af7-99ac-07628a7d8fe6",
    "name": "София",
    "surname": "Макарова",
    "sex": "FEMALE",
    "passportSeries": "MC",
    "passportNumber": "6373235",
    "createDate": "2022-11-11T06:12:15.156",
    "updateDate": "2022-11-11T06:12:15.156"
    },
    {
    "uuid": "12ff37bc-3fc8-47cd-8b18-d0b3fb35597b",
    "name": "Герман",
    "surname": "Куликов",
    "sex": "MALE",
    "passportSeries": "MA",
    "passportNumber": "2634736",
    "createDate": "2021-01-21T06:12:15.156",
    "updateDate": "2021-01-21T06:12:15.156"
    }
    ],
    "pageable": {
    "pageNumber": 0,
    "pageSize": 2,
    "sort": {
    "empty": true,
    "unsorted": true,
    "sorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
    },
    "last": false,
    "totalPages": 6,
    "totalElements": 11,
    "size": 2,
    "number": 0,
    "sort": {
    "empty": true,
    "unsorted": true,
    "sorted": false
    },
    "first": true,
    "numberOfElements": 2,
    "empty": false
    }

##### Пример Get by UUID House:

Отправляем GET запрос по ссылке с параметром UUID.

Например: http://localhost:8080/houses/8ca3955d-b436-471d-872f-f2ce07ac3f15

Получаем результатом объект HouseResponse в формате JSON:

    {
    "uuid": "8ca3955d-b436-471d-872f-f2ce07ac3f15",
    "country": "Беларусь",
    "area": "Витебская область",
    "city": "Витебск",
    "street": "Чкалова",
    "number": "11",
    "createDate": "2024-01-03T09:12:15.156"
    }

##### Пример Get by UUID Person:

Отправляем GET запрос по ссылке с параметром UUID.

Например: http://localhost:8080/persons/79e17dfd-a27d-45b6-8c72-a15538b8216e

Получаем результатом объект PersonResponse в формате JSON:

    {
    "uuid": "79e17dfd-a27d-45b6-8c72-a15538b8216e",
    "name": "Мария",
    "surname": "Лазарева",
    "sex": "FEMALE",
    "passportSeries": "MP",
    "passportNumber": "7332632",
    "createDate": "2018-08-29T06:12:15.156",
    "updateDate": "2024-01-03T09:12:15.156"
    }

##### Пример Update House:

Отправляем PUT запрос HouseRequest в теле запроса параметром uuid передаём UUID объекта для изменения.

Например: http://localhost:8080/houses

    {
    "uuid": "78cdcc8d-07df-496b-86aa-65aadd4cfc77",
    "country": "Беларусь",
    "area": "Могилевская область",
    "city": "Могилев",
    "street": "Главная",
    "number": "222"
    }

Результатом придёт ответ с изменённым объектом House в формате JSON:

    {
    "uuid": "78cdcc8d-07df-496b-86aa-65aadd4cfc77",
    "country": "Беларусь",
    "area": "Могилевская область",
    "city": "Могилев",
    "street": "Главная",
    "number": "222",
    "createDate": "2023-09-29T06:05:15.156"
    }

##### Пример Update Person:

Отправляем PUT запрос PersonRequest в теле запроса параметром uuid передаём UUID объекта для изменения.

Например: http://localhost:8080/persons

    {
        "uuid": "79e17dfd-a27d-45b6-8c72-a15538b8216e",
        "name": "Лазарева",
        "surname": "Мария",
        "sex": "FEMALE",
        "passportSeries": "MC",
        "passportNumber": "7777777",
        "tenantHouseUuidRequest": "a85a721b-c62b-428e-a777-3daa72fc5e3a"
    }

Результатом придёт ответ с изменённым объектом Person в формате JSON:

    {
    "uuid": "79e17dfd-a27d-45b6-8c72-a15538b8216e",
    "name": "Лазарева",
    "surname": "Мария",
    "sex": "FEMALE",
    "passportSeries": "MC",
    "passportNumber": "7777777",
    "createDate": "2018-08-29T06:12:15.156",
    "updateDate": "2024-01-25T23:34:37.208"
    }

##### Пример DeleteById House:

Отправляем DELETE запрос по ссылке с параметром UUID.

Например: http://localhost:8080/houses/524fa37c-f2ed-4e08-b072-910708095e97
После отправки данного запроса ответом от сервера будет результирующий статус NO CONTENT.

##### Пример DeleteById Person:

Отправляем DELETE запрос по ссылке с параметром UUID.

Например: http://localhost:8080/persons/07b74899-51b7-4ad4-8098-ddd51efd209e

После отправки данного запроса ответом от сервера будет результирующий статус NO CONTENT.

##### Пример getByPersonId Houses:

Отправляем GET запрос по ссылке с параметром UUID объекта Person.

Например: http://localhost:8080/persons/ea00be28-1ce2-48e4-b4b3-c0e98c258201/houses
Также можно указать дополнительные параметры page, size, sort

Получаем результатом ответ от сервера список House владельцем которых является Person.

    {
    "content": [
    {
    "uuid": "8ca3955d-b436-471d-872f-f2ce07ac3f15",
    "country": "Беларусь",
    "area": "Витебская область",
    "city": "Витебск",
    "street": "Чкалова",
    "number": "11",
    "createDate": "2024-01-03T09:12:15.156"
    }
    ],
    "pageable": {
    "pageNumber": 0,
    "pageSize": 15,
    "sort": {
    "empty": true,
    "unsorted": true,
    "sorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 1,
    "size": 15,
    "number": 0,
    "sort": {
    "empty": true,
    "unsorted": true,
    "sorted": false
    },
    "first": true,
    "numberOfElements": 1,
    "empty": false
    }

##### Пример getByHouseId Persons:

Отправляем GET запрос по ссылке с параметром UUID объекта House.

Например: http://localhost:8080/persons/house/79e17dfd-a27d-45b6-8c72-a15538b8216e
Также можно указать дополнительные параметры page, size, sort

Получаем результатом ответ от сервера список Person проживающих в House.

    {
    "content": [
    {
    "uuid": "03736b7f-3ca4-4af7-99ac-07628a7d8fe6",
    "name": "София",
    "surname": "Макарова",
    "sex": "FEMALE",
    "passportSeries": "MC",
    "passportNumber": "6373235",
    "createDate": "2022-11-11T06:12:15.156",
    "updateDate": "2022-11-11T06:12:15.156"
    }
    ],
    "pageable": {
    "pageNumber": 0,
    "pageSize": 15,
    "sort": {
    "empty": true,
    "unsorted": true,
    "sorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 1,
    "size": 15,
    "number": 0,
    "sort": {
    "empty": true,
    "unsorted": true,
    "sorted": false
    },
    "first": true,
    "numberOfElements": 1,
    "empty": false
    }

##### Пример getByNameMatches Houses:

Отправляем GET запрос по ссылке с названием интересующей страны, области, города либо улицы.

Например: http://localhost:8080/houses/search/Чкалова
Также можно указать дополнительные параметры page, size, sort

Получаем результатом ответ от сервера список House по совпадениям.

    {
    "content": [
    {
    "uuid": "8ca3955d-b436-471d-872f-f2ce07ac3f15",
    "country": "Беларусь",
    "area": "Витебская область",
    "city": "Витебск",
    "street": "Чкалова",
    "number": "11",
    "createDate": "2024-01-03T09:12:15.156"
    }
    ],
    "pageable": {
    "pageNumber": 0,
    "pageSize": 15,
    "sort": {
    "empty": true,
    "unsorted": true,
    "sorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 1,
    "size": 15,
    "number": 0,
    "sort": {
    "empty": true,
    "unsorted": true,
    "sorted": false
    },
    "first": true,
    "numberOfElements": 1,
    "empty": false
    }

##### Пример getByNameMatches Persons:

Отправляем GET запрос по ссылке с названием имени, фамилии, серии или номер паспорта.

Например: http://localhost:8080/persons/search/Полина
Также можно указать дополнительные параметры page, size, sort

Получаем результатом ответ от сервера список Person по совпадениям:

    {
    "content": [
    {
    "uuid": "fd839347-a17c-44f0-a8b7-77b53d8a652d",
    "name": "Полина",
    "surname": "Леонова",
    "sex": "FEMALE",
    "passportSeries": "MC",
    "passportNumber": "1593875",
    "createDate": "2023-12-28T07:12:15.156",
    "updateDate": "2023-12-28T07:12:15.156"
    }
    ],
    "pageable": {
    "pageNumber": 0,
    "pageSize": 15,
    "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 1,
    "size": 15,
    "number": 0,
    "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
    },
    "first": true,
    "numberOfElements": 1,
    "empty": false
    }

##### Пример getTenantsByHouseUuid Persons:

Отправляем GET запрос по ссылке с параметром UUID объекта House.

Например: http://localhost:8080/houses/a85a721b-c62b-428e-a777-3daa72fc5e3a/tenants
Также можно указать дополнительные параметры page, size, sort

Получаем результатом ответ от сервера список из истории House в котором проживал Person.

    {
    "content": [
    {
    "uuid": "12ff37bc-3fc8-47cd-8b18-d0b3fb35597b",
    "name": "Герман",
    "surname": "Куликов",
    "sex": "MALE",
    "passportSeries": "MA",
    "passportNumber": "2634736",
    "createDate": "2021-01-21T06:12:15.156",
    "updateDate": "2021-01-21T06:12:15.156"
    }
    ],
    "pageable": {
    "pageNumber": 0,
    "pageSize": 15,
    "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 1,
    "size": 15,
    "number": 0,
    "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
    },
    "first": true,
    "numberOfElements": 1,
    "empty": false
    }

##### Пример getOwnersByHouseUuid Persons:

Отправляем GET запрос по ссылке с параметром UUID объекта House.

Например: http://localhost:8080/houses/8126ee0f-edad-41fc-b845-41061439c652/owners
Также можно указать дополнительные параметры page, size, sort

Получаем результатом ответ от сервера список из истории House владельцем которого являлся Person.

    {
    "content": [
    {
    "uuid": "ea00be28-1ce2-48e4-b4b3-c0e98c258201",
    "name": "Иван",
    "surname": "Ананьев",
    "sex": "MALE",
    "passportSeries": "MP",
    "passportNumber": "2346376",
    "createDate": "2022-06-29T06:11:15.156",
    "updateDate": "2022-06-29T06:11:15.156"
    }
    ],
    "pageable": {
    "pageNumber": 0,
    "pageSize": 15,
    "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 1,
    "size": 15,
    "number": 0,
    "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
    },
    "first": true,
    "numberOfElements": 1,
    "empty": false
    }

##### Пример getHousesByTenantUuid Houses:

Отправляем GET запрос по ссылке с параметром UUID объекта Person.

Например: http://localhost:8080/houses/8126ee0f-edad-41fc-b845-41061439c652/owners
Также можно указать дополнительные параметры page, size, sort

Получаем результатом ответ от сервера список House из истории в которых проживал Person.

    {
    "content": [
    {
    "uuid": "a85a721b-c62b-428e-a777-3daa72fc5e3a",
    "country": "Беларусь",
    "area": "Минская область",
    "city": "Минск",
    "street": "проспект Рокоссовского",
    "number": "57",
    "createDate": "2023-08-29T06:12:15.156"
    }
    ],
    "pageable": {
    "pageNumber": 0,
    "pageSize": 15,
    "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 1,
    "size": 15,
    "number": 0,
    "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
    },
    "first": true,
    "numberOfElements": 1,
    "empty": false
    }

##### Пример getHousesByOwnerUuid Houses:

Отправляем GET запрос по ссылке с параметром UUID объекта Person.

Например: http://localhost:8080/houses/8126ee0f-edad-41fc-b845-41061439c652/owners
Также можно указать дополнительные параметры page, size, sort

Получаем результатом ответ от сервера список House из истории в которыми владел Person.

    {
    "content": [
    {
    "uuid": "8126ee0f-edad-41fc-b845-41061439c652",
    "country": "Беларусь",
    "area": "Минская область",
    "city": "Минск",
    "street": "переулок Водопроводный",
    "number": "117",
    "createDate": "2024-01-03T09:12:15.156"
    },
    {
    "uuid": "8ca3955d-b436-471d-872f-f2ce07ac3f15",
    "country": "Беларусь",
    "area": "Витебская область",
    "city": "Витебск",
    "street": "Чкалова",
    "number": "11",
    "createDate": "2024-01-03T09:12:15.156"
    }
    ],
    "pageable": {
    "pageNumber": 0,
    "pageSize": 15,
    "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 2,
    "size": 15,
    "number": 0,
    "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
    },
    "first": true,
    "numberOfElements": 2,
    "empty": false
    }
