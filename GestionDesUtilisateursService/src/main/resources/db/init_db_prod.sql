-- Create the schema if it doesn't already exist, owned by prod_user
CREATE SCHEMA IF NOT EXISTS gestion_utilisateurs AUTHORIZATION prod_user;
ALTER TABLE gestion_utilisateurs.utilisateur
    ADD COLUMN IF NOT EXISTS sexe VARCHAR(35);
ALTER TABLE gestion_utilisateurs.utilisateur
    ALTER COLUMN date_naissance DROP NOT NULL;
