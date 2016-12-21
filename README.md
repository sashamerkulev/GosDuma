# GosDuma

Приложение работы с базой данных "Государственная дума".

База данных получена путем чтения данных из открытого API сайта Государственной думы http://api.duma.gov.ru/

В приложении для работы с БД (SQLiteDatabase) используется самописный singleton-класс (см. файлы db\DatabaseHelper.java).

Для чтения новостей (разделы меню "Новости ГД" и "Новости председателя ГД") используется библиотика Retrofit2 (см. файлы http\ *.java), ответ от сервера парсится в коллекцию Article и затем отображается в соответствующих Activity. 

Для activities работы с данными о депутатх и законах используется архитектура MVP, где моделью является DatabaseHelper, есть соответствующие presenter-ы, а в качестве view выступают activities со своими layout (куда может входить и fragment-ы).

В некоторых Activity, с большим количеством widget в layout используется ButterKnife [http://jakewharton.github.io/butterknife/].

Подключен Firebase Crash Reporting.
