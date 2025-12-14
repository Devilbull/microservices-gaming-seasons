CREATE TABLE notification_log (
                                  id BIGSERIAL PRIMARY KEY,
                                  email VARCHAR(255) NOT NULL,
                                  subject VARCHAR(255) NOT NULL,
                                  message VARCHAR(2000),
                                  sent_at TIMESTAMP NOT NULL
);
