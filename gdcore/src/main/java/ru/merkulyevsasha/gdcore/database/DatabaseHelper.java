package ru.merkulyevsasha.gdcore.database;

import java.util.List;

import ru.merkulyevsasha.gdcore.models.Article;
import ru.merkulyevsasha.gdcore.models.Codifier;
import ru.merkulyevsasha.gdcore.models.Deputy;
import ru.merkulyevsasha.gdcore.models.DeputyRequest;
import ru.merkulyevsasha.gdcore.models.Law;
import ru.merkulyevsasha.gdcore.models.ListData;

public interface DatabaseHelper {
    List<ListData> selectAll(String tableName);

    void deleteAll(String tableName);

    void deleteArticles(int source);

    void addArticles(int source, List<Article> articles);

    List<Article> getArticles(int source);

    List<Deputy> getDeputies(String searchText, String orderBy, String position, int isCurrent);

    List<Law> getDeputyLaws(int deputyId, String searchText, String orderBy);

    List<Law> getLaws(String searchText, String orderBy);

    List<DeputyRequest> getDeputyRequests(String searchText, String orderBy);

    Codifier getPhaseById(int id);

    Codifier getStageById(int id);

    List<Codifier> getProfileComittees(int id);

    List<Codifier> getCoexecutorCommittees(int id);

    List<Codifier> getLawDeputies(int id);

    List<Codifier> getLawRegionals(int id);

    List<Codifier> getLawFederals(int id);
}
