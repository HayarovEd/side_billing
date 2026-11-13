# Спецификация LanBilling SOAP API (`api3`)

> Документация сгенерирована на основе WSDL-схемы биллинга LanBilling (`urn:api3`).
> Предназначена для перехода на автоматическую генерацию клиента (**Вариант Б**) и рефакторинга текущего сервиса `side_billing`.

## Общая информация о протоколе
- **Target Namespace:** `urn:api3`
- **Стиль SOAP:** `Document / Literal`
- **Транспорт:** `HTTP POST`
- **Content-Type:** `text/xml; charset=utf-8`
- **SOAPAction:** `""` (пустой)
- **Авторизация сессии:** HTTP-заголовок `Cookie` (содержит session ID, полученный в ответе на `ClientLogin` через `Set-Cookie`).
- **Всего операций в WSDL:** 750 операций.

---

## Быстрая навигация по используемым разделам
- [1. Аутентификация и управление сессией](#1-аутентификация-и-управление-сессией)
- [2. Учетная запись и персональные данные](#2-учетная-запись-и-персональные-данные)
- [3. Учетные записи услуг (Vgroups) и оборудование](#3-учетные-записи-услуг-vgroups-и-оборудование)
- [4. Тарифы и управление расписанием](#4-тарифы-и-управление-расписанием)
- [5. Дополнительные услуги (Сервисы / Usbox)](#5-дополнительные-услуги-сервисы-usbox)
- [6. Финансы, платежи и обещанный платеж](#6-финансы-платежи-и-обещанный-платеж)
- [7. Блокировки (Добровольная / Административная)](#7-блокировки-добровольная-административная)
- [8. Уведомления и рассылки](#8-уведомления-и-рассылки)
- [9. Статистика потребления трафика](#9-статистика-потребления-трафика)
- [Полный алфавитный реестр всех 750 операций](#полный-алфавитный-реестр-всех-750-операций)
- [Инструкция: Автогенерация клиента в Gradle (Вариант Б)](#инструкция-автогенерация-клиента-в-gradle-вариант-б)

---

## 1. Аутентификация и управление сессией

### Операция `ClientLogin`
**Назначение:** Аутентификация клиента по логину и паролю. Возвращает ID пользователя и устанавливает сессионный Cookie.

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `operid` | `long` | Нет | `0` | |
| `login` | `string` | Да | - | |
| `pass` | `string` | Да | - | |
| `ip` | `string` | Нет | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `soapClientLogin` | 1..unbounded | |

**Детализация структуры `soapClientLogin`:**
| Поле структуры | Тип | Обязательность | По умолчанию |
| :--- | :--- | :--- | :--- |
| `uid` | `long` | Да | - |
| `timelastlogin` | `string` | Да | - |

---

### Операция `Logout`
**Назначение:** Завершение сессии клиента в биллинге.

#### Входящий запрос (Request)
*Параметры отсутствуют или передаются через пустой тег.*

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `long` | 1..1 | |

---

### Операция `updClientPass`
**Назначение:** Смена пароля личного кабинета абонента.

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `vgid` | `long` | Нет | - | |
| `oldpass` | `string` | Да | - | |
| `newpass` | `string` | Да | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `long` | 1..1 | |

---

### Операция `recoverPassword`
**Назначение:** Запрос на восстановление пароля (через SMS или Email).

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `transport` | `int` | Да | - | |
| `login` | `string` | Да | - | |
| `sync` | `boolean` | Да | - | |
| `simplerecover` | `boolean` | Нет | `false` | |
| `operid` | `int` | Нет | `0` | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `soapRecoverPassword` | 1..unbounded | |

**Детализация структуры `soapRecoverPassword`:**
| Поле структуры | Тип | Обязательность | По умолчанию |
| :--- | :--- | :--- | :--- |
| `recordid` | `long` | Нет | `0` |
| `destination` | `string` | Нет | - |

---

### Операция `updatePasswordByCode`
**Назначение:** Подтверждение восстановления пароля по одноразовому коду.

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `val` | `soapUpdatePasswordByCode` | Да | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `string` | 1..1 | |

---

## 2. Учетная запись и персональные данные

### Операция `getClientAccount`
**Назначение:** Получение полной информации об аккаунте: реквизиты, договоры, балансы, статус.

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `flt` | `soapGetAccountFilter` | Нет | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `soapAccountFull` | 1..unbounded | |

**Детализация структуры `soapAccountFull`:**
| Поле структуры | Тип | Обязательность | По умолчанию |
| :--- | :--- | :--- | :--- |
| `application` | `long` | Нет | - |
| `billdeliveryname` | `string` | Нет | - |
| `account` | `soapAccount` | Да | - |
| `delfromgroups` | `long` | Нет | - |
| `usergroups` | `soapUsergroupFull` | Нет | - |
| `addresses` | `soapAddressBrief` | Нет | - |
| `agreements` | `soapAgreement` | Нет | - |
| `addons` | `soapAccountAddon` | Нет | - |

---

### Операция `getClientInfo`
**Назначение:** Получение контактных данных клиента (телефон, email, статус подтверждения контактов).

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `flt` | `soapFilter` | Да | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `soapGetClientInfo` | 1..unbounded | |

**Детализация структуры `soapGetClientInfo`:**
| Поле структуры | Тип | Обязательность | По умолчанию |
| :--- | :--- | :--- | :--- |
| `mobileisconfirmed` | `boolean` | Нет | - |
| `emailisconfirmed` | `boolean` | Нет | - |
| `offerisaccepted` | `boolean` | Нет | - |
| `passistemporary` | `boolean` | Нет | - |
| `triggercontactdatareminder` | `boolean` | Нет | - |
| `email` | `string` | Нет | - |
| `pass` | `string` | Нет | - |
| `phone` | `string` | Нет | - |
| `kontperson` | `string` | Нет | - |
| `mobile` | `string` | Нет | - |

---

### Операция `setClientInfo`
**Назначение:** Обновление контактных данных (телефона, email, имени).

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `val` | `soapClientInfo` | Да | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `long` | 1..1 | |

---

### Операция `validateClientMobileNumber`
**Назначение:** Инициация верификации номера мобильного телефона (отправка SMS).

#### Входящий запрос (Request)
*Параметры отсутствуют или передаются через пустой тег.*

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `long` | 1..1 | |

---

### Операция `confirmValidateClientMobileNumber`
**Назначение:** Подтверждение номера телефона проверочным кодом из SMS.

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `id` | `long` | Да | - | |
| `code` | `string` | Да | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `long` | 1..1 | |

---

### Операция `validateClientEmail`
**Назначение:** Инициация верификации адреса электронной почты.

#### Входящий запрос (Request)
*Параметры отсутствуют или передаются через пустой тег.*

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `long` | 1..1 | |

---

### Операция `confirmValidateClientEmail`
**Назначение:** Подтверждение email проверочным кодом.

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `id` | `long` | Да | - | |
| `code` | `string` | Да | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `long` | 1..1 | |

---

## 3. Учетные записи услуг (Vgroups) и оборудование

### Операция `getClientVgroups`
**Назначение:** Получение списка учетных записей услуг (интернет/IPTV/телефония) по договору.

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `flt` | `soapFilter` | Нет | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `soapClientVgroupFull` | 1..unbounded | |

**Детализация структуры `soapClientVgroupFull`:**
| Поле структуры | Тип | Обязательность | По умолчанию |
| :--- | :--- | :--- | :--- |
| `vgroup` | `soapClientVgroup` | Да | - |
| `tarrasp` | `soapTarifsRasp` | Нет | - |
| `staff` | `soapStaff` | Нет | - |
| `telstaff` | `soapTelStaff` | Нет | - |
| `macstaff` | `soapMacStaff` | Нет | - |
| `tarstaff` | `soapTarifsStaff` | Нет | - |
| `turboshape` | `soapTurboShape` | Нет | - |
| `addons` | `soapVgroupAddon` | Нет | - |
| `blockrasp` | `soapBlockRasp` | Нет | - |
| `addresses` | `soapAddressBrief` | Нет | - |

---

### Операция `getClientVgroupPassword`
**Назначение:** Получение пароля к учетной записи подключения (PPPoE / IPoE / VPN).

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `vgid` | `long` | Да | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `string` | 1..1 | |

---

### Операция `getMacStaff`
**Назначение:** Получение списка привязанных MAC-адресов оборудования к учетной записи.

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `flt` | `soapFilter` | Нет | - | |
| `id` | `long` | Нет | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `soapMacStaff` | 1..unbounded | |

**Детализация структуры `soapMacStaff`:**
| Поле структуры | Тип | Обязательность | По умолчанию |
| :--- | :--- | :--- | :--- |
| `macid` | `long` | Нет | `0` |
| `recordid` | `long` | Нет | `0` |
| `vgid` | `long` | Да | - |
| `segment` | `string` | Нет | - |
| `mac` | `string` | Да | - |

---

### Операция `insupdMacStaff`
**Назначение:** Добавление или обновление привязки MAC-адреса оборудования.

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `isInsert` | `long` | Нет | `1` | |
| `val` | `soapMacStaff` | Да | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `long` | 1..1 | |

---

## 4. Тарифы и управление расписанием

### Операция `getTarifsStaff`
**Назначение:** Получение каталога доступных тарифов для учетной записи.

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `filter` | `soapTarifsStaff` | Да | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `soapTarifsStaff` | 1..unbounded | |

**Детализация структуры `soapTarifsStaff`:**
| Поле структуры | Тип | Обязательность | По умолчанию |
| :--- | :--- | :--- | :--- |
| `groupid` | `long` | Да | - |
| `grouptarid` | `long` | Да | - |
| `groupmoduleid` | `long` | Да | - |
| `tarid` | `long` | Да | - |
| `tartype` | `long` | Нет | `0` |
| `tarcurid` | `long` | Нет | `0` |
| `shape` | `long` | Нет | `0` |
| `rent` | `double` | Нет | `0` |
| `tarname` | `string` | Нет | - |
| `tarsymbol` | `string` | Нет | - |
| `tardescrfull` | `string` | Нет | - |

---

### Операция `getTarifsHistory`
**Назначение:** История изменения тарифов абонента.

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `flt` | `soapFilter` | Нет | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `soapTarifsHistory` | 1..unbounded | |

**Детализация структуры `soapTarifsHistory`:**
| Поле структуры | Тип | Обязательность | По умолчанию |
| :--- | :--- | :--- | :--- |
| `tartype` | `short` | Нет | `-1` |
| `recordid` | `long` | Нет | `0` |
| `vgid` | `long` | Нет | `0` |
| `agrmid` | `long` | Нет | `0` |
| `uid` | `long` | Нет | `0` |
| `id` | `long` | Нет | `0` |
| `taridnew` | `long` | Нет | `0` |
| `taridold` | `long` | Нет | `0` |
| `requestby` | `string` | Нет | - |
| `rasptime` | `string` | Нет | - |
| `changed` | `string` | Нет | - |
| `vglogin` | `string` | Нет | - |
| `agrmnum` | `string` | Нет | - |
| `code` | `string` | Нет | - |
| `accname` | `string` | Нет | - |
| `agentname` | `string` | Нет | - |
| `tarnewname` | `string` | Нет | - |
| `taroldname` | `string` | Нет | - |
| `mgrname` | `string` | Нет | - |
| `mgrdescr` | `string` | Нет | - |
| `currsymbol` | `string` | Нет | - |

---

### Операция `getTarif`
**Назначение:** Детальная информация о конкретном тарифе.

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `id` | `long` | Да | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `soapTarifFull` | 1..unbounded | |

**Детализация структуры `soapTarifFull`:**
| Поле структуры | Тип | Обязательность | По умолчанию |
| :--- | :--- | :--- | :--- |
| `tarif` | `soapTarif` | Да | - |
| `sizeshapes` | `soapSizeShape` | Нет | - |
| `timeshapes` | `soapTimeShape` | Нет | - |
| `addons` | `soapTariffsAddon` | Нет | - |
| `settingsrentasservice` | `soapTariffsSettingsRentAsService` | Да | - |

---

### Операция `insClientTarifsRasp`
**Назначение:** Запланировать переход на новый тариф (смена тарифа по расписанию).

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `val` | `soapTarifsRasp` | Да | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `long` | 1..1 | |

---

### Операция `delClientTarifsRasp`
**Назначение:** Отмена запланированной смены тарифа.

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `id` | `long` | Да | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `long` | 1..1 | |

---

## 5. Дополнительные услуги (Сервисы / Usbox)

### Операция `getAvailableServicesForVgroup`
**Назначение:** Список доступных к подключению дополнительных услуг.

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `flt` | `AvailableServicesFilter` | Да | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `AvailableServices` | 1..unbounded | |

**Детализация структуры `AvailableServices`:**
| Поле структуры | Тип | Обязательность | По умолчанию |
| :--- | :--- | :--- | :--- |
| `tarid` | `long` | Нет | `0` |
| `catidx` | `long` | Нет | `0` |
| `isusbox` | `boolean` | Нет | `false` |
| `isunique` | `boolean` | Нет | `false` |
| `descr` | `string` | Нет | - |
| `saledictservtype` | `short` | Нет | `-1` |
| `promoperiod` | `long` | Нет | `-1` |
| `uuid` | `string` | Нет | - |
| `servicetype` | `short` | Нет | `0` |
| `price` | `double` | Нет | `0` |
| `includeabove` | `double` | Нет | `0` |
| `rentperiod` | `short` | Нет | `0` |
| `rentperiodmonth` | `short` | Нет | `0` |

---

### Операция `getUsboxServices`
**Назначение:** Список подключенных услуг и подписок абонента.

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `flt` | `soapFilter` | Нет | - | |
| `ord` | `soapOrderby` | Нет | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `soapUsboxServiceFull` | 1..unbounded | |

**Детализация структуры `soapUsboxServiceFull`:**
| Поле структуры | Тип | Обязательность | По умолчанию |
| :--- | :--- | :--- | :--- |
| `service` | `soapUsboxService` | Да | - |
| `technicalservice` | `short` | Нет | `-1` |
| `isassignedip` | `long` | Нет | `0` |
| `isassignedphone` | `long` | Нет | `0` |
| `used` | `long` | Нет | `0` |
| `agrmid` | `long` | Нет | `0` |
| `uid` | `long` | Нет | `0` |
| `common` | `long` | Нет | `0` |
| `smartcardid` | `long` | Нет | `0` |
| `available` | `long` | Нет | `0` |
| `availableformanager` | `long` | Нет | `1` |
| `autoassign` | `long` | Нет | `0` |
| `servicecatid` | `long` | Нет | `0` |
| `isunique` | `long` | Нет | `0` |
| `catabove` | `double` | Нет | `0` |
| `includeabove` | `double` | Нет | `0` |
| `totalamount` | `double` | Нет | `0` |
| `agrmnumber` | `string` | Нет | - |
| `username` | `string` | Нет | - |
| `vglogin` | `string` | Нет | - |
| `tardescr` | `string` | Нет | - |
| `catdescr` | `string` | Нет | - |
| `personname` | `string` | Нет | - |
| `smartcardserial` | `string` | Нет | - |
| `accondate` | `string` | Нет | - |
| `symbol` | `string` | Нет | - |
| `uuid` | `string` | Нет | - |
| `currentmodifier` | `soapCurrentModifier` | Нет | - |
| `tartype` | `short` | Нет | `-1` |
| `isassignedpromisepayment` | `boolean` | Нет | `false` |

---

### Операция `insupdClientUsboxService`
**Назначение:** Подключение или изменение параметров дополнительной услуги.

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `isInsert` | `long` | Нет | `1` | |
| `val` | `soapUsboxService` | Да | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `long` | 1..1 | |

---

### Операция `delUsboxService`
**Назначение:** Отключение дополнительной услуги.

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `id` | `long` | Да | - | |
| `notusbox` | `long` | Нет | `0` | |
| `force` | `long` | Нет | `0` | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `long` | 1..1 | |

---

### Операция `getRentAmount`
**Назначение:** Расчет стоимости аренды оборудования или услуги.

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `vgid` | `long` | Да | - | |
| `tarid` | `long` | Да | - | |
| `timefrom` | `string` | Нет | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `double` | 1..1 | |

---

## 6. Финансы, платежи и обещанный платеж

### Операция `getClientPPSettings`
**Назначение:** Настройки обещанного платежа для договора (минимальная/максимальная сумма, комиссия, доступность).

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `agrm` | `long` | Да | - | |
| `dateto` | `string` | Нет | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `soapPPSettings` | 1..unbounded | |

**Детализация структуры `soapPPSettings`:**
| Поле структуры | Тип | Обязательность | По умолчанию |
| :--- | :--- | :--- | :--- |
| `promiserent` | `long` | Да | - |
| `promisetill` | `long` | Да | - |
| `promisemin` | `double` | Да | - |
| `promisemax` | `double` | Да | - |
| `promiselimit` | `double` | Да | - |
| `promiseondays` | `long` | Да | - |
| `promiseblockdays` | `long` | Да | - |
| `promiseavailable` | `long` | Да | - |
| `promisefee` | `double` | Да | - |

---

### Операция `getRecommendedPayment`
**Назначение:** Расчет рекомендуемой суммы платежа для непрерывного оказания услуг.

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `id` | `long` | Да | - | |
| `mode` | `long` | Нет | `0` | |
| `date` | `string` | Нет | - | |
| `considerinstallment` | `boolean` | Нет | `false` | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `double` | 1..1 | |

---

### Операция `ClientPromisePayment`
**Назначение:** Активация обещанного (доверительного) платежа.

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `agrm` | `long` | Да | - | |
| `summ` | `double` | Да | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `long` | 1..1 | |

---

### Операция `getClientPromisePayments`
**Назначение:** История взятых обещанных платежей.

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `flt` | `soapFilter` | Нет | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `soapPromisePayment` | 1..unbounded | |

**Детализация структуры `soapPromisePayment`:**
| Поле структуры | Тип | Обязательность | По умолчанию |
| :--- | :--- | :--- | :--- |
| `recordid` | `long` | Нет | `0` |
| `agrmid` | `long` | Нет | `0` |
| `payid` | `long` | Нет | `0` |
| `currid` | `long` | Нет | `0` |
| `debt` | `double` | Нет | `0` |
| `amount` | `double` | Нет | `0` |
| `promdate` | `string` | Нет | - |
| `promtill` | `string` | Нет | - |
| `symbol` | `string` | Нет | - |
| `username` | `string` | Нет | - |
| `agrmnum` | `string` | Нет | - |
| `status` | `short` | Нет | `-1` |
| `modperson` | `long` | Нет | `-2` |
| `cancelperson` | `long` | Нет | `-1` |
| `modfio` | `string` | Нет | - |
| `cancelfio` | `string` | Нет | - |
| `fee` | `double` | Нет | `0` |

---

### Операция `getClientPayments`
**Назначение:** История фактических платежей абонента за период.

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `flt` | `soapFilter` | Нет | - | |
| `ord` | `soapOrderby` | Нет | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `soapPaymentFull` | 1..unbounded | |

**Детализация структуры `soapPaymentFull`:**
| Поле структуры | Тип | Обязательность | По умолчанию |
| :--- | :--- | :--- | :--- |
| `pay` | `soapPayment` | Да | - |
| `bsodoc` | `long` | Нет | `0` |
| `timestamp` | `long` | Нет | `0` |
| `localtimestamp` | `long` | Нет | `0` |
| `amountcurr` | `double` | Нет | `0` |
| `ordernum` | `string` | Нет | - |
| `uid` | `long` | Нет | `0` |
| `operid` | `long` | Нет | `0` |
| `cardnumber` | `long` | Нет | `0` |
| `currsymb` | `string` | Нет | - |
| `uname` | `string` | Нет | - |
| `agrm` | `string` | Нет | - |
| `mgr` | `string` | Нет | - |
| `mgrdescr` | `string` | Нет | - |
| `mgrlogin` | `string` | Нет | - |
| `opername` | `string` | Нет | - |
| `login` | `string` | Нет | - |
| `iseps` | `boolean` | Нет | `false` |

---

### Операция `getClientOrders`
**Назначение:** Список счетов и начислений за период.

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `flt` | `soapFilter` | Нет | - | |
| `ord` | `soapOrderby` | Нет | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `soapOrder` | 1..unbounded | |

**Детализация структуры `soapOrder`:**
| Поле структуры | Тип | Обязательность | По умолчанию |
| :--- | :--- | :--- | :--- |
| `orderid` | `long` | Да | - |
| `agrmid` | `long` | Да | - |
| `operid` | `long` | Да | - |
| `docid` | `long` | Да | - |
| `ordernum` | `string` | Да | - |
| `resumm` | `double` | Да | - |
| `currsumm` | `double` | Да | - |
| `taxsumm` | `double` | Да | - |
| `uid` | `long` | Нет | `0` |
| `docpayable` | `long` | Нет | `0` |
| `agrmarchive` | `long` | Да | - |
| `period` | `string` | Да | - |
| `orderdate` | `string` | Да | - |
| `creationdate` | `string` | Да | - |
| `paydate` | `string` | Да | - |
| `filename` | `string` | Нет | - |
| `docname` | `string` | Нет | - |
| `docsavepath` | `string` | Нет | - |
| `doctemplate` | `string` | Нет | - |
| `docuploadext` | `string` | Нет | - |
| `opername` | `string` | Нет | - |
| `agrmnum` | `string` | Нет | - |
| `username` | `string` | Нет | - |
| `payreceipt` | `string` | Нет | - |
| `currsymbol` | `string` | Нет | - |
| `ordernumtemplate` | `string` | Нет | - |
| `billdelivery` | `long` | Нет | `-1` |
| `billdeliveryname` | `string` | Нет | - |
| `exportstate` | `short` | Нет | `-1` |
| `exportdate` | `string` | Нет | - |
| ... | ... | *(всего 31 полей)* | |

---

## 7. Блокировки (Добровольная / Административная)

### Операция `getBlockRasp`
**Назначение:** Получение расписания и истории блокировок учетной записи.

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `flt` | `soapFilter` | Нет | - | |
| `ord` | `soapOrderby` | Нет | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `soapBlockRasp` | 1..unbounded | |

**Детализация структуры `soapBlockRasp`:**
| Поле структуры | Тип | Обязательность | По умолчанию |
| :--- | :--- | :--- | :--- |
| `recordid` | `long` | Нет | `0` |
| `vgid` | `long` | Нет | `0` |
| `groupid` | `long` | Нет | `0` |
| `blkreq` | `long` | Нет | `0` |
| `id` | `long` | Нет | `0` |
| `ishistory` | `long` | Нет | `0` |
| `isfreewill` | `long` | Нет | `0` |
| `requestby` | `long` | Нет | `-1` |
| `unblockedby` | `long` | Нет | `-1` |
| `agrmid` | `long` | Нет | `0` |
| `uid` | `long` | Нет | `0` |
| `changetime` | `string` | Нет | - |
| `managerlogin` | `string` | Нет | - |
| `mgrname` | `string` | Нет | - |
| `mgrdescr` | `string` | Нет | - |
| `comment` | `string` | Нет | - |
| `timeto` | `string` | Нет | - |
| `login` | `string` | Нет | - |
| `unblockedmanagerlogin` | `string` | Нет | - |
| `unblockedmanagername` | `string` | Нет | - |
| `unblockedmanagerdescr` | `string` | Нет | - |
| `agrmnum` | `string` | Нет | - |
| `username` | `string` | Нет | - |

---

### Операция `insBlkRasp`
**Назначение:** Установка временной добровольной блокировки учетной записи.

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `val` | `soapBlockRasp` | Да | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `long` | 1..1 | |

---

## 8. Уведомления и рассылки

### Операция `getAccountNotices`
**Назначение:** Настройки каналов уведомлений абонента (SMS, Email, Push).

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `flt` | `soapFilter` | Да | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `soapAccountNotices` | 1..unbounded | |

**Детализация структуры `soapAccountNotices`:**
| Поле структуры | Тип | Обязательность | По умолчанию |
| :--- | :--- | :--- | :--- |
| `uid` | `long` | Нет | `0` |
| `notices` | `soapAccountNotice` | Нет | - |

---

### Операция `setAccountNotices`
**Назначение:** Сохранение настроек каналов уведомлений абонента.

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `an` | `soapAccountNotices` | Да | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `long` | 1..1 | |

---

### Операция `getClientSharedPosts`
**Назначение:** Список информационных сообщений и новостей от провайдера.

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `flt` | `soapFilter` | Нет | - | |
| `ord` | `soapOrderby` | Нет | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `soapSharedPost` | 1..unbounded | |

**Детализация структуры `soapSharedPost`:**
| Поле структуры | Тип | Обязательность | По умолчанию |
| :--- | :--- | :--- | :--- |
| `pid` | `long` | Да | - |
| `postby` | `long` | Нет | `0` |
| `postto` | `long` | Нет | `0` |
| `groupid` | `long` | Нет | `-1` |
| `uid` | `long` | Нет | `0` |
| `posttime` | `string` | Нет | - |
| `text` | `string` | Нет | - |
| `subject` | `string` | Нет | - |
| `senttime` | `string` | Нет | - |
| `status` | `long` | Нет | `-1` |
| `postcatid` | `long` | Нет | `0` |
| `dtfrom` | `string` | Нет | - |
| `dtto` | `string` | Нет | - |
| `emails` | `string` | Нет | - |
| `externurl` | `string` | Нет | - |

---

## 9. Статистика потребления трафика

### Операция `getClientStat`
**Назначение:** Детализация трафика и сессий абонента по периодам (дни, часы, месяцы).

#### Входящий запрос (Request)
| Поле | Тип | Обязательность | По умолчанию | Описание / Примечание |
| :--- | :--- | :--- | :--- | :--- |
| `flt` | `soapFilter` | Нет | - | |
| `ord` | `soapOrderby` | Нет | - | |

#### Ответ (Response)
| Поле | Тип | Кардинальность | Примечание |
| :--- | :--- | :--- | :--- |
| `ret` | `soapStat` | 1..unbounded | |

**Детализация структуры `soapStat`:**
| Поле структуры | Тип | Обязательность | По умолчанию |
| :--- | :--- | :--- | :--- |
| `rows` | `unsignedLong` | Нет | `0` |
| `cols` | `unsignedLong` | Нет | `0` |
| `total` | `unsignedLong` | Нет | `0` |
| `names` | `soapVectorString` | Да | - |
| `datasumm` | `soapVectorString` | Да | - |
| `data` | `soapVectorString` | Нет | - |

---

## Полный алфавитный реестр всех 750 операций

Ниже представлен полный список всех доступных в WSDL методов LanBilling `api3`:

| Операции 1 | Операции 2 | Операции 3 |
| :--- | :--- | :--- |
| `AddressCount` | `ClientLogin` | `ClientPromisePayment` |
| `Count` | `ExternCancelPayment` | `ExternCheckPayment` |
| `ExternPayment` | `Filter2DB` | `FindPhonesInRange` |
| `GetLbsvClientEquipment` | `GetRentedEquipmentServices` | `IPCalc` |
| `IPCalcCount` | `Login` | `Logout` |
| `Payment` | `PromisePayment` | `TestEricssonMWConnection` |
| `UpdateClientContactData` | `actCard` | `actClientCard` |
| `activateHybridEquipment` | `activateSmartCard` | `addAccountToUserGroups` |
| `addDocumentGenTask` | `addGroupVlans` | `addManagerRole` |
| `addManagersGroupsStaff` | `addMasterCategoryItem` | `addSubscribeAccountsToNotify` |
| `addTariffsGroupsStaff` | `addVgroupAccessOverride` | `agrmCancelDebt` |
| `agrmGroupCancelDebt` | `attachHybridEquipment` | `attachTvEquipment` |
| `blkClientVgroup` | `blkVgroup` | `canAcceptUprsPayment` |
| `cancelCloseAgreement` | `cancelGenTask` | `cancelPrePayment` |
| `cancelPromisePayment` | `checkExternAccount` | `checkPaymentTransfer` |
| `cloneCatalog` | `cloneTarif` | `closeAgreement` |
| `closeVgroupAccessOverride` | `confirmPrePayment` | `confirmRecoverPassword` |
| `confirmValidateClientEmail` | `confirmValidateClientMobileNumber` | `createHybridVgroup` |
| `delASCat` | `delAccPhone` | `delAccount` |
| `delAccountFromUserGroups` | `delAccountsAddonsSet` | `delAction` |
| `delActionStaff` | `delActionTariff` | `delAddressArea` |
| `delAddressBuilding` | `delAddressCity` | `delAddressCountry` |
| `delAddressEntrance` | `delAddressFlat` | `delAddressFloor` |
| `delAddressRegion` | `delAddressSettle` | `delAddressStreet` |
| `delAgent` | `delAgreement` | `delAgreementAddon` |
| `delAgreementRenewal` | `delAgreementTemplate` | `delAgreementsAddonsSet` |
| `delAgrmGroup` | `delAgrmGroupStaff` | `delAgrmTemplates` |
| `delAppComment` | `delApplication` | `delApplicationAddr` |
| `delAutonumTemplate` | `delBillDeliveryMethod` | `delBlkRasp` |
| `delBonusMinutePacket` | `delCaptivePortal` | `delCaptivePortalCardSet` |
| `delCardSet` | `delCatItem4Category` | `delCatalog` |
| `delCatalogService` | `delCategoryModifier` | `delCerberMask` |
| `delClientTarifsRasp` | `delCurrency` | `delDHCPOption` |
| `delDHCPOptionsDict` | `delDevGroupOptSet` | `delDevGroupOptStaff` |
| `delDevOptStaff` | `delDevice` | `delDeviceGroup` |
| `delDeviceGroupsMember` | `delDeviceTreeItem` | `delDevicesOptionsSet` |
| `delDictionary` | `delDiscountAddon` | `delDocument` |
| `delEpsAgreementsParams` | `delEquipment` | `delEquipmentModel` |
| `delExternalCharge` | `delExternalModuleOption` | `delGenTask` |
| `delGift` | `delGiftOptionSet` | `delGponPort` |
| `delGrStaff` | `delGroup` | `delGroupVlans` |
| `delGroupedOrder` | `delIPCat` | `delInstallmentsPlan` |
| `delMacStaff` | `delManager` | `delManagerRole` |
| `delManagersGroup` | `delManagersGroupsStaff` | `delMasterCategory` |
| `delMasterCategoryItem` | `delMessengersMessageHistory` | `delMessengersMessageQueue` |
| `delMultitarif` | `delOperTelStaff` | `delOrder` |
| `delPacket` | `delPacketEntry` | `delPacketTariff` |
| `delPassTemplate` | `delPayClass` | `delPeople` |
| `delPeopleAddr` | `delPhoneParser` | `delPhoneReplace` |
| `delPhoneStaff` | `delPort` | `delPortPoliciy` |
| `delPortState` | `delPostman` | `delRadiusAttr` |
| `delRnas` | `delRole` | `delRoleStaff` |
| `delRoleTariff` | `delRoleUserGroup` | `delRoute` |
| `delRouteWeght` | `delSale` | `delSaleDictionaryEntry` |
| `delSbssApplClass` | `delSbssPost` | `delSbssRequestClass` |
| `delSbssStatus` | `delSbssTicket` | `delSearchTemplate` |
| `delSegment` | `delServCategoryPrice` | `delServFunc` |
| `delService` | `delServiceCategory` | `delServiceType` |
| `delSettingsNotice` | `delSharedPost` | `delSharedPostsCategory` |
| `delSmartCard` | `delSmsHistory` | `delSmsQueue` |
| `delSpecialOffer` | `delStaff` | `delSubscribeAccountsToNotify` |
| `delTarCategory` | `delTarCategoryPrice` | `delTarCategoryZoneTel` |
| `delTarif` | `delTariffUserBlock` | `delTariffsAddonsSet` |
| `delTariffsGroup` | `delTariffsGroupsStaff` | `delTarifsRasp` |
| `delTelCat` | `delTelClass` | `delTelClassLimit` |
| `delTrunkStaff` | `delTrusted` | `delUsboxService` |
| `delUserBlockTemplate` | `delUserPacket` | `delUsergroup` |
| `delUsergroupStaff` | `delVgUserBlockSchedule` | `delVgroup` |
| `delVgroupRenewal` | `delVgroupsAddonsSet` | `delVlan` |
| `deleteSessionsRadius` | `detachTvEquipment` | `dhcpAcctStart` |
| `dhcpAcctStop` | `dhcpGetSessions` | `dhcpStartSession` |
| `dhcpStopSession` | `earlyInstallmentRepayment` | `emailIsReady` |
| `exportBalanceTo1C` | `exportDocumentsTo1C` | `genApplications` |
| `genApplications2` | `genCards` | `genOrderGetFile` |
| `genOrders` | `genPreActivatedAccounts` | `genSales` |
| `generatePassword` | `getASCatalog` | `getAccPhones` |
| `getAccount` | `getAccountNotices` | `getAccountSharedPostCategories` |
| `getAccounts` | `getAccountsAddonsSet` | `getAccountsAddonsStaff` |
| `getActions` | `getActionsStaff` | `getActionsTariffs` |
| `getAddressAreas` | `getAddressBuildings` | `getAddressCities` |
| `getAddressCountries` | `getAddressEntrances` | `getAddressFlats` |
| `getAddressFloors` | `getAddressIndexes` | `getAddressMeanings` |
| `getAddressRegions` | `getAddressSettles` | `getAddressStreets` |
| `getAddresses` | `getAgent` | `getAgent2` |
| `getAgentOptions` | `getAgents` | `getAgents2` |
| `getAgentsExt` | `getAgreementAddons` | `getAgreementTemplates` |
| `getAgreements` | `getAgreementsAddonsSet` | `getAgreementsAddonsStaff` |
| `getAgreementsBrief` | `getAgreementsRenewal` | `getAgrmAvailablePayMethods` |
| `getAgrmEquipmentHistory` | `getAgrmGroupStaff` | `getAgrmGroups` |
| `getAppComments` | `getApplications` | `getAutoAgreementExt` |
| `getAutoAgreementNumber` | `getAutonumTemplates` | `getAvailableServicesForVgroup` |
| `getAvailableVgroups` | `getBalanceByActivationCode` | `getBillDeliveryMethods` |
| `getBinLicense` | `getBlockRasp` | `getBonusMinutePackets` |
| `getBonusMinutePacketsIdx` | `getBonusMinutePacketsVg` | `getCASPackages` |
| `getCaptivePortals` | `getCardSet` | `getCardSets` |
| `getCatalog` | `getCatalogFull` | `getCatalogServices` |
| `getCatalogs` | `getCategoryModifiers` | `getCategoryModifiersChangeTariff` |
| `getCerberMasks` | `getCfgSmsPlugin` | `getClientAccount` |
| `getClientActions` | `getClientDocuments` | `getClientInfo` |
| `getClientMessages` | `getClientOrders` | `getClientPPSettings` |
| `getClientPayments` | `getClientPromisePayments` | `getClientServFuncs` |
| `getClientSharedPosts` | `getClientStat` | `getClientVgroupPassword` |
| `getClientVgroups` | `getCreditHistory` | `getCurrencies` |
| `getDHCPOption` | `getDHCPOptionsDict` | `getDHCPOptionsTypes` |
| `getDefaultSmsPlugin` | `getDevGroupOptSet` | `getDevGroupOptStaff` |
| `getDevGroupOptVals` | `getDevOptStaff` | `getDevice` |
| `getDeviceGroups` | `getDevices` | `getDevicesOptionsSet` |
| `getDictOkei` | `getDictOksm` | `getDictOkv` |
| `getDictionaries` | `getDiscountAddons` | `getDiscounts` |
| `getDocuments` | `getEpsAgreementsParams` | `getEquipcardsHistory` |
| `getEquipment` | `getEquipmentHistory` | `getEquipmentModels` |
| `getEquipmentNetworks` | `getEquipmentTypes` | `getExternAccount` |
| `getExternAgreement` | `getExternalCharges` | `getExternalModuleOptions` |
| `getExternalModules` | `getFirstPage` | `getGeneratorsTasks` |
| `getGiftOptions` | `getGiftOptionsSet` | `getGiftTypes` |
| `getGifts` | `getGiftsAssignments` | `getGponPorts` |
| `getGroup` | `getGroupedOrders` | `getGroups` |
| `getHDOptionByName` | `getHDOptions` | `getIPCatalog` |
| `getInfoAboutAccountDataConfirm` | `getInstallments` | `getInstallmentsPlans` |
| `getInstallmentsRemaining` | `getLastPayments` | `getLbinetMode` |
| `getLicense` | `getListSmsPlugins` | `getLoggedUsers` |
| `getMacStaff` | `getManager` | `getManagers` |
| `getManagersGroups` | `getManagersGroupsStaff` | `getManagersRoles` |
| `getMasterCategory` | `getMasterCategoryTelCatalog` | `getMessengersMessageHistory` |
| `getMessengersMessageQueue` | `getMetrics` | `getMultitarifs` |
| `getOperTelStaff` | `getOptionByName` | `getOptions` |
| `getOptions4Device` | `getOrders` | `getPPSettings` |
| `getPabxes` | `getPacket` | `getPacketEntries` |
| `getPacketTariffs` | `getPackets` | `getPassTemplates` |
| `getPayClass` | `getPayClasses` | `getPaycards` |
| `getPayments` | `getPeople` | `getPeopleAddrs` |
| `getPeriodTarifficationService` | `getPhoneFilter` | `getPhoneParsers` |
| `getPhoneReplaces` | `getPhoneSourcesForRecalc` | `getPhoneStaff` |
| `getPortPolicies` | `getPortStates` | `getPorts` |
| `getPostmans` | `getPrePayments` | `getPromisePayments` |
| `getPromisePaymentsServiceSum` | `getRadiusAttr` | `getRadiusAttrs` |
| `getRates` | `getRecalcState` | `getRecommendedPayment` |
| `getRegistries` | `getRegistryPayments` | `getRentAmount` |
| `getRnas` | `getRoleStaff` | `getRoleTariffs` |
| `getRoleUserGroups` | `getRoles` | `getRouteWeights` |
| `getRoutes` | `getSaleDictionaryEntries` | `getSales` |
| `getSbssApplClasses` | `getSbssCrmFiles` | `getSbssKnowledge` |
| `getSbssKnowledges` | `getSbssMail` | `getSbssNoticeTemplate` |
| `getSbssRequestClasses` | `getSbssResponsiblesData` | `getSbssSettingsNotices` |
| `getSbssStatuses` | `getSbssTicket` | `getSbssTickets` |
| `getSearchTemplates` | `getSegments` | `getServCategoryPrices` |
| `getServFuncs` | `getServiceBlocks` | `getServiceCategories` |
| `getServiceTypes` | `getSessionsRadius` | `getSettingsNotices` |
| `getSharedPosts` | `getSharedPostsCategories` | `getSmartCards` |
| `getSmartcardsHistory` | `getSmsHistory` | `getSmsQueue` |
| `getSoftlineDistrs` | `getSormDirectory` | `getStaff` |
| `getStat` | `getStatusGenTask` | `getSubscribeAccountsToNotify` |
| `getTarCategories` | `getTarCategory` | `getTarCategoryCatalog` |
| `getTarCategoryDiscount` | `getTarCategoryPrices` | `getTarCategoryRate` |
| `getTarif` | `getTariffCatalogs` | `getTariffCategories` |
| `getTariffModifiers` | `getTariffOperators` | `getTariffUserBlocks` |
| `getTariffsAddonsSet` | `getTariffsDetailSchedule` | `getTariffsGroups` |
| `getTariffsGroupsStaff` | `getTarifs` | `getTarifsExt` |
| `getTarifsExt2` | `getTarifsHistory` | `getTarifsRasp` |
| `getTarifsStaff` | `getTelCatIdxHistory` | `getTelCatalog` |
| `getTelClass` | `getTelClassLimits` | `getTelClassLimitsAmounts` |
| `getTelClassLimitsStaff` | `getTelClasses` | `getTelStaff` |
| `getTemplateRules` | `getTrunkStaff` | `getTrusteds` |
| `getTurboShapes` | `getUnsubscribeAccountsToNotify` | `getUsboxServices` |
| `getUserBlockTemplate` | `getUserBlockTemplates` | `getUserGroups` |
| `getUserPackets` | `getUsergroup` | `getVersion` |
| `getVgActivities` | `getVgEquipmentHistory` | `getVgSmartcardsHistory` |
| `getVgTarifs` | `getVgUserBlockSchedule` | `getVgroup` |
| `getVgroup2` | `getVgroupAccessOverride` | `getVgroupAddons` |
| `getVgroupAddresses` | `getVgroupAvailableTariffs` | `getVgroupInstallments` |
| `getVgroupServices` | `getVgroups` | `getVgroupsAddonsSet` |
| `getVgroupsAddonsStaff` | `getVgroupsRenewal` | `getVlans` |
| `getWeekends` | `importPhoneCategories` | `induceArcdSynch` |
| `insAccountAddon` | `insAgentOption` | `insBlkRasp` |
| `insBonusMinutePacketIdx` | `insBonusMinutePacketVg` | `insCaptivePortalCardSet` |
| `insCatItem4Category` | `insClientTarifsRasp` | `insCredit` |
| `insDeviceGroupsMember` | `insDeviceTreeItem` | `insDevicesOptionsSet` |
| `insGiftsAssignment` | `insGrStaff` | `insPacketTariff` |
| `insPrePayment` | `insRegistry` | `insRouteWeights` |
| `insTarifsStaff` | `insUsergroupStaff` | `insupdASCat` |
| `insupdAccPhone` | `insupdAccount` | `insupdAccountsAddonsSet` |
| `insupdAction` | `insupdActionStaff` | `insupdActionTariff` |
| `insupdAddressArea` | `insupdAddressBuilding` | `insupdAddressCity` |
| `insupdAddressCountry` | `insupdAddressEntrance` | `insupdAddressFlat` |
| `insupdAddressFloor` | `insupdAddressRegion` | `insupdAddressSettle` |
| `insupdAddressStreet` | `insupdAgent` | `insupdAgent2` |
| `insupdAgreement` | `insupdAgreementAddons` | `insupdAgreementRenewal` |
| `insupdAgreementTemplate` | `insupdAgreementsAddonsSet` | `insupdAppComment` |
| `insupdApplication` | `insupdAutonumTemplate` | `insupdBillDeliveryMethod` |
| `insupdBonusMinutePacket` | `insupdCaptivePortal` | `insupdCardSet` |
| `insupdCatalog` | `insupdCategoryModifier` | `insupdCerberMask` |
| `insupdClientUsboxService` | `insupdCurrency` | `insupdDevGroupOptSet` |
| `insupdDevGroupOptStaff` | `insupdDevGroupOptVals` | `insupdDevOptStaff` |
| `insupdDevice` | `insupdDeviceGroup` | `insupdDictionary` |
| `insupdDiscountAddon` | `insupdDocument` | `insupdEpsAgreementsParams` |
| `insupdEquipment` | `insupdEquipmentModel` | `insupdExternalModuleOption` |
| `insupdExternalModuleOptions` | `insupdExternalService` | `insupdGift` |
| `insupdGiftOptionSet` | `insupdGroup` | `insupdIPCat` |
| `insupdInstallmentsPlan` | `insupdMacStaff` | `insupdManager` |
| `insupdManagersGroup` | `insupdMasterCategory` | `insupdMessengersMessageHistory` |
| `insupdMessengersMessageQueue` | `insupdMultitarif` | `insupdOperTelStaff` |
| `insupdPacket` | `insupdPacketEntry` | `insupdPassTemplate` |
| `insupdPayClass` | `insupdPeople` | `insupdPeopleAddr` |
| `insupdPhoneParser` | `insupdPhoneReplace` | `insupdPhoneStaff` |
| `insupdPort` | `insupdPortPolicy` | `insupdPortState` |
| `insupdPostman` | `insupdRadiusAttr` | `insupdRnas` |
| `insupdRole` | `insupdRoleStaff` | `insupdRoleTariff` |
| `insupdRoleUserGroup` | `insupdRoute` | `insupdSale` |
| `insupdSaleDictionaryEntry` | `insupdSbssApplClass` | `insupdSbssCrmFile` |
| `insupdSbssPost` | `insupdSbssPostFile` | `insupdSbssRequestClass` |
| `insupdSbssStatus` | `insupdSbssTicket` | `insupdSearchTemplate` |
| `insupdSegment` | `insupdServCategoryPrice` | `insupdServFuncs` |
| `insupdService` | `insupdSettingsNotice` | `insupdSharedPostsCategory` |
| `insupdSmartCard` | `insupdSmsHistory` | `insupdSmsQueue` |
| `insupdSormDirectory` | `insupdSpecialOffer` | `insupdStaff` |
| `insupdTarCategory` | `insupdTarCategoryDiscount` | `insupdTarCategoryPrice` |
| `insupdTarif` | `insupdTariffModifier` | `insupdTariffUserBlock` |
| `insupdTariffsAddonsSet` | `insupdTariffsGroup` | `insupdTarifsRasp` |
| `insupdTelCat` | `insupdTelClass` | `insupdTelsCat` |
| `insupdTrunkStaff` | `insupdTrusted` | `insupdTurboShape` |
| `insupdUsboxService` | `insupdUserBlockTemplate` | `insupdUserPacket` |
| `insupdUsergroup` | `insupdVgroup` | `insupdVgroup2` |
| `insupdVgroupRenewal` | `insupdVgroupsAddonsSet` | `insupdVlan` |
| `isEnoughFundsForExternalService` | `lbinetForceFreeLease` | `lbinetGetLeases` |
| `lbinetMakeLeaseNonProlongable` | `lbinetSetLeases` | `lockSbssTicket` |
| `messengerIsReady` | `migrateAgreement` | `onProcessEdrRec` |
| `parseRegistryFile` | `putRadiusStat` | `reactivateLicense` |
| `recoverAccount` | `recoverDeletedAgreement` | `recoverPassword` |
| `recoverVgroup` | `recoveryAgreement` | `replaceSmartCard` |
| `restoreClientPasswd` | `sendEmail` | `sendOrders` |
| `sendSMS` | `sendSharedPost` | `setAccOnDateFirst` |
| `setAccountNotices` | `setAccountSharedPostCategories` | `setAdminExternalCharge` |
| `setAgreementsGroupAddon` | `setAgrmEquipment` | `setAgrmGroup` |
| `setAgrmGroupStaff` | `setCardEquipment` | `setCatalogService` |
| `setCfgSmsPlugin` | `setClientInfo` | `setClientMessagesViewed` |
| `setDHCPOption` | `setDHCPOptionsDict` | `setDefaultSmsPlugin` |
| `setEquipment` | `setExternalCharge` | `setFreeAddress` |
| `setOffer` | `setOpenPeriodBeginDate` | `setPhoneSourcesForRecalc` |
| `setRnasDevice` | `setServiceCategory` | `setServiceType` |
| `setSharedPost` | `setSmartCard` | `setTelClassLimit` |
| `setTelClassLimitsAmount` | `setTelClassLimitsStaff` | `setVgEquipment` |
| `setVgSmartCard` | `setVgUserBlockSchedule` | `setVgroupAddon` |
| `smsIsReady` | `startMonthlyFiscalization` | `startRecalc` |
| `stbPinReset` | `stopHybridServices` | `stopService` |
| `stopServiceByAgreement` | `stopSessionsRadius` | `stopUsboxService` |
| `stopVgUserBlockSchedule` | `transferVgToAgreement` | `updClientPass` |
| `updCurrencyRate` | `updHDOptions` | `updLicense` |
| `updOptions` | `updPhoneFilter` | `updSbssNoticeTemplate` |
| `updSbssSettingsNotice` | `updWeekend` | `updatePasswordByCode` |
| `updatePasswordByToken` | `validateClientEmail` | `validateClientMobileNumber` |

---

## Инструкция: Автогенерация клиента в Gradle (Вариант Б)

Для полного избавления от ручных парсеров и перехода на типизированный клиент добавьте плагин кодогенерации CXF / JAX-WS:

```kotlin
// build.gradle.kts
plugins {
    // Плагин генерации классов из WSDL
    id("io.mateo.cxf-codegen") version "2.4.0"
}

cxfCodegen {
    cxfVersion = "4.0.4"
    wsdl2java {
        register("lanbilling") {
            wsdl.set(file("wsdl.xml"))
            packageName.set("com.impuls.billing.soap.generated")
        }
    }
}
```

После выполнения команды `./gradlew wsdl2java` в проекте появятся готовые интерфейсы и классы DTO, готовые для вызова:

```kotlin
// Пример вызова сгенерированного клиента:
val port: Api3PortType = ...
val loginReq = ClientLogin().apply {
    login = username
    pass = password
}
val response = port.clientLogin(loginReq)
```