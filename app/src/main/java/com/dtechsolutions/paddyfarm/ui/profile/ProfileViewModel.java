package com.dtechsolutions.paddyfarm.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dtechsolutions.paddyfarm.data.models.User;
import com.dtechsolutions.paddyfarm.data.repositories.UsersRepository;
import com.dtechsolutions.paddyfarm.utils.AlertEvent;
import com.dtechsolutions.paddyfarm.utils.BaseViewModel;
import com.dtechsolutions.paddyfarm.utils.Resource;

public class ProfileViewModel extends BaseViewModel {
    private final UsersRepository usersRepository;
    private final MutableLiveData<Resource<User>> user;

    public ProfileViewModel() {
        usersRepository = new UsersRepository();
        user = new MutableLiveData<>();
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
        user.setValue(profile);
    }
}
