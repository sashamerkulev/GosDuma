package ru.merkulyevsasha.gdcore.repositories;


import java.util.List;

import io.reactivex.Single;
import ru.merkulyevsasha.gdcore.models.Law;

public interface DeputyDetailsRepository {

    Single<List<Law>> getDeputyLaws2(final int deputyId, final String searchText, final String orderby);
}
