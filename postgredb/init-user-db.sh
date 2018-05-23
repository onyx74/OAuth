#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE USER vanya WITH PASSWORD '123456';
    CREATE DATABASE usersdb;
    CREATE DATABASE loaddb;
    CREATE DATABASE truckdb;
    CREATE DATABASE vrpdb;
    GRANT ALL PRIVILEGES ON DATABASE usersdb TO vanya;
    GRANT ALL PRIVILEGES ON DATABASE loaddb TO vanya;
    GRANT ALL PRIVILEGES ON DATABASE truckdb TO vanya;
    GRANT ALL PRIVILEGES ON DATABASE vrpdb TO vanya;
EOSQL
