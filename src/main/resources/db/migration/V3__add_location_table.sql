CREATE TABLE location
(
    location_id       VARCHAR(255)     NOT NULL,
    formatted_address VARCHAR(500)     NOT NULL,
    latitude          DOUBLE PRECISION NOT NULL,
    longitude         DOUBLE PRECISION NOT NULL,
    town              VARCHAR(250),
    county            VARCHAR(250),
    CONSTRAINT pk_location PRIMARY KEY (location_id)
);

ALTER TABLE venue
    ADD location_id VARCHAR(255);

ALTER TABLE venue
    ALTER COLUMN location_id SET NOT NULL;

ALTER TABLE venue
    ADD CONSTRAINT uc_venue_location UNIQUE (location_id);

ALTER TABLE venue
    ADD CONSTRAINT FK_VENUE_ON_LOCATION FOREIGN KEY (location_id) REFERENCES location (location_id);

ALTER TABLE venue
DROP
COLUMN address;

ALTER TABLE venue
DROP
COLUMN county;

ALTER TABLE venue
DROP
COLUMN latitude;

ALTER TABLE venue
DROP
COLUMN longitude;

ALTER TABLE venue
DROP
COLUMN town;