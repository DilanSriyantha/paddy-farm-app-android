package com.dtechsolutions.paddyfarm.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dtechsolutions.paddyfarm.MyApplication;
import com.dtechsolutions.paddyfarm.data.models.User;
import com.dtechsolutions.paddyfarm.data.repositories.UsersRepository;
import com.dtechsolutions.paddyfarm.utils.AlertEvent;
import com.dtechsolutions.paddyfarm.utils.BaseViewModel;
import com.dtechsolutions.paddyfarm.utils.Resource;
import com.dtechsolutions.paddyfarm.utils.TokenProvider.SharedPrefsTokenProvider;

public class ProfileViewModel extends BaseViewModel {
    private final UsersRepository usersRepository;
    private final MutableLiveData<Resource<User>> user = new MutableLiveData<>();

    public ProfileViewModel() {
        usersRepository = new UsersRepository();
    }

    public LiveData<Resource<User>> getProfile() {
        return user;
    }

    public void fetchProfile() {
        usersRepository
                .getProfile()
                .observeForever(this::onProfileLoaded);
    }

    private void onProfileLoaded(Resource<User> profile) {
        if(profile.status == Resource.Status.ERROR)
            alertEvent.postValue(new AlertEvent(
                    AlertEvent.Type.ERROR,
                    null,
                    "Failed to load your profile! Please try again later."
            ));

        user.setValue(profile);
    }
}
