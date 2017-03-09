package ru.merkulyevsasha.gosduma.presentation;



public interface ViewInterface extends MvpView {

    interface OnClickListener {
        void onItemClick(int position);
    }

}
