package ru.merkulyevsasha.gosduma.mvp;



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
