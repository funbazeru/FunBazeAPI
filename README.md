# FunBazeAPI
Интерфейс взаимодействия с системами проекта **[FunBaze.ru](https://funbaze.ru/)**<br>
Подробная документация: https://funbaze.ru/javadocs/api
---
# Примеры работы
Получение учётной записи пользователя и взаимодействие с ней
```java
UserManager manager = FunBazeApi.getUserManager();
FBUser user;
try {
  user = manager.getUser("DrKapdor");
} catch(UserNotLoadedException exception) {
  System.out.println("Пользователь не был загружен в кэш...");
  user = manager.load("DrKapdor", CacheMethod.GAME_SESSION);
}
Balance balance = user.getData().getBalance();
balance.addMoney(1500);
user.getData().setBalance(balance);
user.save();
user.asBukkit().sendMessage("Ваш баланс был пополнен на 1.5000!");
```
