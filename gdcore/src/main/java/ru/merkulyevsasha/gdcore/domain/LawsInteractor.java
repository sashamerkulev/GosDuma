package ru.merkulyevsasha.gdcore.domain;


import java.util.List;

import io.reactivex.Single;
import ru.merkulyevsasha.gdcore.models.Law;

public interface LawsInteractor {

    Single<List<Law>> getLaws(String searchText, String orderBy);
}
