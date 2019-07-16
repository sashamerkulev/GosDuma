package ru.merkulyevsasha.gdcore.repositories;


import java.util.List;

import io.reactivex.Single;
import ru.merkulyevsasha.gdcore.models.Law;

public interface LawsRepository {

    Single<List<Law>> getLaws2(String search, String order);
}
