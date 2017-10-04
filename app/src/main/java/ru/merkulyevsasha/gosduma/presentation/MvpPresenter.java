package ru.merkulyevsasha.gosduma.presentation;


public class MvpPresenter<T extends MvpView> {

    protected T view;

    public void bind(T view){
        this.view = view;
    }

    public void unbind(){
        this.view = null;
    }

}
