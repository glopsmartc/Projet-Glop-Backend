DELETE FROM roles;
ALTER TABLE roles ALTER COLUMN id RESTART WITH 1;
