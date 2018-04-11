#!/usr/bin/env bash

psql -c "create user sample22plain with password 'sample22plain';" -U postgres
psql -c "create database sample22plain;" -U postgres
psql -c "grant all on database sample22plain to sample22plain;" -U postgres

psql -c "create user sample22spring with password 'sample22spring';" -U postgres
psql -c "create database sample22spring;" -U postgres
psql -c "grant all on database sample22spring to sample22spring;" -U postgres

psql -c "create user sample22jpa with password 'sample22jpa';" -U postgres
psql -c "create database sample22jpa;" -U postgres
psql -c "grant all on database sample22jpa to sample22jpa;" -U postgres