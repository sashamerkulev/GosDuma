package ru.merkulyevsasha.gosduma.data;


import java.util.List;

import ru.merkulyevsasha.gosduma.models.Law;

public interface DeputyDetailsRepository {

    List<Law> getDeputyLaws(final int deputyId, final String searchText, final String orderby);
}
