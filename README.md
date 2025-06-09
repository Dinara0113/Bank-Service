🏦 Recommendation Bank Assistant

Общее описание
Recommendation Bank Assistant — это рекомендательный сервис для банковских продуктов. Система позволяет формировать кастомные рекомендации на основе динамических и статических правил, а также выдавать их по запросу API или через Telegram-бота.

Стек технологий:
* Java 21
* Spring Boot 3
* Spring Data JDBC
* PostgreSQL + H2 (Test)
* Liquibase
* Caffeine Cache
* TelegramBots API
* Swagger (Springdoc OpenAPI)
* Lombok

 📂 Документация

- [📖 Архитектура и диаграммы](https://github.com/Dinara0113/Bank-Service/wiki/Архитектура)
- [✅ User Stories и требования](https://github.com/Dinara0113/Bank-Service/wiki/User-Stories)
- [📊 OpenAPI / Swagger](http://localhost:8080/swagger-ui.html)
- [📦 Инструкция по развёртыванию](https://github.com/Dinara0113/Bank-Service/wiki/Инструкция-развёртывания)


Автор: Dinara Allanazarova (Java Developer)

Функциональность:
* Выдача рекомендаций на основе правил
* Добавление/удаление динамических правил (CRUD)
* Кеширование ответов из базы знаний
* Статистика срабатываний правил
* Telegram-бот для получения рекомендаций
* Сброс кеша и получение метаинформации о сервисе


