package ru.merkulyevsasha.gosduma.models;


public class Law {

    public int id;
    public String number;
    public String name;
    public String comments;
    public long introductionDate;

    public int lastEventStageId;
    public int lastEventPhaseId;
    public String lastEventSolution;
    public long lastEventDate;
    public String lastEventDocName;
    public String lastEventDocType;

    public int responsibleId;
    public String responsibleName;

    public String type;

}
