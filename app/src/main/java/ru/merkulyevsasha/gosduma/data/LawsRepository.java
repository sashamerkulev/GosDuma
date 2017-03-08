package ru.merkulyevsasha.gosduma.data;


import java.util.List;

import ru.merkulyevsasha.gosduma.models.Law;

public interface LawsRepository {

    List<Law> getLaws(String search, String order);

}
