package ru.merkulyevsasha.gosduma.data;


import java.util.List;

import ru.merkulyevsasha.gosduma.models.Deputy;

public interface DeputiesRepository {

    List<Deputy> getDeputies(String search, String order, String position, int isCurrent);


}
