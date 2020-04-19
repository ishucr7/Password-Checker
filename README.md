# Password-Checker
Password checker in Java


- Add your db configurations in the dbdriver/DBConnection.java
- In your mysql server create a database and tables users and previous_passwords in the in the created db.

Run the following commands on your mysql server. Will add db seeding into it later on.

- create database password_checker;

- CREATE TABLE users (
    username varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    invalid_login_count int DEFAULT 0,
    creation_date TIMESTAMP,
    is_locked boolean,
    PRIMARY KEY (username)
);

- CREATE TABLE previous_passwords (
    username varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    creation_date TIMESTAMP,
    PRIMARY KEY (username, password)
);


How to run?
- Inside the root directory(Compile the java files)
- javac -cp . models/*java dbdriver/*java mypack/*java run.java
- java run.java

There you go!
