# FunBazeAPI 
### Интерфейс взаимодействия с системами проекта **[FunBaze.ru](https://funbaze.ru/)**<br>Подробная документация: https://funbaze.ru/javadoc/api
-----------------------------------
## Установка плагина
#### I. Добавляем Maven зависимость в `pom.xml`
```xml
<dependency>
  <groupId>org.funbaze</groupId>
  <artifactId>funbazeapi</artifactId>
  <version>1.0.2-RELEASE</version>
</dependency>
```
#### II. Добавляем зависимость в `plugin.yml`
```yaml
name: MyPlugin
author: Coder
main: me.coder.myplugin.Main
version: 1.0-SNAPSHOT
depend: 
  - ...
  - FunBazeAPI
```
## Примеры работы
### Получение учётной записи пользователя
```java
// Получаем API
FunBazeApi api = Bukkit.getServer().getServicesManager().load(FunBazeApi.class);
//Получаем менеджер учётных записей
UserManager manager = api.getUserManager();
FBUser user;
try {
  //Подгружаем учётную запись из кэша
  user = manager.getUser("DrKapdor");
} catch (UserNotLoadedException exception) {
  /*
  * В случае, если учётная запись не была кэширована, подргужаем её.
  * После подгрузки она будет автоматически сохранена в кэше.
  *
  * CacheMethod.GAME_SESSION - указываем метод кэширования,
  * сохраняющий учётную запись пользованя в кэше до тех пор,
  * пока пользователь находится в игровой сесси (в сети).
  */
  user = manager.load("DrKapdor", CacheMethod.GAME_SESSION);
}
```
### Взаимодействие с учётной записью
```java
//Управление балансом пользователя
UserBalance balance = user.getData().getBalance();
balance.addMoney(1500); //Пополняем баланс игровых рублей
balance.addVouchers(150); //Пополняем баланс донат-валюты
user.save();
user.asBukkit().sendMessage("Ваш баланс был пополнен!");
```
```java
//Управление метаданными ролевого персонажа
UserMeta meta = user.getData().getMeta();
meta.setAge(30);
meta.setGender(UserGender.MALE);
user.save();
user.asBukkit().sendMessage("Теперь вы солидный молодой человек, на вид лет тридцати!");
```

## Требования
* Spigot версии 1.12.2

## Зависимости
* [PluginMessagingAPI](https://github.com/DrKapdor/pmapi)
* [ProtocolLib](https://github.com/dmulloy2/ProtocolLib)
* [NBTAPI](https://github.com/tr7zw/Item-NBT-API)
