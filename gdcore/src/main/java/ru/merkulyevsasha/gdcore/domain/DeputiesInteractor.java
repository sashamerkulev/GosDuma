package ru.merkulyevsasha.gdcore.domain;


import java.util.List;

import io.reactivex.Single;
import ru.merkulyevsasha.gdcore.models.Deputy;

public interface DeputiesInteractor {

    Single<List<Deputy>> getDeputies(String searchText, String orderBy, String position, int isCurrent);
}
