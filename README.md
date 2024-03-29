﻿﻿# SpringTelegramBot
## Technology stack:
>JAVA, SPRING BOOT, SPRING DATA JPA, MYSQL, HIBERNATE, MAVEN, TELEGRAM BOT API, DOCKER.

### This bot for a salary management system on the Telegram messaging platform.
### It has a few commands for adding, deleting and retrieving income, as well as viewing salaries and advances.
### The bot listens for user input and responds accordingly. When it receives a message, it checks which state it's in and acts accordingly.
### It keeps track of the user's state, which determines which messages it's expecting next.
### The bot is also integrated with a database, which is used to store and retrieve user data such as income, salary, and advance information.
### This project intend to manage and calculate the financial data of the users on the platform.
### The various repositories that are defined in the code enable retrieval and modification of the financial data for each user.
### The User class represents the users on the platform, with each user having a unique chatId and a name.
### The Advance class represents advances taken by a user, with information such as the chatId of the user, the amount of the advance, and the date when the advance was taken.
### The Income class represents the income earned by a user, with information such as the chatId of the user, the amount of the income, and the date when the income was earned.
### The MonthSalary class represents the salary of a user for a specific month, with information such as the chatId of the user, the amount of the salary, and the month and year for which the salary is being recorded.
### The Salary class represents the salary earned by a user on a particular date, with information such as the chatId of the user, the amount of the salary, and the date when the salary was earned.
