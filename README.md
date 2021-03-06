[![Build Status](https://travis-ci.com/mickoallen/vuetinaut.svg?branch=master)](https://travis-ci.com/mickoallen/vuetinaut) [![Coverage Status](https://coveralls.io/repos/github/mickoallen/vuetinaut/badge.svg?branch=master)](https://coveralls.io/github/mickoallen/vuetinaut?branch=master)
# Vuetinaut

Template project for prototyping cool things.

https://micknotes.herokuapp.com/


##### Built from....
Micronaut, Vue, Vuetify, Postgres

## Features
A cool online notepad app.
Full CI/CD using github actions

todo - add pictures

## Local development
For Intellij users, read the Micronaut guides for setting up the IDE.

1. Start dependencies (db) ```docker-compose up```
2. Run/debug main class ```com.mick.vuetinaut.Application```
3. Start the UI webpack dev server ```cd ui && npm run serve```
4. Open browser ```http://localhost:8080/```
5. Enjoy!

## Deployment
Do nothing. Deploys to Heroku from push to master using GitHub actions.

## Todo
- Note sharing
- Make more durable so user can't lose data
- Look for a reactive postgres client
