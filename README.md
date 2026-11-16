# Side Billing — REST API Gateway для LanBilling 2.0

Шлюз **Side Billing** предоставляет современный, строго типизированный REST API для личного кабинета абонента, мобильных приложений и внешних интеграций поверх SOAP API биллинговой системы LANBilling 2.0 (`Api3PortType`).

---

## 🚀 Ключевые особенности и архитектура

- **100% Stateless архитектура:**
  - Полный отказ от реляционной базы данных (PostgreSQL, JPA, Hibernate исключены).
  - Идентификатор активной сессии биллинга (`Set-Cookie` / `sessnum`) упаковывается и подписывается непосредственно в клейм JWT-токена (`sid`).
  - Нулевая нагрузка на дисковый ввод-вывод и отсутствие блокировок транзакций при проверке сессий абонентов.
- **Типизированный JAX-WS клиент (Apache CXF):**
  - Полный отказ от ручного формирования XML и парсинга ответов через RegEx/Jsoup.
  - Автоматическая кодогенерация 1 963 классов DTO и сервисного интерфейса `Api3PortType` из схемы `docs/wsdl.xml` средствами плагина `io.mateo.cxf-codegen`.
- **Kotlin Coroutines:**
  - Все контроллеры и сервисы используют идиоматичные `suspend`-функции и пул диспетчеров `Dispatchers.IO`, обеспечивая высокую пропускную способность под нагрузкой.
- **Интерактивная документация:**
  - Встроенный Swagger UI / OpenAPI v3 для тестирования всех методов прямо из браузера.

---

## 📋 Требования к окружению

- **JDK:** OpenJDK / Eclipse Temurin 21+
- **Сборщик:** Gradle 9.x (включен Gradle Wrapper)
- **Сетевой доступ:** Доступ к демону LanBilling (порт `34012`)

> [!NOTE]
> Если на рабочей машине включен VPN с глобальным TUN-адаптером, убедитесь, что приватная подсеть биллинга добавлена в исключения (Bypass LAN) либо добавлен статический маршрут в Windows:
> ```cmd
> route -p add 10.222.220.0 mask 255.255.255.0 10.222.222.1
> ```

---

## ⚙️ Конфигурация

Все настройки сервиса поддерживают шаблон `${ПЕРЕМЕННАЯ:значение_по_умолчанию}` и могут быть заданы через переменные окружения ОС, файл `.env` (шаблон доступен в [`.env.example`](.env.example)) или файлы `application-{profile}.properties`:

| Переменная окружения | По умолчанию | Описание |
| :--- | :--- | :--- |
| `SPRING_PROFILES_ACTIVE` | `dev` | Активный профиль приложения (`dev` / `prod`) |
| `SERVER_PORT` | `9998` | Порт HTTP-сервера |
| `SERVER_CONTEXT_PATH` | `/api` | Базовый контекстный путь API |
| `SERVER_ADDRESS` | `0.0.0.0` | Сетевой интерфейс для прослушивания (в prod) |
| `BASE_URL` | `http://10.222.220.3:34012` | URL SOAP API демона LanBilling |
| `JWT_SECRET` | *(дефолтный ключ 256 бит)* | Секретный ключ подписи JWT (Base64 HMAC-SHA256) |
| `JWT_EXP` | `31536000000` | Время жизни JWT токена в миллисекундах (1 год) |
| `SECURITY_USER_NAME` | `admin` | Логин системного пользователя Spring Security |
| `SECURITY_USER_PASSWORD` | `admin123` | Пароль системного пользователя Spring Security |
| `SECURITY_USER_ROLE` | `ADMIN` | Роль системного пользователя |

---

## 🛠️ Сборка и запуск

### 1. Генерация классов из WSDL (если схема обновилась)
```bash
./gradlew wsdl2java
```

### 2. Запуск приложения в режиме разработки
```bash
./gradlew bootRun
```
Приложение будет доступно по адресу: `http://localhost:9998/api`

### 3. Сборка исполняемого JAR
```bash
./gradlew clean bootJar
```
Собранный артефакт появится в каталоге `build/libs/side_billing-0.0.1-SNAPSHOT.jar`.

### 4. Запуск тестов
```bash
./gradlew test
```

---

## 📖 Документация API

После запуска сервиса интерактивная документация доступна по адресам:
- **Swagger UI:** [http://localhost:9998/api/swagger-ui.html](http://localhost:9998/api/swagger-ui.html)
- **OpenAPI v3 спецификация (JSON):** [http://localhost:9998/api/v3/api-docs](http://localhost:9998/api/v3/api-docs)

### Подробная документация в репозитории:
- 📑 [**Реестр эндпоинтов REST API**](docs/api_endpoints.md) — детальное описание всех 38 методов контроллеров, параметров и структур ответов.
- 📑 [**Справочник WSDL LanBilling**](docs/wsdl_lanbilling_api.md) — карта всех 750 операций SOAP API биллинга.

---

## 📁 Структура проекта

```text
side_billing/
├── docs/                                  # WSDL схема и документация API
│   ├── api_endpoints.md                   # Спецификация REST эндпоинтов
│   ├── wsdl_lanbilling_api.md             # Справочник 750 операций SOAP LanBilling
│   └── wsdl.xml                           # Исходная WSDL схема
├── src/main/
│   ├── kotlin/com/impuls/billing/side_billing/
│   │   ├── config/                        # Swagger/OpenAPI, логирование и фильтры
│   │   ├── exception/                     # Глобальный обработчик ошибок (GlobalExceptionHandler)
│   │   ├── model/                         # DTO запросов и ответов
│   │   ├── out_controller/                # REST контроллеры (auth, account, home, payment, block, post)
│   │   ├── repository/                    # Сервисный слой бизнес-логики
│   │   ├── security/                      # JWT сервис, фильтр и настройки Spring Security
│   │   ├── soap/                          # JAX-WS CXF клиент (LanBillingClient, SoapClientConfig)
│   │   └── utils/                         # Мапперы типизированных DTO и утилиты
│   └── resources/                         # Конфигурационные файлы application*.properties
├── build.gradle.kts                       # Конфигурация сборки Gradle, плагин CXF Codegen
└── .env.example                           # Шаблон переменных окружения
```

---

## 🔒 Безопасность

1. Доступ к защищенным методам API осуществляется передачей токена в заголовке:
   ```http
   Authorization: Bearer <ACCESS_TOKEN>
   ```
2. Токен выдается эндпоинтом `POST /api/auth/login`.
3. Публичные маршруты (не требуют токена): `/api/auth/**`, `/api/swagger-ui/**`, `/api/v3/api-docs/**`.
4. Внутренний пароль учетной записи биллинга маскируется в ответах API.
