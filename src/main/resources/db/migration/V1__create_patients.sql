CREATE TABLE Patient (
    id uuid PRIMARY KEY,
    name varchar(50) NOT NULL,
    email varchar(50) NOT NULL,
    phone varchar(11),
    birth_date date NOT NULL,
    is_active boolean NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp
);