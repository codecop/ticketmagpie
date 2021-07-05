call heroku config:set SPRING_PROFILES_ACTIVE=hsqldb --app ticket-magpie
call heroku deploy:jar target\ticketmagpie.jar --app ticket-magpie
