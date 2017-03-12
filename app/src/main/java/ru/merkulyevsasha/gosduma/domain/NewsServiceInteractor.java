package ru.merkulyevsasha.gosduma.domain;


import java.util.List;

import ru.merkulyevsasha.gosduma.models.News;

public interface NewsServiceInteractor {

    List<News> getNotificationNews();

}
