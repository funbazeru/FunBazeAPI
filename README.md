# FunBazeAPI 
### Интерфейс взаимодействия с системами проекта **[FunBaze.ru](https://funbaze.ru/)**<br>Подробная документация: https://funbaze.ru/javadocs/api
-----------------------------------
## Примеры работы
#### Получение учётной записи пользователя
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
#### Взаимодействие с учётной записью
##### Управление балансом пользователя
```java
UserBalance balance = user.getData().getBalance();
balance.addMoney(1500); //Пополняем баланс игровых рублей
balance.addVouchers(150); //Пополняем баланс донат-валюты
user.getData().setBalance(balance);
user.save();
user.asBukkit().sendMessage("Ваш баланс был пополнен!");
```
##### Управление метаданными ролевого персонажа
```java
UserMeta meta = user.getData().getMeta();
meta.setAge(30);
meta.setGender(UserGender.MALE);
user.getData().setMeta(meta);
user.save();
user.asBukkit().sendMessage("Теперь вы солидный молодой человек, на вид лет двадцати!");
```
## Зависимости
* [PluginMessagingAPI](https://drive.google.com/u/0/uc?id=1hnaEhO6qr6qlRdx4GLRPVbhBtwPBMjuf&export=download)
* ProtocolLib
* NBTAPI
* WorldGuard v6.2.2
* WorldEdit v6.1.9
