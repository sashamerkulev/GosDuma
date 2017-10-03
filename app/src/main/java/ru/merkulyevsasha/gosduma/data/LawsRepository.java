package ru.merkulyevsasha.gosduma.data;


import java.util.List;

import io.reactivex.Single;
import ru.merkulyevsasha.gosduma.models.Law;

public interface LawsRepository {

    Single<List<Law>> getLaws2(String search, String order);
}
