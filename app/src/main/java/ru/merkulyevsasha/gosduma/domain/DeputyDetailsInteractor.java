package ru.merkulyevsasha.gosduma.domain;


import java.util.List;

import io.reactivex.Single;
import ru.merkulyevsasha.gosduma.models.Law;

public interface DeputyDetailsInteractor {

    Single<List<Law>> getDeputyLaws(final int deputyId, final String searchText, final String orderby);
}
