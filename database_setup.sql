-- Drop tables if they exist
DROP TABLE IF EXISTS job;
DROP TABLE IF EXISTS technician;

-- Create technician table
CREATE TABLE technician (
    tech_id SERIAL PRIMARY KEY,
    tech_name VARCHAR(100) NOT NULL,
    doj DATE NOT NULL
);

-- Create job table
CREATE TABLE job (
    job_id SERIAL PRIMARY KEY,
    description TEXT NOT NULL,
    tech_id INTEGER NOT NULL,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_date TIMESTAMP,
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED')),
    CONSTRAINT fk_technician
        FOREIGN KEY (tech_id)
        REFERENCES technician(tech_id)
        ON DELETE RESTRICT
);

-- Insert sample technicians
INSERT INTO technician (tech_name, doj) VALUES
    ('John Doe', '2024-01-15'),
    ('Jane Smith', '2024-02-01');

-- Insert sample jobs
INSERT INTO job (description, tech_id, created_date, completed_date, status) VALUES
    ('Fix server connectivity issue', 1, '2024-03-10 09:00:00', '2024-03-10 14:30:00', 'COMPLETED'),
    ('Install new software', 1, '2024-03-11 10:00:00', NULL, 'IN_PROGRESS'),
    ('Setup network printer', 2, '2024-03-11 11:00:00', NULL, 'PENDING'); 