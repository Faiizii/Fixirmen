package com.app.fixirman.api_db_helper;

import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

public abstract class DistinctLiveData<T> {

    private final String TAG = "DistinctLiveData";

    private final MediatorLiveData<T> result = new MediatorLiveData<>();

    public DistinctLiveData() {
        final LiveData<T> data = load();
        result.addSource(data, new Observer<T>() {
            boolean initialized = false;
            T lastObj;

            @Override
            public void onChanged(T obj) {

                Log.e(TAG, "onChanged: called" );
                if (!initialized) {
                    Log.e(TAG, "if init: called" );
                    initialized = true;
                    lastObj = obj;
                    result.postValue(lastObj);
                } else if(obj instanceof ArrayList && lastObj instanceof ArrayList){
                    if(((ArrayList) obj).size() == ((ArrayList) lastObj).size()){
                        for (int i = 0; i < ((ArrayList) obj).size(); i++) {
                            if(compareParcelables(((ArrayList) obj).get(i),((ArrayList) lastObj).get(i))){
                                Log.e(TAG, "onChanged: true called" );
                                ((ArrayList) obj).remove(i);
                                i--;
                            }
                        }
                        lastObj = obj;
                        result.postValue(lastObj);
                        Log.e(TAG, "onChanged: data after removing" );
                    }else{
                        Log.e(TAG, "onChanged: size no equal" );
                        Log.e(TAG, "onChanged: obj size "+((ArrayList) obj).size() );
                        Log.e(TAG, "onChanged: last obj size "+((ArrayList) lastObj).size() );
                    }
                    Log.e(TAG, "onChanged: is list called" );
                }else if ((obj == null && lastObj != null) || !DistinctLiveData.this.equals(obj, lastObj)) {
                    Log.e(TAG, "else if: called" );
                    lastObj = obj;
                    result.postValue(lastObj);
                }else{
                    Log.e(TAG, "last obj"+lastObj );
                    Log.e(TAG, "obj "+obj );
                    Log.e(TAG, "else: called" );
                }
            }
        });
    }

    private boolean compareParcelables(Object o, Object o1){
        if(o instanceof Parcelable && o1 instanceof  Parcelable){
            Log.e(TAG, "compareParceables: called" );
            return new Gson().toJson(o).equalsIgnoreCase(new Gson().toJson(o1));
        }else{
            return false;
        }
    }

    protected boolean equals(T newObj, T lastObj) { return newObj == lastObj; }

    protected abstract LiveData<T> load();

    public LiveData<T> asLiveData() { return result; }
}