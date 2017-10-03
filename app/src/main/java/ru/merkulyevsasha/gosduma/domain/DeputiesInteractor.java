package ru.merkulyevsasha.gosduma.domain;


import java.util.List;

import io.reactivex.Single;
import ru.merkulyevsasha.gosduma.models.Deputy;

public interface DeputiesInteractor {

    Single<List<Deputy>> getDeputies(String searchText, String orderBy, String position, int isCurrent);
}
