package ru.merkulyevsasha.gdcore.repositories;


import java.util.List;

import io.reactivex.Single;
import ru.merkulyevsasha.gdcore.models.Deputy;

public interface DeputiesRepository {

    Single<List<Deputy>> getDeputies2(String search, String order, String position, int isCurrent);
}
