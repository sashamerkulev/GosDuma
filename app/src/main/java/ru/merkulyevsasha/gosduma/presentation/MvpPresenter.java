package ru.merkulyevsasha.gosduma.presentation;


import io.reactivex.disposables.CompositeDisposable;

public class MvpPresenter<T extends MvpView> {

    protected T view;
    protected CompositeDisposable compositeDisposable;

    public MvpPresenter(){
        compositeDisposable = new CompositeDisposable();
    }

    public void bind(T view){
        this.view = view;
    }

    public void unbind(){
        this.view = null;
    }

    public void onDestroy(){
        compositeDisposable.dispose();
    }
}
