### Текущий сервис
* user-service – микросервис для управления пользователями

### Описание системы
Тестовая система, которая имитирует процесс создания заказа на покупку поста пользователя.

### Реализованные микросервисы
1\. [user-post-storage-db-service](https://github.com/serious-deeev/user-post-storage-db-service/tree/develop) – spring-boot микросервис, выполняет миграции БД для микросервисов user-service, post-service и order-service:
* в качестве СУБД используется PostgreSQL 16.8
* в качестве либы для миграций используется Liquibase
* создает единую схему БД `user_post_storage` для микросервисов

2\. [user-service](https://github.com/serious-deeev/user-service/tree/develop) – spring-boot микросервис для управления пользователями:
* предоставляет gRPC-метод GetUser для проверки существования пользователя
* для тестирования используется Testcontainers

3\. [post-service](https://github.com/serious-deeev/post-service/tree/develop) – spring-boot микросервис для управления постами пользователей:
* предоставляет gRPC-метод GetPost для проверки существования поста
* предоставляет gRPC-метод ReservePost для бронирования поста
* для тестирования используется Testcontainers

4\. [order-service](https://github.com/serious-deeev/order-service/tree/develop) – spring-boot микросервис для создания заказов:
* содержит контроллер для обработки HTTP-запросов на создание заказов
* предоставляет gRPC-клиенты для взаимодействия с сервисами user-service и post-service
* отвечает за выполнение распределенной транзакции
* для тестирования используется Testcontainers

5\. [notification-service](https://github.com/serious-deeev/notification-service/tree/develop) – spring-boot микросервис для отправки уведомления о созданном заказе владельцу поста:
* выполняет отправку уведомлений посредством технической почты

### Локальный запуск компонентов

Запуск DB:
```bash
docker run -d --hostname test_db --name user_post_db -p 5454:5432 -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=verySecret! -e POSTGRES_DB=user_post_repository -v user_post-data:/var/lib/postgresql/data --restart=unless-stopped postgres:16.8
```

Войти в контейнер с БД:
```bash
docker exec -ti user_post_db /bin/bash
```

Запуск kafka в режиме kraft (broker + controller):
```bash
docker run -d --restart=unless-stopped \
  --name test-user-post-kafka-broker \
  -p 9092:9092 \
  -v test-user-post-kafka-broker-data:/var/lib/kafka/data \
  -e KAFKA_NODE_ID=1 \
  -e KAFKA_PROCESS_ROLES=broker,controller \
  -e KAFKA_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093 \
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
  -e KAFKA_CONTROLLER_LISTENER_NAMES=CONTROLLER \
  -e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT \
  -e KAFKA_CONTROLLER_QUORUM_VOTERS=1@localhost:9093 \
  -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
  -e KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR=1 \
  -e KAFKA_TRANSACTION_STATE_LOG_MIN_ISR=1 \
  -e KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS=0 \
  -e KAFKA_NUM_PARTITIONS=3 \
  apache/kafka:3.9.1
```
