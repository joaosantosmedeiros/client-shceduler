CREATE TABLE Speciality (
  id uuid PRIMARY KEY,
  name varchar(50) UNIQUE NOT NULL,
  is_active boolean NOT NULL,
  created_at timestamp NOT NULL,
  updated_at timestamp
);