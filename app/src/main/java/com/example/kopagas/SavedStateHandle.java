package com.example.kopagas;

import androidx.lifecycle.ViewModel;

public class SavedStateHandle extends ViewModel {
    private SavedStateHandle savedStateHandle;
    //public LiveData<List<String>> filteredData;
    //public UserViewModel(SavedStateHandle savedStateHandle) {
        //this.savedStateHandle = savedStateHandle;
        //LiveData<String> queryLiveData = savedStateHandle.getLiveData("query");
        //filteredData = Transformations.switchMap(queryLiveData, query -> {
            //return repository.getFilteredData(query);
        //});
    //}

    //public void setQuery(String query) {
        //savedStateHandle.set("query", query);
    //}
}
