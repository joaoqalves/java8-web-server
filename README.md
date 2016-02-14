# Web Server + example App

## Introduction

This is a web server made with Java 8. To run it, you don't need any special dependency. All the relevant code for the server is available at ``net.joaoqalves.core``.

## Example application demo

There is an example application demo available at ``http://demo01.joaoqalves.net``.

### Available resources

The following resources are available:

+ ``GET /login``
+ ``POST /sessions/create``
+ ``GET /sessions/destroy``
+ ``GET /page{1,2,3}``
+ ``GET /users`` (``JSON``)
+ ``GET /users/:username`` (``JSON``)
+ ``PUT /users/admin/:username/update`` (``JSON``)
+ ``DELETE /users/admin/:username/delete`` (``JSON``)

### Users

There are 3 normal users and one administrator:

+ Username: ``user1``, Password: ``1user``, access to ``/page1``, ``/users`` and ``/users/:username``;
+ Username: ``user2``, Password: ``2user``, access to ``/page2``, ``/users``and ``/users/:username``;
+ Username: ``user3``, Password: ``3user``, access to ``/page3``, ``/users`` and ``/users/:username``;
+ Username: ``admin``, Password: ``nimda``, access to ``/users/*``

### Back-end

The back-end is using an [H2 database](http://www.h2database.com/html/main.html) behind to store the users, pages and roles. Moreover, [Hibernate](http://hibernate.org/) is used to communicate with the database. Sessions are stored in memory.

### Further considerations

In this demo instance, the database is restored each 10 minutes.

## How to build and execute it

### Requirements

+ git
+ Java 8
+ Maven 3

Just clone this project and execute ``mvn package``. Then, go to the ``target``folder and execute ``java -jar web-server-1.0.0-SNAPSHOT.jar``.

## Trying out the ReST API

If you want to try out the ReST API just use the above end-points or you can run the ``api_test.py`` script that is present in the root of this repository. Just install the dependencies with ``pip install -r requirements.txt`` and then execute ``python api_test.py`` and you should be good to go :)


## Future improvements (that won't be done :D)

+ Add more test coverage
+ Add content negotiation via HTTP, file extension (e.g., ``users.html``, ``users.json``) and format (e.g., ``users?format=xml``, ``users?format=json``)
+ Improve routing system. It's naive and certainly bug-prone. But it's cooler than have an _if-else-if_ hell :)
+ Find a user's session when its roles changed and update it or just terminate the users's session. We could use [RxJava](https://github.com/ReactiveX/RxJava) to make the session _Observable_ and to make this update smooth.

