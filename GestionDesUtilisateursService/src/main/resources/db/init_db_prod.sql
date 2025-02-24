-- Create the schema if it doesn't already exist, owned by prod_user
CREATE SCHEMA IF NOT EXISTS gestion_utilisateurs AUTHORIZATION prod_user;
ALTER TABLE roles
DROP CONSTRAINT roles_name_check;

ALTER TABLE roles
    ADD CONSTRAINT roles_name_check CHECK (
        name::text = ANY (ARRAY['USER', 'MEDECIN', 'CONSEILLER', 'LOGISTICIEN', 'CLIENT', 'PARTENAIRE']::text[])
    );