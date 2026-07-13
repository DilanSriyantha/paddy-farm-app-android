package com.dtechsolutions.paddyfarm.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.dtechsolutions.paddyfarm.data.models.User;
import com.dtechsolutions.paddyfarm.data.repositories.UsersRepository;
import com.dtechsolutions.paddyfarm.utils.AlertEvent;
import com.dtechsolutions.paddyfarm.utils.BaseViewModel;
import com.dtechsolutions.paddyfarm.utils.Resource;

public class ProfileViewModel extends BaseViewModel {
    private final UsersRepository usersRepository;

    private final MutableLiveData<Integer> fetchProfileTrigger = new MutableLiveData<>(0);

    private final LiveData<Resource<User>> profileResult;

    public ProfileViewModel() {
        usersRepository = new UsersRepository();

        profileResult = Transformations.switchMap(fetchProfileTrigger, onChange -> usersRepository.getProfile());
    }

    public void fetchProfile() {
        Integer current = fetchProfileTrigger.getValue();
        fetchProfileTrigger.postValue(current == null ? 1 : current + 1);
    }

    public LiveData<Resource<User>> getProfileResult() {
        return profileResult;
    }

//    private final UsersRepository usersRepository;
//    private final MutableLiveData<Resource<User>> user;
//
//    public ProfileViewModel() {
//        usersRepository = new UsersRepository();
//        user = new MutableLiveData<>();
//    }
//
//    public LiveData<Resource<User>> getProfile() {
//        return user;
//    }
//
//    public void fetchProfile() {
//        usersRepository
//                .getProfile()
//                .observeForever(this::onProfileLoaded);
//    }
//
//    private void onProfileLoaded(Resource<User> profile) {
//        user.setValue(profile);
//    }
}
