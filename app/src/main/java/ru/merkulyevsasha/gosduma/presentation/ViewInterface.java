package ru.merkulyevsasha.gosduma.presentation;



public interface ViewInterface extends MvpView {

    interface OnClickListener {
        void onItemClick(int position);
    }

    interface OnPresenterListener {
        void onPresenterCreated(PresenterInterface presenter);
    }

}
