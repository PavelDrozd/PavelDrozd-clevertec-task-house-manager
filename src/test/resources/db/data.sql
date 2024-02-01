INSERT INTO houses ("uuid", country, area, city, street, "number", create_date)
VALUES ('ac602dde-9556-4ef5-954c-aeebc42c5056', 'Беларусь', 'Минская область', 'Минск', 'Разинская', '99', '2024-01-03T09:12:15.156'),
        ('061783b1-c63b-4fb2-a9d0-9d90842911a2', 'Беларусь', 'Минская область', 'Солигорск', 'К.Заслонова', '52', '2024-01-03T09:12:15.156'),
        ('a85a721b-c62b-428e-a777-3daa72fc5e3a', 'Беларусь', 'Минская область', 'Минск', 'проспект Рокоссовского', '57', '2023-08-29T06:12:15.156'),
        ('8126ee0f-edad-41fc-b845-41061439c652', 'Беларусь', 'Минская область', 'Минск', 'переулок Водопроводный', '117', '2024-01-03T09:12:15.156'),
        ('63f0df3a-b447-4d76-ba8f-638b81a99b07', 'Беларусь', 'Минская область', 'Минск', 'проспект Независимости', '91Б', '2024-01-03T09:12:15.156'),
        ('07b74899-51b7-4ad4-8098-ddd51efd209e', 'Беларусь', 'Гомельская область', 'Гомель', 'Бакунина', '141', '2022-06-29T06:11:15.156'),
        ('2634cbbb-93cb-4d24-b84f-1123a529f4f5', 'Беларусь', 'Брестская область', 'Брест', 'Суворова', '84', '2023-12-28T07:12:15.156'),
        ('b99d623b-4b7e-4c89-afce-2bed07ceb9fc', 'Беларусь', 'Гродненская область', 'Гродно', 'Пестрака', '38', '2024-01-03T09:12:15.156'),
        ('8ca3955d-b436-471d-872f-f2ce07ac3f15', 'Беларусь', 'Витебская область', 'Витебск', 'Чкалова', '11', '2024-01-03T09:12:15.156'),
        ('78cdcc8d-07df-496b-86aa-65aadd4cfc77', 'Беларусь', 'Могилевская область', 'Могилев', 'Петрозаводская', '3', '2023-09-29T06:05:15.156');

INSERT INTO persons ("uuid", "name", surname, sex, passport_series, passport_number, create_date, update_date, house_id)
VALUES ('79e17dfd-a27d-45b6-8c72-a15538b8216e', 'Мария', 'Лазарева', 'FEMALE', 'MP', '7332632', '2018-08-29T06:12:15.156', '2024-01-03T09:12:15.156', (SELECT id FROM houses WHERE uuid = 'ac602dde-9556-4ef5-954c-aeebc42c5056')),
		('03736b7f-3ca4-4af7-99ac-07628a7d8fe6', 'София', 'Макарова', 'FEMALE', 'MC', '6373235', '2022-11-11T06:12:15.156', '2022-11-11T06:12:15.156', (SELECT id FROM houses WHERE uuid = '061783b1-c63b-4fb2-a9d0-9d90842911a2')),
		('12ff37bc-3fc8-47cd-8b18-d0b3fb35597b', 'Герман', 'Куликов', 'MALE', 'MA', '2634736', '2021-01-21T06:12:15.156', '2021-01-21T06:12:15.156', (SELECT id FROM houses WHERE uuid = 'a85a721b-c62b-428e-a777-3daa72fc5e3a')),
		('db34378e-ab65-40bb-9030-90fac73e195f', 'Вячеслав', 'Савицкий', 'MALE', 'MO', '1236347', '2023-08-29T06:12:15.156', '2023-08-29T06:12:15.156', (SELECT id FROM houses WHERE uuid = '8126ee0f-edad-41fc-b845-41061439c652')),
		('05242f3b-5e92-4ce4-a509-60c475dce50f', 'Софья', 'Муравьева', 'FEMALE', 'MC', '9737347', '2024-01-03T09:12:15.156', '2024-01-03T09:12:15.156', (SELECT id FROM houses WHERE uuid = '63f0df3a-b447-4d76-ba8f-638b81a99b07')),
		('4e5a4980-8e74-48de-9f1c-8681012203f5', 'Анастасия', 'Шарова', 'FEMALE', 'MC', '7964583', '2023-02-26T23:12:15.156', '2023-02-26T23:12:15.156', (SELECT id FROM houses WHERE uuid = '07b74899-51b7-4ad4-8098-ddd51efd209e')),
		('524fa37c-f2ed-4e08-b072-910708095e97', 'Артём', 'Михайлов', 'MALE', 'MO', '4965633', '2022-06-29T06:06:15.156', '2023-02-26T23:12:15.156', (SELECT id FROM houses WHERE uuid = '2634cbbb-93cb-4d24-b84f-1123a529f4f5')),
		('789c8c63-a58d-4e50-a4b4-33c15debfaf3', 'Андрей', 'Румянцев', 'MALE', 'MP', '9474383', '2023-09-29T06:05:15.156', '2023-09-29T06:05:15.156', (SELECT id FROM houses WHERE uuid = 'b99d623b-4b7e-4c89-afce-2bed07ceb9fc')),
		('ea00be28-1ce2-48e4-b4b3-c0e98c258201', 'Иван', 'Ананьев', 'MALE', 'MP', '2346376', '2022-06-29T06:11:15.156', '2022-06-29T06:11:15.156', (SELECT id FROM houses WHERE uuid = '8ca3955d-b436-471d-872f-f2ce07ac3f15')),
		('fd839347-a17c-44f0-a8b7-77b53d8a652d', 'Полина', 'Леонова', 'FEMALE', 'MC', '1593875', '2023-12-28T07:12:15.156', '2023-12-28T07:12:15.156', (SELECT id FROM houses WHERE uuid = '8ca3955d-b436-471d-872f-f2ce07ac3f15'));

INSERT INTO owner_house (person_id, house_id)
VALUES ((SELECT id FROM persons WHERE uuid = '03736b7f-3ca4-4af7-99ac-07628a7d8fe6'), (SELECT id FROM houses WHERE uuid = 'a85a721b-c62b-428e-a777-3daa72fc5e3a')),
		((SELECT id FROM persons WHERE uuid = '03736b7f-3ca4-4af7-99ac-07628a7d8fe6'), (SELECT id FROM houses WHERE uuid = '061783b1-c63b-4fb2-a9d0-9d90842911a2')),
		((SELECT id FROM persons WHERE uuid = '03736b7f-3ca4-4af7-99ac-07628a7d8fe6'), (SELECT id FROM houses WHERE uuid = 'ac602dde-9556-4ef5-954c-aeebc42c5056')),
		((SELECT id FROM persons WHERE uuid = '05242f3b-5e92-4ce4-a509-60c475dce50f'), (SELECT id FROM houses WHERE uuid = '63f0df3a-b447-4d76-ba8f-638b81a99b07')),
		((SELECT id FROM persons WHERE uuid = '524fa37c-f2ed-4e08-b072-910708095e97'), (SELECT id FROM houses WHERE uuid = '2634cbbb-93cb-4d24-b84f-1123a529f4f5')),
		((SELECT id FROM persons WHERE uuid = 'ea00be28-1ce2-48e4-b4b3-c0e98c258201'), (SELECT id FROM houses WHERE uuid = '8126ee0f-edad-41fc-b845-41061439c652')),
		((SELECT id FROM persons WHERE uuid = 'ea00be28-1ce2-48e4-b4b3-c0e98c258201'), (SELECT id FROM houses WHERE uuid = '8ca3955d-b436-471d-872f-f2ce07ac3f15')),
		((SELECT id FROM persons WHERE uuid = 'fd839347-a17c-44f0-a8b7-77b53d8a652d'), (SELECT id FROM houses WHERE uuid = '8ca3955d-b436-471d-872f-f2ce07ac3f15'));
