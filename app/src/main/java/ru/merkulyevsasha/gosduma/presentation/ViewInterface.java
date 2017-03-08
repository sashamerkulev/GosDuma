package ru.merkulyevsasha.gosduma.presentation;



public interface ViewInterface {

    interface OnClickListener {
        void onItemClick(int position);
    }

    interface OnPresenterListener {
        void onPresenterCreated(PresenterInterface presenter);
    }

    void showProgress();
    void hideProgress();

}
