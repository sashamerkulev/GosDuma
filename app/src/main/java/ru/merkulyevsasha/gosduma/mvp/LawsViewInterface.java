package ru.merkulyevsasha.gosduma.mvp;


import java.util.List;

import ru.merkulyevsasha.gosduma.models.Law;

public interface LawsViewInterface {

    void show(List<Law> items);
}
