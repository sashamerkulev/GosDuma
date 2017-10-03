package ru.merkulyevsasha.gosduma.domain;


import java.util.List;

import io.reactivex.Single;
import ru.merkulyevsasha.gosduma.models.Law;

public interface LawsInteractor {

    Single<List<Law>> getLaws(String searchText, String orderBy);
}
