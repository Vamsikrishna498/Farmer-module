-- Create table for farmer registration
CREATE TABLE farmer_registration (
    farmer_id SERIAL PRIMARY KEY,                                -- Unique ID for each farmer
    first_name VARCHAR(100) NOT NULL,                             -- Farmer's first name
    last_name VARCHAR(100) NOT NULL,                              -- Farmer's last name
    dob DATE NOT NULL,                                            -- Date of birth
    gender VARCHAR(10) CHECK (gender IN ('Male', 'Female', 'Transgender', 'Prefer not to say')) NOT NULL, -- Gender with validation
    email VARCHAR(100) UNIQUE NOT NULL,                           -- Unique email address
    phone_number VARCHAR(15) UNIQUE,                              -- Unique phone number
    country VARCHAR(50) NOT NULL,                                 -- Country of the farmer
    state VARCHAR(50) NOT NULL,                                   -- State of the farmer
    pin_code VARCHAR(10) NOT NULL,                                -- Pin code for the address
    time_zone VARCHAR(50),                                        -- Time zone
    nationality VARCHAR(50),                                      -- Nationality
    alternative_number VARCHAR(15),                               -- Alternative contact number
    father_name VARCHAR(100),                                     -- Father's name
    father_relation VARCHAR(50),                                  -- Relationship to the farmer (Father, Relative, etc.)
    address TEXT NOT NULL,                                         -- Full address
    profession VARCHAR(100),                                      -- Profession of the farmer
    current_crop VARCHAR(100),                                    -- Current crop grown
    total_land_holding DECIMAL(10, 2) CHECK (total_land_holding >= 0), -- Total land holding in acres
    irrigation_details TEXT,                                       -- Details of irrigation
    bank_name VARCHAR(100),                                        -- Bank name
    account_number VARCHAR(20),                                    -- Bank account number
    pan_number VARCHAR(20),                                        -- PAN number
    aadhar_number VARCHAR(20),                                     -- Aadhar number
    geo_tag VARCHAR(100),                                          -- GPS coordinates (latitude, longitude)
    soil_test_certificate BYTEA,                                  -- Binary data for soil test certificate
    experience INT CHECK (experience >= 0),                        -- Years of farming experience
    proposed_crop VARCHAR(100),                                    -- Crop that is proposed to be grown
    irrigation_method VARCHAR(50),                                 -- Type of irrigation used
    profile_photo BYTEA,                                           -- Binary data for farmer's profile photo
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,               -- Timestamp for record creation
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,               -- Timestamp for record update
    CONSTRAINT chk_dob CHECK (dob <= CURRENT_DATE),               -- Ensures the date of birth is in the past
    CONSTRAINT chk_pin_code CHECK (pin_code ~ '^\d{6}$')          -- Ensures the pin code is exactly 6 digits
);

-- Indexes for optimization
CREATE INDEX idx_farmer_email ON farmer_registration (email);
CREATE INDEX idx_farmer_phone ON farmer_registration (phone_number);
CREATE INDEX idx_farmer_state ON farmer_registration (state);
CREATE INDEX idx_farmer_country ON farmer_registration (country);

-- Example of inserting a farmer record
INSERT INTO farmer_registration (
    first_name, last_name, dob, gender, email, phone_number, country, state, pin_code, 
    time_zone, nationality, alternative_number, father_name, father_relation, address, 
    profession, current_crop, total_land_holding, irrigation_details, bank_name, account_number, 
    pan_number, aadhar_number, geo_tag, soil_test_certificate, experience, proposed_crop, 
    irrigation_method, profile_photo
) VALUES (
    'Krishna', 'Kumar', '1980-12-12', 'Male', 'krishna@example.com', '9876543210', 'India', 'Telangana', 
    '500062', 'GMT+5:30', 'Indian', '9876543211', 'Krishna Kumar', 'Father', '123 Main Street, Hyderabad',
    'Farmer', 'Rice', 5.4, 'Drip irrigation', 'HDFC Bank', '1234567890', 'ABCDE1234F', '987654321098', 
    '17.385044,78.486671', NULL, 15, 'Cotton', 'Drip', NULL
);

-- Trigger for auto-updating 'updated_at' field whenever a record is updated
CREATE OR REPLACE FUNCTION update_updated_at() 
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_updated_at
BEFORE UPDATE ON farmer_registration
FOR EACH ROW EXECUTE FUNCTION update_updated_at();

-- Ensure proper data integrity with foreign key constraints (if applicable)
-- E.g., linking to another table such as `crop_information`
-- FOREIGN KEY (crop_id) REFERENCES crop_information(crop_id);

-- Example table for crop information (if applicable)
CREATE TABLE crop_information (
    crop_id SERIAL PRIMARY KEY,
    crop_name VARCHAR(100) NOT NULL,
    crop_type VARCHAR(50) NOT NULL
);

-- Ensure the crop name is unique (if necessary)
CREATE UNIQUE INDEX idx_crop_name ON crop_information (crop_name);
