INSERT INTO "USER" (id, name, date_of_birth, password) VALUES
                                                           (1, 'Райан Гослинг', '1990-01-01', '$2a$10$BQs2tXuFKW8gUz7Gos/2LuUdWAVP7C6Ww7oSdtDw7g59r.wJlH4yO'),
                                                           (2, 'Геральт Ведьмачьев', '1992-02-02', '$2a$10$MHfErO85QjlJEfpzmqUrV.g1PeVrKdmJ0XbH4IY9Rql6rHvXcIFna');

INSERT INTO ACCOUNT (user_id, balance, initial_balance) VALUES
                                                            (1, 1000.00, 1000.00),
                                                            (2, 500.00, 500.00);

INSERT INTO EMAIL_DATA (user_id, email) VALUES
                                            (1, 'ryam@example.com'),
                                            (1, 'ryan.alt@example.com'),
                                            (2, 'geralt@example.com');

INSERT INTO PHONE_DATA (user_id, phone) VALUES
                                            (1, '79201234567'),
                                            (2, '79207654321');
