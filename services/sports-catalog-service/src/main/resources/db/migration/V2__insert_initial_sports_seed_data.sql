INSERT INTO sports (id, name, active)
VALUES
    ('8d9f3f9e-2e4b-4e1f-9b27-7d5e9b8d2c41', 'Football', true),
    ('c3b9a2e4-7f6d-4a1c-9f35-2d8e5b7c1a90', 'Basketball', true),
    ('0e4b7d8f-9a2c-4f3e-8b61-5d7c9e1a3f24', 'Tennis', true),
    ('f2a8c1d7-6e9b-4c3f-8d25-9b7e1a4c6f30', 'Handball', true),
    ('7b1e9d4c-3f8a-4c6e-9d72-1a5b8f0e3c49', 'Volleyball', true),
    ('a6d4f8c2-1b7e-4f9a-8c35-6e2d9b1f7a84', 'Rugby', true),
    ('3f7a9c1e-5d2b-4e8f-9a64-c1e7b3d5f802', 'Baseball', true),
    ('b8e2d5f1-9c7a-4a3e-8f26-5d1b7c9e4a60', 'American Football', true),
    ('5c9f1a7e-2d8b-4f6c-9e31-7a4d2b8f0c95', 'Ice Hockey', true),
    ('e1a7c5f9-8d2b-4c6f-9a35-2b7e4d1c8f60', 'Field Hockey', true),
    ('9d4f2b8e-1c7a-4f5e-8b63-c2e9a7d1f405', 'Cricket', true),
    ('2b8e5c1f-7a4d-4f9c-8e36-1d5f7b2a9c40', 'Padel', true),
    ('6f1a9d3c-2e7b-4c8f-9d45-a5b1e7c2f803', 'Golf', true),
    ('d5c2f8a1-9e4b-4f7c-8a36-2d1b9e5f7c40', 'Boxing', true),
    ('1a7e4d9f-5c2b-4f8e-9a63-d7b1c5e2f804', 'Cycling', true),
    ('4e9c1f7a-2d5b-4c8e-9f36-b1a7d2e5c904', 'Athletics', true),
    ('7c2f8a1d-9e5b-4f6c-8d34-a1b7e9c5f203', 'Swimming', true),
    ('c9e1a7d4-5f2b-4c8e-9a36-7d1b5f2c8e04', 'Motorsport', true),
    ('8f5c2a1d-7e9b-4d6f-8c34-b2a7e1d5f903', 'MMA', true),
    ('f1d7c5a9-2e4b-4f8c-9a63-5b1e7d2c8f04', 'Table Tennis', true)
    ON CONFLICT (id) DO NOTHING;