### Текущий сервис
* user-service – микросервис для управления пользователями

### Описание системы
Тестовая система, которая имитирует процесс создания заказа на покупку поста пользователя.

### Реализованные микросервисы
1\. user-migrate – spring-boot микросервис, выполняет миграции БД для микросервиса user-service:
* в качестве СУБД используется PostgreSQL 16.8
* в качестве либы для миграций используется Liquibase

2\. user-service – spring-boot микросервис для управления пользователями:
* предоставляет gRPC-метод GetUser для проверки существования пользователя
* для тестирования используется Testcontainers

### Локальный запуск компонентов

Start DB:
```bash
docker run -d --hostname test_db --name user_post_db -p 5454:5432 -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=verySecret! -e POSTGRES_DB=user_post_repository -v user_post-data:/var/lib/postgresql/data --restart=unless-stopped postgres:16.8
```

Enter to container:
```bash
docker exec -ti user_post_db /bin/bash
```
