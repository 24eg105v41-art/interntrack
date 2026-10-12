-- InternTrack database schema
-- The Spring Boot backend creates/updates these tables automatically
-- (spring.jpa.hibernate.ddl-auto=update). This script is provided as a
-- reference and for manual/bootstrap setup.

CREATE DATABASE IF NOT EXISTS interntrack_db
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE interntrack_db;

CREATE TABLE IF NOT EXISTS users (
  user_id         BIGINT       NOT NULL AUTO_INCREMENT,
  name            VARCHAR(100) NOT NULL,
  email           VARCHAR(150) NOT NULL,
  password        VARCHAR(255) NOT NULL,
  role            VARCHAR(20)  NOT NULL DEFAULT 'STUDENT',
  phone           VARCHAR(20)  NULL,
  college         VARCHAR(150) NULL,
  branch          VARCHAR(80)  NULL,
  graduation_year INT          NULL,
  cgpa            DOUBLE       NULL,
  skills          VARCHAR(300) NULL,
  created_at      DATETIME     NULL,
  PRIMARY KEY (user_id),
  UNIQUE KEY uk_users_email (email)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS internships (
  internship_id    BIGINT       NOT NULL AUTO_INCREMENT,
  user_id          BIGINT       NOT NULL,
  company_name     VARCHAR(150) NOT NULL,
  role_title       VARCHAR(150) NOT NULL,
  location         VARCHAR(120) NULL,
  work_mode        VARCHAR(20)  NOT NULL DEFAULT 'ONSITE',
  stipend          INT          NOT NULL DEFAULT 0,
  application_date DATE         NOT NULL,
  deadline         DATE         NOT NULL,
  status           VARCHAR(20)  NOT NULL DEFAULT 'APPLIED',
  source_link      VARCHAR(300) NULL,
  notes            VARCHAR(1000) NULL,
  created_at       DATETIME     NULL,
  updated_at       DATETIME     NULL,
  PRIMARY KEY (internship_id),
  KEY idx_internships_user (user_id),
  CONSTRAINT fk_internships_user FOREIGN KEY (user_id) REFERENCES users (user_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS offers (
  offer_id          BIGINT       NOT NULL AUTO_INCREMENT,
  user_id           BIGINT       NOT NULL,
  internship_id     BIGINT       NULL,
  company_name      VARCHAR(150) NOT NULL,
  role_title        VARCHAR(150) NOT NULL,
  stipend           INT          NOT NULL DEFAULT 0,
  offer_date        DATE         NULL,
  joining_date      DATE         NULL,
  response_deadline DATE         NULL,
  status            VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
  letter_link       VARCHAR(300) NULL,
  notes             VARCHAR(1000) NULL,
  created_at        DATETIME     NULL,
  updated_at        DATETIME     NULL,
  PRIMARY KEY (offer_id),
  KEY idx_offers_user (user_id),
  KEY idx_offers_internship (internship_id),
  CONSTRAINT fk_offers_user FOREIGN KEY (user_id) REFERENCES users (user_id),
  CONSTRAINT fk_offers_internship FOREIGN KEY (internship_id) REFERENCES internships (internship_id)
) ENGINE=InnoDB;
