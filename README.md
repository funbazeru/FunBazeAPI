# FunBazeAPI
### Интерфейс взаимодействия с системами проекта **[FunBaze.ru](https://funbaze.ru/)**<br>Подробная документация: https://funbaze.ru/javadocs/api
-----------------------------------
## Примеры работы
#### Получение учётной записи пользователя
```java
//Получаем менеджер учётных записей
UserManager manager = FunBazeApi.getUserManager();
FBUser user;
try {
  //Подгружаем учётную запись из кэша
  user = manager.getUser("DrKapdor");
} catch(UserNotLoadedException exception) {
  /*
  * В случае, если учётная запись не была кэширована, подргужаем её.
  * После подгрузки она будет автоматически кэширована.
  *
  * CacheMethod.GAME_SESSION - обозначаем метод,
  * который будет сохранять учётную запись в кэше до
  * того момента, пока тот не покинет сервер.
  */
  user = manager.load("DrKapdor", CacheMethod.GAME_SESSION);
}
```
#### Взаимодействие с учётной записью
```java
//Начислим игроку немного игровой валюты
UserBalance balance = user.getData().getBalance();
balance.addMoney(1500);
user.getData().setBalance(balance);
user.save();
user.asBukkit().sendMessage("Ваш баланс был пополнен на 1.5000!");
```
