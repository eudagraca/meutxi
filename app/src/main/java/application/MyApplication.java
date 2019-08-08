package application;

import android.app.Application;

import com.account.R;
import com.parse.Parse;
import com.parse.ParseInstallation;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.app_id))
                // if defined
                .clientKey(getString(R.string.client_id))
                .server(getString(R.string.server))
                .build()
        );

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("GCMSenderId", getString(R.string.gcm_defaultSenderId));
        installation.saveInBackground();

    }
}
