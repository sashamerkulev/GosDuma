package ru.merkulyevsasha.gosduma.domain;



import io.reactivex.Single;
import ru.merkulyevsasha.gosduma.models.News;

public interface NewsServiceInteractor {

    Single<News> getNotificationNews2();
}
