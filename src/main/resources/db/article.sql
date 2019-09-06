CREATE TABLE article (
  article_id SERIAL NOT NULL,
  title VARCHAR(200) NOT NULL,
  category VARCHAR(200) NOT NULL
);

INSERT INTO article(title, category) VALUES ('Java Concurrency', 'Java');
INSERT INTO article(title, category) VALUES ('Spring Boot Getting Started', 'Spring Boot');