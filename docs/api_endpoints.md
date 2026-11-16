# Документация REST API шлюза Side Billing

Шлюз **Side Billing** предоставляет REST API для личного кабинета абонента и сторонних интеграций поверх SOAP API LANBilling 2.0 (порт `Api3PortType`).

---

## 1. Базовые параметры сервиса

- **Базовый URL:** `http://<host>:9998/api`
- **Интерактивная документация Swagger UI:** `http://<host>:9998/api/swagger-ui.html`
- **Спецификация OpenAPI v3:** `http://<host>:9998/api/v3/api-docs`

---

## 2. Безопасность и авторизация

- Архитектура сервиса **100% Stateless** (без использования реляционной БД).
- Для аутентификации используется JWT Bearer токен:
  ```http
  Authorization: Bearer <ACCESS_TOKEN>
  ```
- Внутри JWT токена надежно зашифрован и подписан идентификатор активной SOAP-сессии LanBilling (`sid`), передаваемый шлюзом в биллинг при каждом защищенном вызове.
- **Публичные эндпоинты (не требуют токена):**
  - `/api/auth/login`
  - `/api/auth/recoverPassword`
  - `/api/auth/updatePasswordByCode`
  - `/api/auth/is_valid`
  - `/api/swagger-ui/**`, `/api/v3/api-docs/**`

---

## 3. Реестр эндпоинтов

### 3.1. Аутентификация (`/api/auth`)

| Метод | Путь | Описание | Авторизация | Параметры / Тело запроса | Ответ |
| :--- | :--- | :--- | :---: | :--- | :--- |
| `POST` | `/api/auth/login` | Вход по логину и паролю абонента, генерация JWT | Публичный | JSON: `{"name": "...", "password": "..."}` *(поддерживает алиасы `"login"`, `"username"`)* | `TokenDto`: `{"token": "...", "id": 12345}` |
| `POST` | `/api/auth/recoverPassword` | Запрос восстановления пароля (отправка SMS/Email) | Публичный | JSON: `{"transport": 1, "login": "...", "sync": false, "simpleRecover": false, "operId": 0}` | `RecoverPassword`: `{"recordId": 1, "destination": "..."}` |
| `POST` | `/api/auth/updatePasswordByCode` | Установка нового пароля по коду подтверждения | Публичный | JSON: `{"login": "...", "password": "...", "code": "..."}` | `ResultSuccessDto`: `{"success": true}` |
| `GET` | `/api/auth/is_valid` | Проверка валидности и срока действия JWT токена | Публичный | Query: `token` (String, required) | `ValidateToken`: `{"valid": true}` |

---

### 3.2. Учетная запись и профиль (`/api/account`)

| Метод | Путь | Описание | Авторизация | Параметры / Тело запроса | Ответ |
| :--- | :--- | :--- | :---: | :--- | :--- |
| `GET` | `/api/account/` | Данные текущего авторизованного пользователя (баланс, договоры, учетные записи) | Bearer JWT | — | `ClientAccount` |
| `GET` | `/api/account/info` | Полная карточка клиента (персональные данные, контакты, реквизиты) | Bearer JWT | — | `ClientInfoResponse` |
| `POST` | `/api/account/clientMobilePhone` | Изменение мобильного телефона абонента | Bearer JWT | JSON: `{"uid": 12345, "phone": "+79991234567"}` | `ResultSuccessDto`: `{"success": true}` |
| `POST` | `/api/account/clientEmail` | Изменение email адреса абонента | Bearer JWT | JSON: `{"uid": 12345, "email": "user@example.com"}` | `ResultSuccessDto`: `{"success": true}` |
| `POST` | `/api/account/validationClientMobileNumber` | Запрос проверочного кода на мобильный телефон | Bearer JWT | — | `ValidationDto`: `{"id": 1001, "message": "..."}` |
| `POST` | `/api/account/validationClientEmail` | Запрос проверочного кода на email | Bearer JWT | — | `ValidationDto`: `{"id": 1002, "message": "..."}` |
| `POST` | `/api/account/confirmationClientMobileNumber` | Подтверждение номера телефона по коду из SMS | Bearer JWT | JSON: `{"id": 1001, "code": "1234"}` | `ResultSuccessDto`: `{"success": true}` |
| `POST` | `/api/account/confirmationClientEmail` | Подтверждение email по коду | Bearer JWT | JSON: `{"id": 1002, "code": "5678"}` | `ResultSuccessDto`: `{"success": true}` |
| `POST` | `/api/account/password` | Смена пароля личного кабинета | Bearer JWT | JSON: `{"oldPassword": "...", "newPassword": "..."}` | `ResultSuccessDto`: `{"success": true}` |
| `GET` | `/api/account/vgroup` | Список учетных записей (Vgroups) по договору или ID | Bearer JWT | Query: `agrmid` (Long, optional), `vgid` (Long, optional) | `List<ClientVgroupFullResponse>` |
| `GET` | `/api/account/vgroupPassword` | Получение пароля учетной записи (для PPPoE/VPN/IPoE) | Bearer JWT | Query: `vgid` (Long, required) | `PasswordResponse`: `{"password": "..."}` |
| `GET` | `/api/account/notices` | Настройки уведомлений пользователя (SMS, Email, Telegram) | Bearer JWT | Query: `userid` (Long, required) | `AccountNoticeResponse` |
| `POST` | `/api/account/notices` | Сохранение настроек уведомлений пользователя | Bearer JWT | JSON: `AccountNoticeResponse` | `Long` (ID записи) |
| `DELETE` | `/api/account/logout` | Выход из системы (инвалидация сессии в LanBilling) | Bearer JWT | — | `ResultSuccessDto`: `{"success": true}` |

---

### 3.3. Домашний интернет, тарифы и услуги (`/api/home`)

| Метод | Путь | Описание | Авторизация | Параметры / Тело запроса | Ответ |
| :--- | :--- | :--- | :---: | :--- | :--- |
| `GET` | `/api/home/tariff` | Справочник тарифов (сетка тарифов по группам/модулям) | Bearer JWT | Query: `groupid`, `grouptarid`, `groupmoduleid`, `tarid` | `List<TarifsStaffDto>` |
| `GET` | `/api/home/tariff/history` | История назначений тарифов по учетной записи | Bearer JWT | Query: `vgid` (Long, optional) | `List<TarifsHistoryDto>` |
| `GET` | `/api/home/tariff/{id}` | Полное описание и параметры конкретного тарифа | Bearer JWT | Path: `id` (Long, required) | `TarifFullDto` |
| `POST` | `/api/home/tariff/rasp` | Запланировать смену тарифа со следующего расчетного периода | Bearer JWT | JSON: `{"vgId": 101, "tarIdNew": 202}` | `Long` (ID расписания) |
| `DELETE` | `/api/home/tariff/rasp/{id}` | Отменить запланированную смену тарифа | Bearer JWT | Path: `id` (Long, required) | `ResultSuccessDto`: `{"success": true}` |
| `GET` | `/api/home/mac` | Получение привязанного MAC-адреса оборудования | Bearer JWT | Query: `id` (Long, required — vgId) | `List<MacStaffDto>` |
| `POST` | `/api/home/mac` | Установка / сброс / перепривязка MAC-адреса | Bearer JWT | JSON: `{"id": 101, "mac": "AA:BB:CC:DD:EE:FF"}` | `ResultSuccessDto`: `{"success": true}` |
| `GET` | `/api/home/availableService` | Список доступных для подключения дополнительных услуг | Bearer JWT | Query: `vgId` (Long, required) | `List<AvailableServiceDto>` |
| `GET` | `/api/home/service` | Список активных подключенных дополнительных услуг (Usbox) | Bearer JWT | Query: `vgId` (Long, required) | `List<UsboxServiceFull>` |
| `POST` | `/api/home/service` | Подключение дополнительной услуги | Bearer JWT | JSON: `{"vgId": 101, "tarId": 202, "catIdx": 1, "mui": 0, "notusBox": 0}` | `ResultSuccessLongDto`: `{"success": true, "id": 303}` |
| `DELETE` | `/api/home/service/{id}` | Отключение дополнительной услуги | Bearer JWT | Path: `id` (Long, required — servId) | `ResultSuccessDto`: `{"success": true}` |
| `GET` | `/api/home/short_statistics` | Краткая статистика потребления трафика за период | Bearer JWT | Query: `vgId`, `repDetail`, `dtFrom`, `dtTo` | `List<ClientShortStat>` |
| `GET` | `/api/home/tarifStatistics` | Статистика потребления трафика в разрезе тарифа | Bearer JWT | Query: `vgId`, `dtFrom`, `dtTo` | `List<ClientTarifStat>` |
| `GET` | `/api/home/ipStatistics` | Детализированная статистика трафика по IP-сессиям | Bearer JWT | Query: `vgId`, `dtFrom`, `dtTo` | `List<ClientIpStat>` |

---

### 3.4. Платежи и финансы (`/api/payment`)

| Метод | Путь | Описание | Авторизация | Параметры / Тело запроса | Ответ |
| :--- | :--- | :--- | :---: | :--- | :--- |
| `GET` | `/api/payment/ppSettings` | Параметры и условия активации «Обещанного платежа» | Bearer JWT | Query: `agrm_id` (Long, required) | `PPSettingsDto` (макс. сумма, комиссия, дни) |
| `GET` | `/api/payment/recommended` | Рекомендуемая сумма пополнения для продления услуг | Bearer JWT | Query: `agrm_id` (Long, required), `mode` (Long, optional) | `RecommendedPaymentDto`: `{"amount": 500.0}` |
| `GET` | `/api/payment/promisePayment` | Список оформленных абонентом обещанных платежей | Bearer JWT | — | `List<PromisePaymentDto>` |
| `POST` | `/api/payment/promisePayment` | Оформление обещанного (доверительного) платежа | Bearer JWT | JSON: `{"agreementId": 101, "sum": 500.0}` | `ResultSuccessDto`: `{"success": true}` |
| `GET` | `/api/payment/payment` | История поступивших платежей и пополнений баланса | Bearer JWT | Query: `agrmnum` (String), `dtfrom` (String), `dtto` (String) | `List<ClientPayment>` |
| `GET` | `/api/payment/order` | Список выставленных счетов и квитанций на оплату | Bearer JWT | Query: `payed` (Long, 0/1), `dtfrom` (String), `dtto` (String) | `List<OrderResponse>` |
| `GET` | `/api/payment/debits` | Детализация списаний за услуги и абонентской платы | Bearer JWT | Query: `vgid`, `tarifid`, `dtfrom`, `dtto`, `agrmid` *(или `armgid`)* | `List<DebitDto>` |

---

### 3.5. Добровольные блокировки (`/api/block`)

| Метод | Путь | Описание | Авторизация | Параметры / Тело запроса | Ответ |
| :--- | :--- | :--- | :---: | :--- | :--- |
| `GET` | `/api/block/rasp` | Список запланированных и активных блокировок | Bearer JWT | Query: `vgid` (Long, required), `date_from`, `date_to` | `List<BlockRaspResponse>` |
| `POST` | `/api/block/rasp` | Установка добровольной блокировки учетной записи | Bearer JWT | JSON: `{"vgId": 101, "agrmId": 202, "changeTime": "2026-11-20 00:00:00", "blkReq": 1}` | `ResultSuccessLongDto`: `{"success": true, "id": 505}` |

---

### 3.6. Уведомления и рассылки (`/api/post`)

| Метод | Путь | Описание | Авторизация | Параметры / Тело запроса | Ответ |
| :--- | :--- | :--- | :---: | :--- | :--- |
| `GET` | `/api/post` | Список сообщений, новостей и уведомлений для клиента | Bearer JWT | Query: `date_from` (String, optional, формат `yyyy-MM-dd`) | `List<SharedPostResponse>` |

---

## 4. Коды ответов и формат ошибок

Все ответы возвращаются в формате `application/json`.

В случае ошибки возвращается унифицированное тело:
```json
{
  "message": "Описание ошибки (например: Invalid login or password)"
}
```

### Стандартные HTTP-коды:
- `200 OK` — Успешное выполнение запроса.
- `400 Bad Request` — Ошибка валидации параметров запроса или бизнес-ошибка LanBilling.
- `401 Unauthorized` — Неверный логин/пароль, отсутствие или истечение JWT-токена, завершение сессии в биллинге.
- `502 Bad Gateway` — Сетевая ошибка взаимодействия с сервером LanBilling.
- `504 Gateway Timeout` — Истекло время ожидания ответа от сервера LanBilling.
