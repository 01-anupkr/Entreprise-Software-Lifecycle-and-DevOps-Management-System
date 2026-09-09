
INSERT INTO users (name, email, password, role)
SELECT 'Anup Kumar', 'anup.kumar@neuroforge.com', 'hashed_pass_123', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'anup.kumar@neuroforge.com');

INSERT INTO users (name, email, password, role)
SELECT 'Piyushkant Bhardwaj', 'piyush.kant@neuroforge.com', '12345678', 'TESTER'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'piyush.kant@neuroforge.com');

INSERT INTO projects (project_name, description)
SELECT 'NeuroCore Engine', 'Core AI processing pipeline'
WHERE NOT EXISTS (SELECT 1 FROM projects WHERE project_name = 'NeuroCore Engine');

INSERT INTO projects (project_name, description)
SELECT 'Forge UI Kit', 'Frontend component library'
WHERE NOT EXISTS (SELECT 1 FROM projects WHERE project_name = 'Forge UI Kit');

INSERT INTO repository (project_id, repository_name, repository_url)
SELECT 1, 'neuro-core-repo', 'https://github.com/neuroforge/neuro-core'
WHERE NOT EXISTS (SELECT 1 FROM repository WHERE repository_name = 'neuro-core-repo');

INSERT INTO repository (project_id, repository_name, repository_url)
SELECT 2, 'forge-ui-repo', 'https://github.com/neuroforge/forge-ui'
WHERE NOT EXISTS (SELECT 1 FROM repository WHERE repository_name = 'forge-ui-repo');

INSERT INTO test_cases (test_name, test_steps, expected_result, actual_result, status, task_id)
SELECT 'GPU Stress Test', 'Execute 1000 parallel inferences', 'Response time < 50ms', 'Response time 42ms', 'PASSED', 1
WHERE NOT EXISTS (SELECT 1 FROM test_cases WHERE test_name = 'GPU Stress Test');
