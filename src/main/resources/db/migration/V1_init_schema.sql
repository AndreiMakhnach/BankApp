CREATE TABLE "USER" (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(500),
                        date_of_birth DATE,
                        password VARCHAR(500)
);

CREATE TABLE ACCOUNT (
                         id BIGSERIAL PRIMARY KEY,
                         user_id BIGINT NOT NULL,
                         balance DECIMAL NOT NULL,
                         initial_balance DECIMAL NOT NULL,
                         CONSTRAINT fk_account_user FOREIGN KEY (user_id) REFERENCES "USER" (id)
);

CREATE TABLE EMAIL_DATA (
                            id BIGSERIAL PRIMARY KEY,
                            user_id BIGINT NOT NULL,
                            email VARCHAR(200) NOT NULL UNIQUE,
                            CONSTRAINT fk_email_user FOREIGN KEY (user_id) REFERENCES "USER" (id)
);

CREATE TABLE PHONE_DATA (
                            id BIGSERIAL PRIMARY KEY,
                            user_id BIGINT NOT NULL,
                            phone VARCHAR(13) NOT NULL UNIQUE,
                            CONSTRAINT fk_phone_user FOREIGN KEY (user_id) REFERENCES "USER" (id)
);
