#!/usr/bin/env bash
heroku config:set SPRING_PROFILES_ACTIVE=hsqldb --app ticket-magpie
heroku deploy:jar target/ticketmagpie.jar --app ticket-magpie
