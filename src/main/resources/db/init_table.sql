-- Drop tables in the correct order
drop table if exists seat cascade;
drop table if exists reservation cascade;
drop table if exists schedule cascade;
drop table if exists concert cascade;
drop table if exists member cascade;

-- Create tables
CREATE TABLE member (
                        id BINARY(16) NOT NULL,
                        member_name VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        point DOUBLE NOT NULL,
                        role VARCHAR(255) NOT NULL,
                        PRIMARY KEY (id)
);

CREATE TABLE concert (
                         id BINARY(16) NOT NULL,
                         artist VARCHAR(255) NOT NULL,
                         concert_name VARCHAR(255) NOT NULL,
                         ticket_price DOUBLE NOT NULL,
                         PRIMARY KEY (id)
);

CREATE TABLE schedule (
                          id BINARY(16) NOT NULL,
                          concert_date TIMESTAMP NOT NULL,
                          seat_count INT NOT NULL,
                          extra_seat_count INT NOT NULL,
                          concert_id BINARY(16),
                          PRIMARY KEY (id),
                          FOREIGN KEY (concert_id) REFERENCES concert(id)
);

CREATE TABLE reservation (
                             id BINARY(16) NOT NULL,
                             member_id BINARY(16),
                             schedule_id BINARY(16),
                             reservation_date TIMESTAMP NOT NULL,
                             pay_status BOOLEAN NOT NULL,
                             cancel_status BOOLEAN NOT NULL,
                             version BIGINT,
                             PRIMARY KEY (id),
                             FOREIGN KEY (member_id) REFERENCES member(id),
                             FOREIGN KEY (schedule_id) REFERENCES schedule(id)
);

CREATE TABLE seat (
                      id BINARY(16) NOT NULL,
                      seat_num INT NOT NULL,
                      is_booked BOOLEAN NOT NULL,
                      schedule_id BINARY(16),
                      reservation_id BINARY(16),
                      version BIGINT,
                      PRIMARY KEY (id),
                      FOREIGN KEY (schedule_id) REFERENCES schedule(id),
                      FOREIGN KEY (reservation_id) REFERENCES reservation(id)
);


