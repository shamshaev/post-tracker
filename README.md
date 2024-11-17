# Post Tracker

[![Actions Status](https://github.com/shamshaev/postal-tracker/actions/workflows/self-check.yml/badge.svg)](https://github.com/shamshaev/postal-tracker/actions)
[![Maintainability](https://api.codeclimate.com/v1/badges/31a596f39ffdada4c587/maintainability)](https://codeclimate.com/github/shamshaev/post-tracker/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/31a596f39ffdada4c587/test_coverage)](https://codeclimate.com/github/shamshaev/post-tracker/test_coverage)

It's the RESTful API of a simple post service. This service allows you to create different types of postal items (letter, package, parcel, postcard) and then track their delivery.

Implemented operations:
1. Registration of postal item.
2. Arriving of postal item to post office.
3. Departing of postal item to post office.
4. Receiving of postal item by recipient.
5. Checking of tracking status, including whole delivery history of postal item.

The application is based on Spring Framework, Spring Boot and Spring Data.

### Link to the deployed service: https://post-tracker-6vd6.onrender.com/

- you will be redirected to the integrated Swagger UI from where you can understand API structure and interact with IT.
- service database already includes all post offices (except parcel machines) from 10 cities: Moscow, Saint-Petersburg, Nizhny Novgorod, Kazan, Krasnodar, Sochi, Yekaterinburg, Novosibirsk, Naberezhnye Chelny.
- 2 sample postal items with tracking statuses already created for your convenience.

![screenshot-1](screenshots/screenshot-1.png?raw=true)

For local application start you have to install and configure the following dependencies on your machine:

1. Java 21
2. Docker, Docker Compose

### Start a postgresql database in a docker container

For example, to start a postgresql database in a docker container, run:

```bash
make docker-infra-start
```

### Run application with database in docker

```bash
make start
```