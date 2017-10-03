package ru.merkulyevsasha.gosduma.data;


import java.util.List;

import io.reactivex.Single;
import ru.merkulyevsasha.gosduma.models.Law;

public interface DeputyDetailsRepository {

    Single<List<Law>> getDeputyLaws2(final int deputyId, final String searchText, final String orderby);
}
