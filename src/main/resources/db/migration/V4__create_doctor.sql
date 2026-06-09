CREATE TABLE Doctor (
    id uuid PRIMARY KEY,
    name varchar(50) NOT NULL,
    cpf char(11) NOT NULL UNIQUE,
    crm varchar(15) NOT NULL UNIQUE,
    phone varchar(11),
    birth_date date NOT NULL,
    is_active boolean NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp
);