CREATE DATABASE IF NOT EXISTS reservations_db;
USE reservations_db;

CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS localities (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    postal_code VARCHAR(10) UNIQUE,
    locality VARCHAR(60) UNIQUE
);

CREATE TABLE IF NOT EXISTS locations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    locality_id BIGINT,
    slug VARCHAR(60) UNIQUE,
    designation VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    website VARCHAR(255),
    phone VARCHAR(30),
    CONSTRAINT fk_location_locality FOREIGN KEY (locality_id) REFERENCES localities(id)
);

CREATE TABLE IF NOT EXISTS users (
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
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS artists (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    firstname VARCHAR(60),
    lastname VARCHAR(60)
);

CREATE TABLE IF NOT EXISTS artist_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(60) NOT NULL
);

CREATE TABLE IF NOT EXISTS artist_type (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    artist_id BIGINT NOT NULL,
    type_id BIGINT NOT NULL,
    CONSTRAINT fk_artist_type_artist FOREIGN KEY (artist_id) REFERENCES artists(id),
    CONSTRAINT fk_artist_type_type FOREIGN KEY (type_id) REFERENCES artist_types(id)
);

CREATE TABLE IF NOT EXISTS shows (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    location_id BIGINT,
    slug VARCHAR(60) UNIQUE,
    title VARCHAR(255) NOT NULL,
    poster_url VARCHAR(255),
    bookable BOOLEAN DEFAULT TRUE,
    price DECIMAL(10,2),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    producer_id BIGINT,
    CONSTRAINT fk_show_location FOREIGN KEY (location_id) REFERENCES locations(id),
    CONSTRAINT fk_show_producer FOREIGN KEY (producer_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS collaborations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    artist_type_id BIGINT NOT NULL,
    show_id BIGINT NOT NULL,
    CONSTRAINT fk_collab_artist_type FOREIGN KEY (artist_type_id) REFERENCES artist_type(id),
    CONSTRAINT fk_collab_show FOREIGN KEY (show_id) REFERENCES shows(id)
);

CREATE TABLE IF NOT EXISTS representations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    show_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    performance_date DATE NOT NULL,
    performance_time TIME NOT NULL,
    capacity INT DEFAULT 0,
    booked_seats INT DEFAULT 0,
    is_full BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_representation_show FOREIGN KEY (show_id) REFERENCES shows(id),
    CONSTRAINT fk_representation_location FOREIGN KEY (location_id) REFERENCES locations(id)
);

CREATE TABLE IF NOT EXISTS prices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    label VARCHAR(50),
    amount DECIMAL(10,2),
    representation_id BIGINT NOT NULL,
    CONSTRAINT fk_price_representation FOREIGN KEY (representation_id) REFERENCES representations(id)
);

CREATE TABLE IF NOT EXISTS reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_price DECIMAL(10,2),
    status VARCHAR(30),
    ticket_delivery_method VARCHAR(30),
    payment_method VARCHAR(30),
    payment_status VARCHAR(30),
    CONSTRAINT fk_reservation_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS representation_reservation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reservation_id BIGINT NOT NULL,
    representation_id BIGINT NOT NULL,
    price_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    CONSTRAINT fk_rr_reservation FOREIGN KEY (reservation_id) REFERENCES reservations(id),
    CONSTRAINT fk_rr_representation FOREIGN KEY (representation_id) REFERENCES representations(id),
    CONSTRAINT fk_rr_price FOREIGN KEY (price_id) REFERENCES prices(id)
);

CREATE TABLE IF NOT EXISTS reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    show_id BIGINT NOT NULL,
    rating INT,
    comment TEXT,
    review_type VARCHAR(30) DEFAULT 'COMMENT',
    source_url VARCHAR(500),
    is_published BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_review_show FOREIGN KEY (show_id) REFERENCES shows(id)
);

CREATE TABLE IF NOT EXISTS affiliate_plans (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    plan_name VARCHAR(50),
    api_limit INT,
    monthly_price DECIMAL(10,2)
);

CREATE TABLE IF NOT EXISTS api_keys (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    affiliate_plan_id BIGINT NOT NULL,
    api_key VARCHAR(255) UNIQUE,
    enabled BOOLEAN DEFAULT TRUE,
    api_usage_count INT DEFAULT 0,
    api_usage_period_start TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_api_key_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_api_key_plan FOREIGN KEY (affiliate_plan_id) REFERENCES affiliate_plans(id)
);

INSERT IGNORE INTO roles (id, role_name) VALUES
    (1, 'ADMIN'), (2, 'MEMBER'), (3, 'PRODUCER'), (4, 'CRITIC'), (5, 'AFFILIATE');

INSERT IGNORE INTO localities (id, postal_code, locality) VALUES
    (1, '1000', 'Bruxelles'), (2, '1050', 'Ixelles');

INSERT IGNORE INTO locations (id, locality_id, slug, designation, address, website, phone) VALUES
    (1, 1, 'theatre-royal', 'Theatre Royal', 'Rue de la Scene 1, 1000 Bruxelles', 'https://example.test/theatre-royal', '+32 2 555 01 01'),
    (2, 2, 'espace-ixelles', 'Espace Ixelles', 'Place Flagey 4, 1050 Ixelles', 'https://example.test/espace-ixelles', '+32 2 555 02 02');

INSERT IGNORE INTO shows (id, location_id, slug, title, poster_url, bookable, price, description) VALUES
    (1, 1, 'la-vie-en-scene', 'La vie en scene', NULL, TRUE, 22.50, 'Une comedie contemporaine autour des rencontres improvisees.'),
    (2, 2, 'les-voix-de-la-nuit', 'Les voix de la nuit', NULL, TRUE, 18.00, 'Une creation musicale intimiste pour tous les publics.'),
    (3, 1, 'le-dernier-acte', 'Le dernier acte', NULL, FALSE, 25.00, 'Un spectacle actuellement non reservable.');

INSERT IGNORE INTO representations (id, show_id, location_id, performance_date, performance_time, capacity, booked_seats, is_full) VALUES
    (1, 1, 1, '2030-04-10', '20:00:00', 120, 0, FALSE),
    (2, 1, 1, '2030-04-12', '20:00:00', 120, 0, FALSE),
    (3, 2, 2, '2030-04-15', '19:30:00', 80, 0, FALSE),
    (4, 3, 1, '2030-04-20', '20:30:00', 100, 0, FALSE);

INSERT IGNORE INTO prices (id, label, amount, representation_id) VALUES
    (1, 'Plein tarif', 22.50, 1), (2, 'Tarif reduit', 15.00, 1), (3, 'Etudiant', 10.00, 1),
    (4, 'Plein tarif', 22.50, 2), (5, 'Tarif reduit', 15.00, 2),
    (6, 'Plein tarif', 18.00, 3), (7, 'Etudiant', 10.00, 3),
    (8, 'Plein tarif', 25.00, 4);

INSERT IGNORE INTO artists (id, firstname, lastname) VALUES
    (1, 'Camille', 'Martin'), (2, 'Noah', 'Dupont');

INSERT IGNORE INTO artist_types (id, type_name) VALUES
    (1, 'Comedien'), (2, 'Metteur en scene');

INSERT IGNORE INTO artist_type (id, artist_id, type_id) VALUES
    (1, 1, 1), (2, 2, 2);

INSERT IGNORE INTO collaborations (id, artist_type_id, show_id) VALUES
    (1, 1, 1), (2, 2, 1), (3, 1, 2);

INSERT IGNORE INTO affiliate_plans (id, plan_name, api_limit, monthly_price) VALUES
    (1, 'FREE', 5, 0.00), (2, 'STARTER', 100, 9.99), (3, 'PREMIUM', 1000, 29.99);
