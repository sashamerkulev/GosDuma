package ru.merkulyevsasha.gdcore.domain;


import java.util.HashMap;

import io.reactivex.Single;
import ru.merkulyevsasha.gdcore.models.Law;

public interface LawDetailsInteractor {

    Single<HashMap<String, String>> getLawDetails(Law law);
}
