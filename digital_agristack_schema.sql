--
-- PostgreSQL database dump
--

-- Dumped from database version 17.2
-- Dumped by pg_dump version 17.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: relation_type; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.relation_type AS ENUM (
    'D/O',
    'S/O',
    'W/O'
);


ALTER TYPE public.relation_type OWNER TO postgres;

--
-- Name: register_farmer(character varying, character varying, date, character varying, character varying, character varying, character varying, character varying, character varying, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.register_farmer(fname character varying, lname character varying, dob date, gender character varying, country character varying, state character varying, pin character varying, email character varying, phone character varying, password text) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    new_farmer_id INTEGER;
BEGIN
    -- Insert into farmer_registration
    INSERT INTO farmer_registration (first_name, last_name, date_of_birth, gender, country_region, state, pin_code, email_address, phone_number, password_hash)
    VALUES (fname, lname, dob, gender, country, state, pin, email, phone, crypt(password, gen_salt('bf')))
    RETURNING farmer_id INTO new_farmer_id;
    
    -- Insert into farmer_details
    INSERT INTO farmer_details (farmer_id, first_name, last_name, date_of_birth, gender, mobile_number, email_id, registration_date)
    VALUES (new_farmer_id, fname, lname, dob, gender, phone, email, CURRENT_TIMESTAMP);
    
    RETURN new_farmer_id;
END;
$$;


ALTER FUNCTION public.register_farmer(fname character varying, lname character varying, dob date, gender character varying, country character varying, state character varying, pin character varying, email character varying, phone character varying, password text) OWNER TO postgres;

--
-- Name: sync_farmer_details(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.sync_farmer_details() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    INSERT INTO farmer_details (farmer_id, first_name, last_name, date_of_birth, gender, mobile_number, email_id, state, pincode, registration_date)
    VALUES (NEW.farmer_id, NEW.first_name, NEW.last_name, NEW.date_of_birth, NEW.gender, NEW.phone_number, NEW.email_address, NEW.state, NEW.pin_code, NEW.created_at)
    ON CONFLICT (farmer_id) DO NOTHING;
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.sync_farmer_details() OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: blocks; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.blocks (
    block_id integer NOT NULL,
    block_name character varying(100) NOT NULL,
    district_id integer
);


ALTER TABLE public.blocks OWNER TO postgres;

--
-- Name: blocks_block_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.blocks_block_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.blocks_block_id_seq OWNER TO postgres;

--
-- Name: blocks_block_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.blocks_block_id_seq OWNED BY public.blocks.block_id;


--
-- Name: countries; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.countries (
    country_id integer NOT NULL,
    country_name character varying(100) NOT NULL
);


ALTER TABLE public.countries OWNER TO postgres;

--
-- Name: countries_country_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.countries_country_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.countries_country_id_seq OWNER TO postgres;

--
-- Name: countries_country_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.countries_country_id_seq OWNED BY public.countries.country_id;


--
-- Name: crop_types; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.crop_types (
    crop_id integer NOT NULL,
    crop_name character varying(50) NOT NULL
);


ALTER TABLE public.crop_types OWNER TO postgres;

--
-- Name: crop_types_crop_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.crop_types_crop_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.crop_types_crop_id_seq OWNER TO postgres;

--
-- Name: crop_types_crop_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.crop_types_crop_id_seq OWNED BY public.crop_types.crop_id;


--
-- Name: districts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.districts (
    district_id integer NOT NULL,
    district_name character varying(100) NOT NULL,
    state_id integer
);


ALTER TABLE public.districts OWNER TO postgres;

--
-- Name: districts_district_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.districts_district_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.districts_district_id_seq OWNER TO postgres;

--
-- Name: districts_district_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.districts_district_id_seq OWNED BY public.districts.district_id;


--
-- Name: education_levels; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.education_levels (
    education_id integer NOT NULL,
    education_level character varying(50) NOT NULL
);


ALTER TABLE public.education_levels OWNER TO postgres;

--
-- Name: education_levels_education_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.education_levels_education_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.education_levels_education_id_seq OWNER TO postgres;

--
-- Name: education_levels_education_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.education_levels_education_id_seq OWNED BY public.education_levels.education_id;


--
-- Name: farmer_address; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.farmer_address (
    address_id integer NOT NULL,
    farmer_id integer NOT NULL,
    country_id integer,
    state_id integer,
    district_id integer,
    block_id integer,
    village_id integer,
    zipcode character varying(10) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.farmer_address OWNER TO postgres;

--
-- Name: farmer_address_address_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.farmer_address_address_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.farmer_address_address_id_seq OWNER TO postgres;

--
-- Name: farmer_address_address_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.farmer_address_address_id_seq OWNED BY public.farmer_address.address_id;


--
-- Name: farmer_bank_details; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.farmer_bank_details (
    bank_id integer NOT NULL,
    farmer_id integer NOT NULL,
    bank_name character varying(100) NOT NULL,
    account_number bigint NOT NULL,
    branch_name character varying(100) NOT NULL,
    ifsc_code character varying(20) NOT NULL,
    passbook_file_path text NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.farmer_bank_details OWNER TO postgres;

--
-- Name: farmer_bank_details_bank_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.farmer_bank_details_bank_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.farmer_bank_details_bank_id_seq OWNER TO postgres;

--
-- Name: farmer_bank_details_bank_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.farmer_bank_details_bank_id_seq OWNED BY public.farmer_bank_details.bank_id;


--
-- Name: farmer_crop_info; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.farmer_crop_info (
    crop_info_id integer NOT NULL,
    farmer_id integer NOT NULL,
    farmer_photo_path character varying(255) NOT NULL,
    survey_number character varying(50) NOT NULL,
    total_land_holding_acres numeric(5,2),
    geo_tag character varying(100),
    crop_id integer,
    net_income character varying(20),
    soil_test boolean NOT NULL,
    soil_test_certificate_path character varying(255),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT farmer_crop_info_total_land_holding_acres_check CHECK ((total_land_holding_acres > (0)::numeric))
);


ALTER TABLE public.farmer_crop_info OWNER TO postgres;

--
-- Name: farmer_crop_info_crop_info_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.farmer_crop_info_crop_info_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.farmer_crop_info_crop_info_id_seq OWNER TO postgres;

--
-- Name: farmer_crop_info_crop_info_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.farmer_crop_info_crop_info_id_seq OWNED BY public.farmer_crop_info.crop_info_id;


--
-- Name: farmer_details; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.farmer_details (
    farmer_id integer NOT NULL,
    first_name character varying(100),
    last_name character varying(100),
    father_name character varying(100),
    date_of_birth date,
    gender character varying(10),
    mobile_number character varying(15),
    email_id character varying(100),
    address text,
    village character varying(100),
    taluk character varying(100),
    district character varying(100),
    state character varying(100),
    pincode character varying(10),
    land_area_acres numeric(10,2),
    farmer_category character varying(50),
    registration_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.farmer_details OWNER TO postgres;

--
-- Name: farmer_details_farmer_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.farmer_details_farmer_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.farmer_details_farmer_id_seq OWNER TO postgres;

--
-- Name: farmer_details_farmer_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.farmer_details_farmer_id_seq OWNED BY public.farmer_details.farmer_id;


--
-- Name: farmer_documents; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.farmer_documents (
    document_id integer NOT NULL,
    farmer_id integer NOT NULL,
    document_type character varying(50) NOT NULL,
    document_number character varying(100) NOT NULL,
    document_file_path text,
    uploaded_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT farmer_documents_document_type_check CHECK (((document_type)::text = ANY ((ARRAY['ID/Voter Card'::character varying, 'Aadhar Number'::character varying, 'PAN Number'::character varying, 'PPB Number'::character varying])::text[])))
);


ALTER TABLE public.farmer_documents OWNER TO postgres;

--
-- Name: farmer_documents_document_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.farmer_documents_document_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.farmer_documents_document_id_seq OWNER TO postgres;

--
-- Name: farmer_documents_document_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.farmer_documents_document_id_seq OWNED BY public.farmer_documents.document_id;


--
-- Name: farmer_family_contact_info; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.farmer_family_contact_info (
    family_contact_id integer NOT NULL,
    farmer_id integer NOT NULL,
    relation public.relation_type NOT NULL,
    father_name character varying(100) NOT NULL,
    alternative_number character varying(20) NOT NULL,
    alternative_number_type character varying(50) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.farmer_family_contact_info OWNER TO postgres;

--
-- Name: farmer_family_contact_info_family_contact_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.farmer_family_contact_info_family_contact_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.farmer_family_contact_info_family_contact_id_seq OWNER TO postgres;

--
-- Name: farmer_family_contact_info_family_contact_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.farmer_family_contact_info_family_contact_id_seq OWNED BY public.farmer_family_contact_info.family_contact_id;


--
-- Name: farmer_personal_info; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.farmer_personal_info (
    farmer_id integer NOT NULL,
    photo bytea,
    first_name character varying(50) NOT NULL,
    middle_name character varying(50),
    last_name character varying(50) NOT NULL,
    gender character varying(20) NOT NULL,
    nationality character varying(50) NOT NULL,
    date_of_birth date NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT farmer_personal_info_gender_check CHECK (((gender)::text = ANY ((ARRAY['Male'::character varying, 'Female'::character varying, 'Transgender'::character varying])::text[])))
);


ALTER TABLE public.farmer_personal_info OWNER TO postgres;

--
-- Name: farmer_personal_info_farmer_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.farmer_personal_info_farmer_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.farmer_personal_info_farmer_id_seq OWNER TO postgres;

--
-- Name: farmer_personal_info_farmer_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.farmer_personal_info_farmer_id_seq OWNED BY public.farmer_personal_info.farmer_id;


--
-- Name: farmer_professional_info; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.farmer_professional_info (
    professional_id integer NOT NULL,
    farmer_id integer NOT NULL,
    education_id integer,
    experience_years integer,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT farmer_professional_info_experience_years_check CHECK ((experience_years >= 0))
);


ALTER TABLE public.farmer_professional_info OWNER TO postgres;

--
-- Name: farmer_professional_info_professional_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.farmer_professional_info_professional_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.farmer_professional_info_professional_id_seq OWNER TO postgres;

--
-- Name: farmer_professional_info_professional_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.farmer_professional_info_professional_id_seq OWNED BY public.farmer_professional_info.professional_id;


--
-- Name: farmer_registration; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.farmer_registration (
    farmer_id integer NOT NULL,
    first_name character varying(50) NOT NULL,
    last_name character varying(50),
    date_of_birth date NOT NULL,
    gender character varying(20),
    country_region character varying(100) NOT NULL,
    state character varying(100) NOT NULL,
    pin_code character varying(10) NOT NULL,
    time_zone character varying(50),
    email_address character varying(100) NOT NULL,
    phone_number character varying(15) NOT NULL,
    password_hash text NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT farmer_registration_gender_check CHECK (((gender)::text = ANY ((ARRAY['Male'::character varying, 'Female'::character varying, 'Other'::character varying, 'Prefer not to say'::character varying])::text[])))
);


ALTER TABLE public.farmer_registration OWNER TO postgres;

--
-- Name: farmer_registration_farmer_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.farmer_registration_farmer_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.farmer_registration_farmer_id_seq OWNER TO postgres;

--
-- Name: farmer_registration_farmer_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.farmer_registration_farmer_id_seq OWNED BY public.farmer_registration.farmer_id;


--
-- Name: farmer_registration_partitioned; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.farmer_registration_partitioned (
    farmer_id integer NOT NULL,
    first_name character varying(50) NOT NULL,
    last_name character varying(50),
    date_of_birth date NOT NULL,
    gender character varying(20),
    country_region character varying(100) NOT NULL,
    state character varying(100) NOT NULL,
    pin_code character varying(10) NOT NULL,
    email_address character varying(100) NOT NULL,
    phone_number character varying(15) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT farmer_registration_state_chk CHECK ((state IS NOT NULL))
)
PARTITION BY LIST (state);


ALTER TABLE public.farmer_registration_partitioned OWNER TO postgres;

--
-- Name: farmer_registration_partitioned_farmer_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.farmer_registration_partitioned_farmer_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.farmer_registration_partitioned_farmer_id_seq OWNER TO postgres;

--
-- Name: farmer_registration_partitioned_farmer_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.farmer_registration_partitioned_farmer_id_seq OWNED BY public.farmer_registration_partitioned.farmer_id;


--
-- Name: farmer_registration_karnataka; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.farmer_registration_karnataka (
    farmer_id integer DEFAULT nextval('public.farmer_registration_partitioned_farmer_id_seq'::regclass) NOT NULL,
    first_name character varying(50) NOT NULL,
    last_name character varying(50),
    date_of_birth date NOT NULL,
    gender character varying(20),
    country_region character varying(100) NOT NULL,
    state character varying(100) NOT NULL,
    pin_code character varying(10) NOT NULL,
    email_address character varying(100) NOT NULL,
    phone_number character varying(15) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT farmer_registration_state_chk CHECK ((state IS NOT NULL))
);


ALTER TABLE public.farmer_registration_karnataka OWNER TO postgres;

--
-- Name: farmer_registration_tamilnadu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.farmer_registration_tamilnadu (
    farmer_id integer DEFAULT nextval('public.farmer_registration_partitioned_farmer_id_seq'::regclass) NOT NULL,
    first_name character varying(50) NOT NULL,
    last_name character varying(50),
    date_of_birth date NOT NULL,
    gender character varying(20),
    country_region character varying(100) NOT NULL,
    state character varying(100) NOT NULL,
    pin_code character varying(10) NOT NULL,
    email_address character varying(100) NOT NULL,
    phone_number character varying(15) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT farmer_registration_state_chk CHECK ((state IS NOT NULL))
);


ALTER TABLE public.farmer_registration_tamilnadu OWNER TO postgres;

--
-- Name: forgot_password_requests; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.forgot_password_requests (
    request_id integer NOT NULL,
    user_id integer NOT NULL,
    reset_token character varying(255) NOT NULL,
    email_phone_id character varying(100) NOT NULL,
    is_used boolean DEFAULT false,
    token_expiry timestamp without time zone NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.forgot_password_requests OWNER TO postgres;

--
-- Name: forgot_password_requests_request_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.forgot_password_requests_request_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.forgot_password_requests_request_id_seq OWNER TO postgres;

--
-- Name: forgot_password_requests_request_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.forgot_password_requests_request_id_seq OWNED BY public.forgot_password_requests.request_id;


--
-- Name: irrigation_details; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.irrigation_details (
    irrigation_id integer NOT NULL,
    farmer_id integer NOT NULL,
    crop_id integer,
    water_source_id integer,
    borewell_discharge_lph integer,
    summer_discharge_lph integer,
    borewell_location character varying(255),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.irrigation_details OWNER TO postgres;

--
-- Name: irrigation_details_irrigation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.irrigation_details_irrigation_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.irrigation_details_irrigation_id_seq OWNER TO postgres;

--
-- Name: irrigation_details_irrigation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.irrigation_details_irrigation_id_seq OWNED BY public.irrigation_details.irrigation_id;


--
-- Name: irrigation_sources; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.irrigation_sources (
    source_id integer NOT NULL,
    source_name character varying(50) NOT NULL
);


ALTER TABLE public.irrigation_sources OWNER TO postgres;

--
-- Name: irrigation_sources_source_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.irrigation_sources_source_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.irrigation_sources_source_id_seq OWNER TO postgres;

--
-- Name: irrigation_sources_source_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.irrigation_sources_source_id_seq OWNED BY public.irrigation_sources.source_id;


--
-- Name: password_reset; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.password_reset (
    reset_id integer NOT NULL,
    user_id integer,
    reset_token character varying(255) NOT NULL,
    token_expiry timestamp without time zone NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.password_reset OWNER TO postgres;

--
-- Name: password_reset_requests; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.password_reset_requests (
    reset_id integer NOT NULL,
    user_id integer,
    reset_token text NOT NULL,
    request_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    expiry_time timestamp without time zone,
    status character varying(50) DEFAULT 'Pending'::character varying
);


ALTER TABLE public.password_reset_requests OWNER TO postgres;

--
-- Name: password_reset_requests_reset_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.password_reset_requests_reset_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.password_reset_requests_reset_id_seq OWNER TO postgres;

--
-- Name: password_reset_requests_reset_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.password_reset_requests_reset_id_seq OWNED BY public.password_reset_requests.reset_id;


--
-- Name: password_reset_reset_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.password_reset_reset_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.password_reset_reset_id_seq OWNER TO postgres;

--
-- Name: password_reset_reset_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.password_reset_reset_id_seq OWNED BY public.password_reset.reset_id;


--
-- Name: portal_access; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.portal_access (
    access_id integer NOT NULL,
    farmer_id integer NOT NULL,
    role_designation character varying(50) NOT NULL,
    access_status character varying(20) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT portal_access_access_status_check CHECK (((access_status)::text = ANY ((ARRAY['Active'::character varying, 'Inactive'::character varying])::text[]))),
    CONSTRAINT portal_access_role_designation_check CHECK (((role_designation)::text = ANY ((ARRAY['Manager'::character varying, 'Employee'::character varying])::text[])))
);


ALTER TABLE public.portal_access OWNER TO postgres;

--
-- Name: portal_access_access_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.portal_access_access_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.portal_access_access_id_seq OWNER TO postgres;

--
-- Name: portal_access_access_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.portal_access_access_id_seq OWNED BY public.portal_access.access_id;


--
-- Name: proposed_crop_info; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.proposed_crop_info (
    proposed_crop_id integer NOT NULL,
    farmer_id integer NOT NULL,
    survey_number character varying(50) NOT NULL,
    total_land_holding_acres numeric(5,2),
    geo_tag character varying(100),
    net_income character varying(20),
    crop_id integer,
    soil_test boolean NOT NULL,
    soil_test_certificate_path character varying(255),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT proposed_crop_info_total_land_holding_acres_check CHECK ((total_land_holding_acres > (0)::numeric))
);


ALTER TABLE public.proposed_crop_info OWNER TO postgres;

--
-- Name: proposed_crop_info_proposed_crop_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.proposed_crop_info_proposed_crop_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.proposed_crop_info_proposed_crop_id_seq OWNER TO postgres;

--
-- Name: proposed_crop_info_proposed_crop_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.proposed_crop_info_proposed_crop_id_seq OWNED BY public.proposed_crop_info.proposed_crop_id;


--
-- Name: proposed_crop_irrigation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.proposed_crop_irrigation (
    irrigation_id integer NOT NULL,
    farmer_id integer NOT NULL,
    proposed_crop_id integer NOT NULL,
    water_source_id integer,
    borewell_discharge_lph integer,
    summer_discharge_lph integer,
    borewell_location character varying(255),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.proposed_crop_irrigation OWNER TO postgres;

--
-- Name: proposed_crop_irrigation_irrigation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.proposed_crop_irrigation_irrigation_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.proposed_crop_irrigation_irrigation_id_seq OWNER TO postgres;

--
-- Name: proposed_crop_irrigation_irrigation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.proposed_crop_irrigation_irrigation_id_seq OWNED BY public.proposed_crop_irrigation.irrigation_id;


--
-- Name: proposed_crop_types; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.proposed_crop_types (
    crop_id integer NOT NULL,
    crop_name character varying(50) NOT NULL
);


ALTER TABLE public.proposed_crop_types OWNER TO postgres;

--
-- Name: proposed_crop_types_crop_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.proposed_crop_types_crop_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.proposed_crop_types_crop_id_seq OWNER TO postgres;

--
-- Name: proposed_crop_types_crop_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.proposed_crop_types_crop_id_seq OWNED BY public.proposed_crop_types.crop_id;


--
-- Name: states; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.states (
    state_id integer NOT NULL,
    state_name character varying(100) NOT NULL,
    country_id integer
);


ALTER TABLE public.states OWNER TO postgres;

--
-- Name: states_state_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.states_state_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.states_state_id_seq OWNER TO postgres;

--
-- Name: states_state_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.states_state_id_seq OWNED BY public.states.state_id;


--
-- Name: user_login; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_login (
    user_id integer NOT NULL,
    login_identifier character varying(100) NOT NULL,
    password_hash text NOT NULL,
    is_active boolean DEFAULT true,
    last_login timestamp without time zone,
    login_attempts integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.user_login OWNER TO postgres;

--
-- Name: user_login_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_login_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.user_login_user_id_seq OWNER TO postgres;

--
-- Name: user_login_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.user_login_user_id_seq OWNED BY public.user_login.user_id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    user_id integer NOT NULL,
    email character varying(255) NOT NULL,
    phone_number character varying(15),
    password_hash text NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: users_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_user_id_seq OWNER TO postgres;

--
-- Name: users_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_user_id_seq OWNED BY public.users.user_id;


--
-- Name: villages; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.villages (
    village_id integer NOT NULL,
    village_name character varying(100) NOT NULL,
    block_id integer
);


ALTER TABLE public.villages OWNER TO postgres;

--
-- Name: villages_village_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.villages_village_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.villages_village_id_seq OWNER TO postgres;

--
-- Name: villages_village_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.villages_village_id_seq OWNED BY public.villages.village_id;


--
-- Name: farmer_registration_karnataka; Type: TABLE ATTACH; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_registration_partitioned ATTACH PARTITION public.farmer_registration_karnataka FOR VALUES IN ('Karnataka');


--
-- Name: farmer_registration_tamilnadu; Type: TABLE ATTACH; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_registration_partitioned ATTACH PARTITION public.farmer_registration_tamilnadu FOR VALUES IN ('Tamil Nadu');


--
-- Name: blocks block_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.blocks ALTER COLUMN block_id SET DEFAULT nextval('public.blocks_block_id_seq'::regclass);


--
-- Name: countries country_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.countries ALTER COLUMN country_id SET DEFAULT nextval('public.countries_country_id_seq'::regclass);


--
-- Name: crop_types crop_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.crop_types ALTER COLUMN crop_id SET DEFAULT nextval('public.crop_types_crop_id_seq'::regclass);


--
-- Name: districts district_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.districts ALTER COLUMN district_id SET DEFAULT nextval('public.districts_district_id_seq'::regclass);


--
-- Name: education_levels education_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.education_levels ALTER COLUMN education_id SET DEFAULT nextval('public.education_levels_education_id_seq'::regclass);


--
-- Name: farmer_address address_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_address ALTER COLUMN address_id SET DEFAULT nextval('public.farmer_address_address_id_seq'::regclass);


--
-- Name: farmer_bank_details bank_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_bank_details ALTER COLUMN bank_id SET DEFAULT nextval('public.farmer_bank_details_bank_id_seq'::regclass);


--
-- Name: farmer_crop_info crop_info_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_crop_info ALTER COLUMN crop_info_id SET DEFAULT nextval('public.farmer_crop_info_crop_info_id_seq'::regclass);


--
-- Name: farmer_details farmer_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_details ALTER COLUMN farmer_id SET DEFAULT nextval('public.farmer_details_farmer_id_seq'::regclass);


--
-- Name: farmer_documents document_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_documents ALTER COLUMN document_id SET DEFAULT nextval('public.farmer_documents_document_id_seq'::regclass);


--
-- Name: farmer_family_contact_info family_contact_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_family_contact_info ALTER COLUMN family_contact_id SET DEFAULT nextval('public.farmer_family_contact_info_family_contact_id_seq'::regclass);


--
-- Name: farmer_personal_info farmer_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_personal_info ALTER COLUMN farmer_id SET DEFAULT nextval('public.farmer_personal_info_farmer_id_seq'::regclass);


--
-- Name: farmer_professional_info professional_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_professional_info ALTER COLUMN professional_id SET DEFAULT nextval('public.farmer_professional_info_professional_id_seq'::regclass);


--
-- Name: farmer_registration farmer_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_registration ALTER COLUMN farmer_id SET DEFAULT nextval('public.farmer_registration_farmer_id_seq'::regclass);


--
-- Name: farmer_registration_partitioned farmer_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_registration_partitioned ALTER COLUMN farmer_id SET DEFAULT nextval('public.farmer_registration_partitioned_farmer_id_seq'::regclass);


--
-- Name: forgot_password_requests request_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.forgot_password_requests ALTER COLUMN request_id SET DEFAULT nextval('public.forgot_password_requests_request_id_seq'::regclass);


--
-- Name: irrigation_details irrigation_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.irrigation_details ALTER COLUMN irrigation_id SET DEFAULT nextval('public.irrigation_details_irrigation_id_seq'::regclass);


--
-- Name: irrigation_sources source_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.irrigation_sources ALTER COLUMN source_id SET DEFAULT nextval('public.irrigation_sources_source_id_seq'::regclass);


--
-- Name: password_reset reset_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.password_reset ALTER COLUMN reset_id SET DEFAULT nextval('public.password_reset_reset_id_seq'::regclass);


--
-- Name: password_reset_requests reset_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.password_reset_requests ALTER COLUMN reset_id SET DEFAULT nextval('public.password_reset_requests_reset_id_seq'::regclass);


--
-- Name: portal_access access_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.portal_access ALTER COLUMN access_id SET DEFAULT nextval('public.portal_access_access_id_seq'::regclass);


--
-- Name: proposed_crop_info proposed_crop_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proposed_crop_info ALTER COLUMN proposed_crop_id SET DEFAULT nextval('public.proposed_crop_info_proposed_crop_id_seq'::regclass);


--
-- Name: proposed_crop_irrigation irrigation_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proposed_crop_irrigation ALTER COLUMN irrigation_id SET DEFAULT nextval('public.proposed_crop_irrigation_irrigation_id_seq'::regclass);


--
-- Name: proposed_crop_types crop_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proposed_crop_types ALTER COLUMN crop_id SET DEFAULT nextval('public.proposed_crop_types_crop_id_seq'::regclass);


--
-- Name: states state_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.states ALTER COLUMN state_id SET DEFAULT nextval('public.states_state_id_seq'::regclass);


--
-- Name: user_login user_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_login ALTER COLUMN user_id SET DEFAULT nextval('public.user_login_user_id_seq'::regclass);


--
-- Name: users user_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN user_id SET DEFAULT nextval('public.users_user_id_seq'::regclass);


--
-- Name: villages village_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.villages ALTER COLUMN village_id SET DEFAULT nextval('public.villages_village_id_seq'::regclass);


--
-- Name: blocks blocks_block_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.blocks
    ADD CONSTRAINT blocks_block_name_key UNIQUE (block_name);


--
-- Name: blocks blocks_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.blocks
    ADD CONSTRAINT blocks_pkey PRIMARY KEY (block_id);


--
-- Name: countries countries_country_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.countries
    ADD CONSTRAINT countries_country_name_key UNIQUE (country_name);


--
-- Name: countries countries_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.countries
    ADD CONSTRAINT countries_pkey PRIMARY KEY (country_id);


--
-- Name: crop_types crop_types_crop_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.crop_types
    ADD CONSTRAINT crop_types_crop_name_key UNIQUE (crop_name);


--
-- Name: crop_types crop_types_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.crop_types
    ADD CONSTRAINT crop_types_pkey PRIMARY KEY (crop_id);


--
-- Name: districts districts_district_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.districts
    ADD CONSTRAINT districts_district_name_key UNIQUE (district_name);


--
-- Name: districts districts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.districts
    ADD CONSTRAINT districts_pkey PRIMARY KEY (district_id);


--
-- Name: education_levels education_levels_education_level_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.education_levels
    ADD CONSTRAINT education_levels_education_level_key UNIQUE (education_level);


--
-- Name: education_levels education_levels_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.education_levels
    ADD CONSTRAINT education_levels_pkey PRIMARY KEY (education_id);


--
-- Name: farmer_address farmer_address_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_address
    ADD CONSTRAINT farmer_address_pkey PRIMARY KEY (address_id);


--
-- Name: farmer_bank_details farmer_bank_details_account_number_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_bank_details
    ADD CONSTRAINT farmer_bank_details_account_number_key UNIQUE (account_number);


--
-- Name: farmer_bank_details farmer_bank_details_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_bank_details
    ADD CONSTRAINT farmer_bank_details_pkey PRIMARY KEY (bank_id);


--
-- Name: farmer_crop_info farmer_crop_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_crop_info
    ADD CONSTRAINT farmer_crop_info_pkey PRIMARY KEY (crop_info_id);


--
-- Name: farmer_details farmer_details_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_details
    ADD CONSTRAINT farmer_details_pkey PRIMARY KEY (farmer_id);


--
-- Name: farmer_documents farmer_documents_document_number_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_documents
    ADD CONSTRAINT farmer_documents_document_number_key UNIQUE (document_number);


--
-- Name: farmer_documents farmer_documents_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_documents
    ADD CONSTRAINT farmer_documents_pkey PRIMARY KEY (document_id);


--
-- Name: farmer_family_contact_info farmer_family_contact_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_family_contact_info
    ADD CONSTRAINT farmer_family_contact_info_pkey PRIMARY KEY (family_contact_id);


--
-- Name: farmer_personal_info farmer_personal_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_personal_info
    ADD CONSTRAINT farmer_personal_info_pkey PRIMARY KEY (farmer_id);


--
-- Name: farmer_professional_info farmer_professional_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_professional_info
    ADD CONSTRAINT farmer_professional_info_pkey PRIMARY KEY (professional_id);


--
-- Name: farmer_registration farmer_registration_email_address_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_registration
    ADD CONSTRAINT farmer_registration_email_address_key UNIQUE (email_address);


--
-- Name: farmer_registration_partitioned farmer_registration_email_uniq; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_registration_partitioned
    ADD CONSTRAINT farmer_registration_email_uniq UNIQUE (email_address, state);


--
-- Name: farmer_registration_karnataka farmer_registration_karnataka_email_address_state_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_registration_karnataka
    ADD CONSTRAINT farmer_registration_karnataka_email_address_state_key UNIQUE (email_address, state);


--
-- Name: farmer_registration_partitioned farmer_registration_phone_uniq; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_registration_partitioned
    ADD CONSTRAINT farmer_registration_phone_uniq UNIQUE (phone_number, state);


--
-- Name: farmer_registration_karnataka farmer_registration_karnataka_phone_number_state_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_registration_karnataka
    ADD CONSTRAINT farmer_registration_karnataka_phone_number_state_key UNIQUE (phone_number, state);


--
-- Name: farmer_registration_partitioned farmer_registration_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_registration_partitioned
    ADD CONSTRAINT farmer_registration_pk PRIMARY KEY (farmer_id, state);


--
-- Name: farmer_registration_karnataka farmer_registration_karnataka_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_registration_karnataka
    ADD CONSTRAINT farmer_registration_karnataka_pkey PRIMARY KEY (farmer_id, state);


--
-- Name: farmer_registration farmer_registration_phone_number_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_registration
    ADD CONSTRAINT farmer_registration_phone_number_key UNIQUE (phone_number);


--
-- Name: farmer_registration farmer_registration_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_registration
    ADD CONSTRAINT farmer_registration_pkey PRIMARY KEY (farmer_id);


--
-- Name: farmer_registration_tamilnadu farmer_registration_tamilnadu_email_address_state_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_registration_tamilnadu
    ADD CONSTRAINT farmer_registration_tamilnadu_email_address_state_key UNIQUE (email_address, state);


--
-- Name: farmer_registration_tamilnadu farmer_registration_tamilnadu_phone_number_state_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_registration_tamilnadu
    ADD CONSTRAINT farmer_registration_tamilnadu_phone_number_state_key UNIQUE (phone_number, state);


--
-- Name: farmer_registration_tamilnadu farmer_registration_tamilnadu_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_registration_tamilnadu
    ADD CONSTRAINT farmer_registration_tamilnadu_pkey PRIMARY KEY (farmer_id, state);


--
-- Name: forgot_password_requests forgot_password_requests_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.forgot_password_requests
    ADD CONSTRAINT forgot_password_requests_pkey PRIMARY KEY (request_id);


--
-- Name: forgot_password_requests forgot_password_requests_reset_token_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.forgot_password_requests
    ADD CONSTRAINT forgot_password_requests_reset_token_key UNIQUE (reset_token);


--
-- Name: irrigation_details irrigation_details_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.irrigation_details
    ADD CONSTRAINT irrigation_details_pkey PRIMARY KEY (irrigation_id);


--
-- Name: irrigation_sources irrigation_sources_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.irrigation_sources
    ADD CONSTRAINT irrigation_sources_pkey PRIMARY KEY (source_id);


--
-- Name: irrigation_sources irrigation_sources_source_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.irrigation_sources
    ADD CONSTRAINT irrigation_sources_source_name_key UNIQUE (source_name);


--
-- Name: password_reset password_reset_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.password_reset
    ADD CONSTRAINT password_reset_pkey PRIMARY KEY (reset_id);


--
-- Name: password_reset_requests password_reset_requests_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.password_reset_requests
    ADD CONSTRAINT password_reset_requests_pkey PRIMARY KEY (reset_id);


--
-- Name: password_reset password_reset_reset_token_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.password_reset
    ADD CONSTRAINT password_reset_reset_token_key UNIQUE (reset_token);


--
-- Name: portal_access portal_access_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.portal_access
    ADD CONSTRAINT portal_access_pkey PRIMARY KEY (access_id);


--
-- Name: proposed_crop_info proposed_crop_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proposed_crop_info
    ADD CONSTRAINT proposed_crop_info_pkey PRIMARY KEY (proposed_crop_id);


--
-- Name: proposed_crop_irrigation proposed_crop_irrigation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proposed_crop_irrigation
    ADD CONSTRAINT proposed_crop_irrigation_pkey PRIMARY KEY (irrigation_id);


--
-- Name: proposed_crop_types proposed_crop_types_crop_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proposed_crop_types
    ADD CONSTRAINT proposed_crop_types_crop_name_key UNIQUE (crop_name);


--
-- Name: proposed_crop_types proposed_crop_types_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proposed_crop_types
    ADD CONSTRAINT proposed_crop_types_pkey PRIMARY KEY (crop_id);


--
-- Name: states states_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.states
    ADD CONSTRAINT states_pkey PRIMARY KEY (state_id);


--
-- Name: states states_state_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.states
    ADD CONSTRAINT states_state_name_key UNIQUE (state_name);


--
-- Name: farmer_registration unique_email; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_registration
    ADD CONSTRAINT unique_email UNIQUE (email_address);


--
-- Name: farmer_registration unique_phone_number; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_registration
    ADD CONSTRAINT unique_phone_number UNIQUE (phone_number);


--
-- Name: user_login user_login_login_identifier_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_login
    ADD CONSTRAINT user_login_login_identifier_key UNIQUE (login_identifier);


--
-- Name: user_login user_login_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_login
    ADD CONSTRAINT user_login_pkey PRIMARY KEY (user_id);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_phone_number_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_phone_number_key UNIQUE (phone_number);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


--
-- Name: villages villages_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.villages
    ADD CONSTRAINT villages_pkey PRIMARY KEY (village_id);


--
-- Name: idx_farmer_land; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_farmer_land ON public.farmer_crop_info USING btree (total_land_holding_acres);


--
-- Name: idx_farmer_pincode; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_farmer_pincode ON public.farmer_registration USING btree (pin_code);


--
-- Name: idx_farmer_state; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_farmer_state ON public.farmer_registration USING btree (state);


--
-- Name: farmer_registration_karnataka_email_address_state_key; Type: INDEX ATTACH; Schema: public; Owner: postgres
--

ALTER INDEX public.farmer_registration_email_uniq ATTACH PARTITION public.farmer_registration_karnataka_email_address_state_key;


--
-- Name: farmer_registration_karnataka_phone_number_state_key; Type: INDEX ATTACH; Schema: public; Owner: postgres
--

ALTER INDEX public.farmer_registration_phone_uniq ATTACH PARTITION public.farmer_registration_karnataka_phone_number_state_key;


--
-- Name: farmer_registration_karnataka_pkey; Type: INDEX ATTACH; Schema: public; Owner: postgres
--

ALTER INDEX public.farmer_registration_pk ATTACH PARTITION public.farmer_registration_karnataka_pkey;


--
-- Name: farmer_registration_tamilnadu_email_address_state_key; Type: INDEX ATTACH; Schema: public; Owner: postgres
--

ALTER INDEX public.farmer_registration_email_uniq ATTACH PARTITION public.farmer_registration_tamilnadu_email_address_state_key;


--
-- Name: farmer_registration_tamilnadu_phone_number_state_key; Type: INDEX ATTACH; Schema: public; Owner: postgres
--

ALTER INDEX public.farmer_registration_phone_uniq ATTACH PARTITION public.farmer_registration_tamilnadu_phone_number_state_key;


--
-- Name: farmer_registration_tamilnadu_pkey; Type: INDEX ATTACH; Schema: public; Owner: postgres
--

ALTER INDEX public.farmer_registration_pk ATTACH PARTITION public.farmer_registration_tamilnadu_pkey;


--
-- Name: farmer_registration trigger_sync_farmer_details; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trigger_sync_farmer_details AFTER INSERT ON public.farmer_registration FOR EACH ROW EXECUTE FUNCTION public.sync_farmer_details();


--
-- Name: blocks blocks_district_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.blocks
    ADD CONSTRAINT blocks_district_id_fkey FOREIGN KEY (district_id) REFERENCES public.districts(district_id) ON DELETE CASCADE;


--
-- Name: districts districts_state_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.districts
    ADD CONSTRAINT districts_state_id_fkey FOREIGN KEY (state_id) REFERENCES public.states(state_id) ON DELETE CASCADE;


--
-- Name: farmer_address farmer_address_block_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_address
    ADD CONSTRAINT farmer_address_block_id_fkey FOREIGN KEY (block_id) REFERENCES public.blocks(block_id);


--
-- Name: farmer_address farmer_address_country_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_address
    ADD CONSTRAINT farmer_address_country_id_fkey FOREIGN KEY (country_id) REFERENCES public.countries(country_id);


--
-- Name: farmer_address farmer_address_district_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_address
    ADD CONSTRAINT farmer_address_district_id_fkey FOREIGN KEY (district_id) REFERENCES public.districts(district_id);


--
-- Name: farmer_address farmer_address_state_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_address
    ADD CONSTRAINT farmer_address_state_id_fkey FOREIGN KEY (state_id) REFERENCES public.states(state_id);


--
-- Name: farmer_address farmer_address_village_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_address
    ADD CONSTRAINT farmer_address_village_id_fkey FOREIGN KEY (village_id) REFERENCES public.villages(village_id);


--
-- Name: farmer_crop_info farmer_crop_info_crop_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_crop_info
    ADD CONSTRAINT farmer_crop_info_crop_id_fkey FOREIGN KEY (crop_id) REFERENCES public.crop_types(crop_id);


--
-- Name: farmer_professional_info farmer_professional_info_education_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_professional_info
    ADD CONSTRAINT farmer_professional_info_education_id_fkey FOREIGN KEY (education_id) REFERENCES public.education_levels(education_id);


--
-- Name: farmer_crop_info fk_crop_farmer; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_crop_info
    ADD CONSTRAINT fk_crop_farmer FOREIGN KEY (farmer_id) REFERENCES public.farmer_personal_info(farmer_id) ON DELETE CASCADE;


--
-- Name: farmer_family_contact_info fk_farmer; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_family_contact_info
    ADD CONSTRAINT fk_farmer FOREIGN KEY (farmer_id) REFERENCES public.farmer_personal_info(farmer_id) ON DELETE CASCADE;


--
-- Name: farmer_bank_details fk_farmer; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_bank_details
    ADD CONSTRAINT fk_farmer FOREIGN KEY (farmer_id) REFERENCES public.farmer_personal_info(farmer_id) ON DELETE CASCADE;


--
-- Name: farmer_address fk_farmer_address; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_address
    ADD CONSTRAINT fk_farmer_address FOREIGN KEY (farmer_id) REFERENCES public.farmer_personal_info(farmer_id) ON DELETE CASCADE;


--
-- Name: farmer_documents fk_farmer_documents; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_documents
    ADD CONSTRAINT fk_farmer_documents FOREIGN KEY (farmer_id) REFERENCES public.farmer_personal_info(farmer_id) ON DELETE CASCADE;


--
-- Name: portal_access fk_farmer_portal; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.portal_access
    ADD CONSTRAINT fk_farmer_portal FOREIGN KEY (farmer_id) REFERENCES public.farmer_personal_info(farmer_id) ON DELETE CASCADE;


--
-- Name: farmer_details fk_farmer_registration; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_details
    ADD CONSTRAINT fk_farmer_registration FOREIGN KEY (farmer_id) REFERENCES public.farmer_registration(farmer_id) ON DELETE CASCADE;


--
-- Name: irrigation_details fk_irrigation_farmer; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.irrigation_details
    ADD CONSTRAINT fk_irrigation_farmer FOREIGN KEY (farmer_id) REFERENCES public.farmer_personal_info(farmer_id) ON DELETE CASCADE;


--
-- Name: farmer_professional_info fk_prof_farmer; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.farmer_professional_info
    ADD CONSTRAINT fk_prof_farmer FOREIGN KEY (farmer_id) REFERENCES public.farmer_personal_info(farmer_id) ON DELETE CASCADE;


--
-- Name: proposed_crop_info fk_proposed_crop_farmer; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proposed_crop_info
    ADD CONSTRAINT fk_proposed_crop_farmer FOREIGN KEY (farmer_id) REFERENCES public.farmer_personal_info(farmer_id) ON DELETE CASCADE;


--
-- Name: proposed_crop_irrigation fk_proposed_crop_irrigation_crop; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proposed_crop_irrigation
    ADD CONSTRAINT fk_proposed_crop_irrigation_crop FOREIGN KEY (proposed_crop_id) REFERENCES public.proposed_crop_info(proposed_crop_id) ON DELETE CASCADE;


--
-- Name: proposed_crop_irrigation fk_proposed_crop_irrigation_farmer; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proposed_crop_irrigation
    ADD CONSTRAINT fk_proposed_crop_irrigation_farmer FOREIGN KEY (farmer_id) REFERENCES public.farmer_personal_info(farmer_id) ON DELETE CASCADE;


--
-- Name: forgot_password_requests fk_user_login; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.forgot_password_requests
    ADD CONSTRAINT fk_user_login FOREIGN KEY (user_id) REFERENCES public.user_login(user_id);


--
-- Name: irrigation_details irrigation_details_water_source_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.irrigation_details
    ADD CONSTRAINT irrigation_details_water_source_id_fkey FOREIGN KEY (water_source_id) REFERENCES public.irrigation_sources(source_id);


--
-- Name: password_reset_requests password_reset_requests_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.password_reset_requests
    ADD CONSTRAINT password_reset_requests_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- Name: password_reset password_reset_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.password_reset
    ADD CONSTRAINT password_reset_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.user_login(user_id);


--
-- Name: proposed_crop_info proposed_crop_info_crop_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proposed_crop_info
    ADD CONSTRAINT proposed_crop_info_crop_id_fkey FOREIGN KEY (crop_id) REFERENCES public.proposed_crop_types(crop_id);


--
-- Name: proposed_crop_irrigation proposed_crop_irrigation_water_source_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proposed_crop_irrigation
    ADD CONSTRAINT proposed_crop_irrigation_water_source_id_fkey FOREIGN KEY (water_source_id) REFERENCES public.irrigation_sources(source_id);


--
-- Name: states states_country_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.states
    ADD CONSTRAINT states_country_id_fkey FOREIGN KEY (country_id) REFERENCES public.countries(country_id) ON DELETE CASCADE;


--
-- Name: villages villages_block_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.villages
    ADD CONSTRAINT villages_block_id_fkey FOREIGN KEY (block_id) REFERENCES public.blocks(block_id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

