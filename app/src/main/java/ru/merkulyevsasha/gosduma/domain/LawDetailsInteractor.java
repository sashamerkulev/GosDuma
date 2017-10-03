package ru.merkulyevsasha.gosduma.domain;


import java.util.HashMap;

import io.reactivex.Single;
import ru.merkulyevsasha.gosduma.models.Law;

public interface LawDetailsInteractor {

    Single<HashMap<String, String>> getLawDetails(Law law);
}
