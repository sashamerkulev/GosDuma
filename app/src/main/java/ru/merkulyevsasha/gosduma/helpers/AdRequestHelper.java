package ru.merkulyevsasha.gosduma.helpers;

import com.google.android.gms.ads.AdRequest;

import ru.merkulyevsasha.gosduma.BuildConfig;


public class AdRequestHelper {

    public static AdRequest getAdRequest(){
        return BuildConfig.DEBUG_MODE
                ? new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build()
                : new AdRequest.Builder().addTestDevice("349C53FFD0654BDC5FF7D3D9254FC8E6").build();
    }

}
