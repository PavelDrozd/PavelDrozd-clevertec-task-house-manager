INSERT INTO owner_house (person_id, house_id)
VALUES ((SELECT id FROM persons WHERE uuid = '03736b7f-3ca4-4af7-99ac-07628a7d8fe6'), (SELECT id FROM houses WHERE uuid = 'a85a721b-c62b-428e-a777-3daa72fc5e3a')),
		((SELECT id FROM persons WHERE uuid = '03736b7f-3ca4-4af7-99ac-07628a7d8fe6'), (SELECT id FROM houses WHERE uuid = '061783b1-c63b-4fb2-a9d0-9d90842911a2')),
		((SELECT id FROM persons WHERE uuid = '03736b7f-3ca4-4af7-99ac-07628a7d8fe6'), (SELECT id FROM houses WHERE uuid = 'ac602dde-9556-4ef5-954c-aeebc42c5056')),
		((SELECT id FROM persons WHERE uuid = '05242f3b-5e92-4ce4-a509-60c475dce50f'), (SELECT id FROM houses WHERE uuid = '63f0df3a-b447-4d76-ba8f-638b81a99b07')),
		((SELECT id FROM persons WHERE uuid = '524fa37c-f2ed-4e08-b072-910708095e97'), (SELECT id FROM houses WHERE uuid = '2634cbbb-93cb-4d24-b84f-1123a529f4f5')),
		((SELECT id FROM persons WHERE uuid = 'ea00be28-1ce2-48e4-b4b3-c0e98c258201'), (SELECT id FROM houses WHERE uuid = '8126ee0f-edad-41fc-b845-41061439c652')),
		((SELECT id FROM persons WHERE uuid = 'ea00be28-1ce2-48e4-b4b3-c0e98c258201'), (SELECT id FROM houses WHERE uuid = '8ca3955d-b436-471d-872f-f2ce07ac3f15')),
		((SELECT id FROM persons WHERE uuid = 'fd839347-a17c-44f0-a8b7-77b53d8a652d'), (SELECT id FROM houses WHERE uuid = '8ca3955d-b436-471d-872f-f2ce07ac3f15'));