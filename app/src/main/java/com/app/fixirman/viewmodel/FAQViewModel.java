package com.app.fixirman.viewmodel;

import android.widget.ProgressBar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.fixirman.api_db_helper.DaoHelper;
import com.app.fixirman.model.faq.FAQModel;

import java.util.List;

import javax.inject.Inject;

public class FAQViewModel extends ViewModel {
    private DaoHelper helper;
    @Inject
    public FAQViewModel(DaoHelper helper){
        this.helper = helper;
    }

    public void init(ProgressBar pb,int userId){
        helper.getOnlineFAQList(pb,userId,null);
    }
    public LiveData<List<FAQModel>> getFAQs(ProgressBar pb,int userId){
        List<FAQModel> faq = helper.getFAQs();
        MutableLiveData<List<FAQModel>> liveData = new MutableLiveData<>();
        if(faq.size() > 0){
            liveData.setValue(faq);
        }else{
            helper.getOnlineFAQList(pb,userId,liveData);
        }
        return liveData;
    }
}
