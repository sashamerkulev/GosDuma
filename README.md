# GosDuma

Приложение работы с базой данных "Государственная дума".

База данных получена путем чтения данных из открытого API сайта Государственной думы http://api.duma.gov.ru/

Структурно проект содержит несколько packages, названия (и содержимое) которых соответствует терминам Clean Architecture (https://fernandocejas.com/2014/09/03/architecting-android-the-clean-way/):

- Presentation
- Domain
- Data

Data package содержит классы для работы c базой данных и internet.
- для работы с БД используется самописный класс data\db\DatabaseHelper;
- Для работы с сетью используется библиотека Retrofit2.

В этом же пакете лежат интерфейсы/классы repositories. Количество классов/интерфейсов соответствует количеству активити/фрагментов.

Пример:
```java
public class NewsRepositoryImpl implements NewsRepository {

    private DatabaseHelper db;
    private RssService serv;

    public NewsRepositoryImpl(DatabaseHelper db, RssService service){
        this.db = db;
        this.serv = service;
    }

    @Override
    public List<Article> getArticles(int id) {
        return db.getArticles(id);
    }

    @Override
    public void saveToCache(int id, List<Article> result){
        db.deleteArticles(id);
        db.addArticles(id, result);
    }

    @Override
    public Call<ResponseBody> gosduma() {
        return serv.gosduma();
    }

    @Override
    public Call<ResponseBody> chairman() {
        return serv.chairman();
    }
}
```

Есть примеры и попроще:
```java
public class LawsRepositoryImpl implements LawsRepository{

    private DatabaseHelper db;

    public LawsRepositoryImpl(DatabaseHelper db){
        this.db = db;
    }

    @Override
    public List<Law> getLaws(String search, String order){
        return db.getLaws(search, order);
    }

}
```

Domain package содержит интерфейсы/классы интеракторов, которые содержат бизнес логику и взаимодействуют с классами репозитариев и презентерами.

Пример:
```java
public class NewsInteractorImpl implements NewsInteractor {

    private NewsRepository repo;

    public NewsInteractorImpl(NewsRepository repo){
        this.repo = repo;
    }

    @Override
    public List<Article> getArticles(int id) {
        return repo.getArticles(id);
    }

    @Override
    public void loadNews(final int id, final NewsCallback callback) {
        Call<ResponseBody> resp = null;

        if (id == R.id.nav_news_gd){
            resp = repo.gosduma();
        } else if (id == R.id.nav_news_preds){
            resp = repo.chairman();
        }

        if (resp != null){
            resp.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        RssParser parser = new RssParser();
                        List<Article> result = parser.parseXml(response.body().string());
                        repo.saveToCache(id, result);

                        callback.success(result);

                    } catch(Exception e){
                        callback.failure(e);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    callback.failure(new Exception(t));
                }
            });
        }

    }

}

```

Остальные интеракторы выглядят попроще:
```java
public class LawsInteractorImpl implements LawsInteractor {

    private LawsRepository repo;
    private ExecutorService executor;

    public LawsInteractorImpl(ExecutorService executor, LawsRepository repo){
        this.repo = repo;
        this.executor = executor;
    }

    @Override
    public void loadLaws(final String searchText, final String orderBy, final LawsCallback callback) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Law> items = repo.getLaws(searchText, orderBy);
                    callback.success(items);
                } catch(Exception e){
                    callback.failure(e);
                }
            }
        });
    }
}
```



p.s.

Подключен Firebase Crash Reporting.
