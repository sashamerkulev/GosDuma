package ru.merkulyevsasha.gdcore.domain;


import java.util.List;

import io.reactivex.Single;
import ru.merkulyevsasha.gdcore.models.Law;

public interface DeputyDetailsInteractor {

    Single<List<Law>> getDeputyLaws(final int deputyId, final String searchText, final String orderby);
}
