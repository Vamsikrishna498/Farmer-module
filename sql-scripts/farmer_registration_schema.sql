CREATE TABLE Farmers (
    farmer_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    dob DATE,
    gender VARCHAR(10),
    phone_number VARCHAR(15),
    address TEXT
);

CREATE TABLE Crops (
    crop_id SERIAL PRIMARY KEY,
    farmer_id INT REFERENCES Farmers(farmer_id),
    crop_name VARCHAR(50),
    season VARCHAR(20),
    area_covered FLOAT
);

CREATE TABLE Bank_Details (
    bank_id SERIAL PRIMARY KEY,
    farmer_id INT REFERENCES Farmers(farmer_id),
    bank_name VARCHAR(100),
    account_number VARCHAR(20),
    ifsc_code VARCHAR(15)
);

CREATE TABLE Land_Holdings (
    land_id SERIAL PRIMARY KEY,
    farmer_id INT REFERENCES Farmers(farmer_id),
    land_area FLOAT,
    land_address TEXT,
    ownership_status VARCHAR(50)
);

CREATE TABLE Irrigation_Details (
    irrigation_id SERIAL PRIMARY KEY,
    farmer_id INT REFERENCES Farmers(farmer_id),
    irrigation_type VARCHAR(50),
    water_source VARCHAR(100)
);

CREATE TABLE Documents (
    document_id SERIAL PRIMARY KEY,
    farmer_id INT REFERENCES Farmers(farmer_id),
    document_type VARCHAR(50),
    document_path VARCHAR(255)
);
