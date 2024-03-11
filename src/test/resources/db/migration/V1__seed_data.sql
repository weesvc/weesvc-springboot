CREATE TABLE places
(
    id          SERIAL PRIMARY KEY,
    name        TEXT UNIQUE NOT NULL,
    description TEXT,
    latitude    REAL,
    longitude   REAL,
    created_at  TIMESTAMP      NOT NULL,
    updated_at  TIMESTAMP      NOT NULL
);

INSERT INTO places (name, description, latitude, longitude, created_at, updated_at)
VALUES
    ('Mount Rushmore', 'Mount Rushmore National Memorial, SD, USA', 43.88031, -103.45387, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Bellagio Fountains', 'Fountains of Bellagio, NV, USA', 36.11274, -115.17430, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('MCO', 'Orlando International Airport, FL, USA', 28.42461, -81.31075, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Hoover Dam', 'Hoover Dam, Nevada, USA', 36.01604, -114.73783, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Red Rocks', 'Red Rocks Park and Amphitheatre, CO, USA', 39.66551, -105.20531, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('MIA', 'Miami International Airport, FL, USA',	25.79516, -80.27959, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Unknown', '',	0.00000, 0.00000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Grand Canyon', 'Grand Canyon National Park, AZ, USA', 36.26603, -112.36380, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Hollywood Studios', 'Disney''s Hollywood Studios, FL, USA', 28.35801, -81.55918, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('ORD', 'O''Hare International Airport, Chicago, IL, USA', 41.97861, -87.90472, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
