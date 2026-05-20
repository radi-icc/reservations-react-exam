CREATE DATABASE reservations_db;

USE reservations_db;

CREATE TABLE roles (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       role_name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       firstname VARCHAR(60),
                       lastname VARCHAR(60),
                       language VARCHAR(20),
                       enabled BOOLEAN DEFAULT TRUE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       role_id BIGINT,

                       CONSTRAINT fk_user_role
                           FOREIGN KEY (role_id)
                               REFERENCES roles(id)
);

CREATE TABLE localities (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            postal_code VARCHAR(10) UNIQUE,
                            locality VARCHAR(60) UNIQUE
);

CREATE TABLE locations (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           locality_id BIGINT,
                           slug VARCHAR(60) UNIQUE,
                           designation VARCHAR(100) NOT NULL,
                           address VARCHAR(255),
                           website VARCHAR(255),
                           phone VARCHAR(30),

                           CONSTRAINT fk_location_locality
                               FOREIGN KEY (locality_id)
                                   REFERENCES localities(id)
);

CREATE TABLE artists (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         firstname VARCHAR(60),
                         lastname VARCHAR(60)
);

CREATE TABLE artist_types (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              type_name VARCHAR(60) NOT NULL
);

CREATE TABLE artist_type (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             artist_id BIGINT NOT NULL,
                             type_id BIGINT NOT NULL,

                             CONSTRAINT fk_artist_type_artist
                                 FOREIGN KEY (artist_id)
                                     REFERENCES artists(id),

                             CONSTRAINT fk_artist_type_type
                                 FOREIGN KEY (type_id)
                                     REFERENCES artist_types(id)
);


CREATE TABLE shows (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       location_id BIGINT,
                       slug VARCHAR(60) UNIQUE,
                       title VARCHAR(255) NOT NULL,
                       poster_url VARCHAR(255),
                       bookable BOOLEAN DEFAULT TRUE,
                       price DECIMAL(10,2),
                       description TEXT,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT fk_show_location
                           FOREIGN KEY (location_id)
                               REFERENCES locations(id)
);

CREATE TABLE collaborations (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                artist_type_id BIGINT NOT NULL,
                                show_id BIGINT NOT NULL,

                                CONSTRAINT fk_collab_artist_type
                                    FOREIGN KEY (artist_type_id)
                                        REFERENCES artist_type(id),

                                CONSTRAINT fk_collab_show
                                    FOREIGN KEY (show_id)
                                        REFERENCES shows(id)
);


CREATE TABLE representations (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 show_id BIGINT NOT NULL,
                                 location_id BIGINT NOT NULL,
                                 performance_date DATE NOT NULL,
                                 performance_time TIME NOT NULL,
                                 capacity INT DEFAULT 0,
                                 booked_seats INT DEFAULT 0,
                                 is_full BOOLEAN DEFAULT FALSE,

                                 CONSTRAINT fk_representation_show
                                     FOREIGN KEY (show_id)
                                         REFERENCES shows(id),

                                 CONSTRAINT fk_representation_location
                                     FOREIGN KEY (location_id)
                                         REFERENCES locations(id)
);

CREATE TABLE prices (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        label VARCHAR(50),
                        amount DECIMAL(10,2)
);

CREATE TABLE reservations (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              user_id BIGINT NOT NULL,
                              reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              total_price DECIMAL(10,2),
                              status VARCHAR(30),

                              CONSTRAINT fk_reservation_user
                                  FOREIGN KEY (user_id)
                                      REFERENCES users(id)
);

CREATE TABLE representation_reservation (
                                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                            reservation_id BIGINT NOT NULL,
                                            representation_id BIGINT NOT NULL,
                                            price_id BIGINT NOT NULL,
                                            quantity INT NOT NULL,

                                            CONSTRAINT fk_rr_reservation
                                                FOREIGN KEY (reservation_id)
                                                    REFERENCES reservations(id),

                                            CONSTRAINT fk_rr_representation
                                                FOREIGN KEY (representation_id)
                                                    REFERENCES representations(id),

                                            CONSTRAINT fk_rr_price
                                                FOREIGN KEY (price_id)
                                                    REFERENCES prices(id)
);


CREATE TABLE reviews (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         user_id BIGINT NOT NULL,
                         show_id BIGINT NOT NULL,
                         rating INT,
                         comment TEXT,
                         is_published BOOLEAN DEFAULT FALSE,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                         CONSTRAINT fk_review_user
                             FOREIGN KEY (user_id)
                                 REFERENCES users(id),

                         CONSTRAINT fk_review_show
                             FOREIGN KEY (show_id)
                                 REFERENCES shows(id)
);


CREATE TABLE affiliate_plans (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 plan_name VARCHAR(50),
                                 api_limit INT,
                                 monthly_price DECIMAL(10,2)
);

CREATE TABLE api_keys (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          user_id BIGINT NOT NULL,
                          affiliate_plan_id BIGINT NOT NULL,
                          api_key VARCHAR(255) UNIQUE,
                          enabled BOOLEAN DEFAULT TRUE,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT fk_api_key_user
                              FOREIGN KEY (user_id)
                                  REFERENCES users(id),

                          CONSTRAINT fk_api_key_plan
                              FOREIGN KEY (affiliate_plan_id)
                                  REFERENCES affiliate_plans(id)
);

