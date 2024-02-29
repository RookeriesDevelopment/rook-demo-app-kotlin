# Rook demo app kotlin

Demo app for ROOK modular packages:

* [rook-users](https://github.com/RookeriesDevelopment/rook-android-sdks-docs/tree/main/rook-users)
* [rook-health-connect](https://github.com/RookeriesDevelopment/rook-android-sdks-docs/tree/main/rook-health-connect)
* [rook-transmission](https://github.com/RookeriesDevelopment/rook-android-sdks-docs/tree/main/rook-transmission)

## Configure & Run

* In the root folder create a `local.properties` file with the following properties:

```properties
clientUUID=CLIENT_UUID
secretKey=SECRET_KEY
```

* Sync gradle.
* This project has a preconfigured USER_ID (clientHC) if you want to change it, go to
  the `com.rookmotion.rookconnectdemo.common.Environment.kt` file. 
