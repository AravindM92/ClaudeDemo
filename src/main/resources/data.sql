-- Insert technicians first
INSERT INTO technician (tech_id, tech_name, doj) VALUES
    (1, 'John Doe', '2024-01-15'),
    (2, 'Jane Smith', '2024-02-01')
ON CONFLICT (tech_id) DO NOTHING;

-- Then insert jobs
INSERT INTO job (description, tech_id, created_date, completed_date, status) 
SELECT 'Fix server connectivity issue', 1, '2024-03-10 09:00:00', '2024-03-10 14:30:00', 'COMPLETED'
WHERE NOT EXISTS (
    SELECT 1 FROM job WHERE description = 'Fix server connectivity issue'
);

INSERT INTO job (description, tech_id, created_date, completed_date, status)
SELECT 'Install new software', 1, '2024-03-11 10:00:00', NULL, 'IN_PROGRESS'
WHERE NOT EXISTS (
    SELECT 1 FROM job WHERE description = 'Install new software'
);

INSERT INTO job (description, tech_id, created_date, completed_date, status)
SELECT 'Setup network printer', 2, '2024-03-11 11:00:00', NULL, 'PENDING'
WHERE NOT EXISTS (
    SELECT 1 FROM job WHERE description = 'Setup network printer'
); 