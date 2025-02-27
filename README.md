# Redfast Android SDK (mobile/tablet/TV)

Please refer to the Android help [page](https://help.redfast.com/docs/android-sdk) for SDK installation instructions.

See [Releases](https://github.com/redfast/redfast-sdk-android/releases) for the latest version

# Redfast Demo App
The demo app illustrates a reference implementation of the Redfast SDK. Before you run the demo app, make sure to update the following (your Customer Success Manager can provide details).
- `APP_ID` at `com.redfast.mpass.MainActivityKt.APP_ID`
- `bearerToken` at `com.redfast.mpass.api.RedflixApi.bearerToken`
- `API_KEY` at `com.redfast.mpass.api.TmdbKt.API_KEY`
- `RF_APP` at `com.redfast.mpass.api.TokenApliKt.RF_APP`
- `DEFAULT_USER_ID` at `com.redfast.mpass.base.DefaultSharedPrefsKt.DEFAULT_USER_ID`
- For Amazon build also add `api_key.txt` file under `app/src/amazon/assets/api_key.txt` with the amazon api key
- For Google build also add `google-services.json` file under `app` with the google api key
