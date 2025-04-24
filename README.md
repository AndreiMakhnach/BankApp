# BankApp

**Микросервис для управления банковскими операциями** с REST API, JWT-аутентификацией и PostgreSQL.

## Ключевые возможности

- **Безопасность**: JWT-аутентификация с ролями USER/ADMIN
- **Фильтрация**: Поиск пользователей по параметрам (имя, email, телефон, дата рождения)
- **Операции**: Переводы между счетами с проверкой баланса
- **Тестирование**: 
  - Unit-тесты с Mockito
  - Интеграционные тесты с Testcontainers
- **Логирование**: Настройка уровней логирования через application.yml

## Технологии

| Категория       | Технологии                                                                 |
|-----------------|----------------------------------------------------------------------------|
| Язык            | Java 11                                                                    |
| Фреймворк       | Spring Boot 3.1.2, Spring Security, Spring Data JPA                        |
| База данных     | PostgreSQL 13 + Hibernate                                                  |
| Тестирование    | JUnit 5, Mockito, Testcontainers, MockMvc                                  |
| Инструменты     | Lombok, MapStruct, Swagger                                                 |
| Деплой          | Docker-контейнеризация, GitHub Actions CI/CD                               |

### Приложение запускается через Docker
docker-compose up -d

text
Приложение будет доступно на `http://localhost:8080`

### Подключение бд происходит вручную


## Документация API
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI спецификация**: `http://localhost:8080/v3/api-docs`

Пример запроса для поиска пользователей:
GET /api/users/search?name=Иван&page=0&size=10 HTTP/1.1
Authorization: Bearer <JWT_TOKEN>

text

## Тестовое покрытие

Запуск всех тестов
mvn test
