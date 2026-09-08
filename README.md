# Juno Movie

A private home movie app for a Huawei HG8145V5 USB HDD.

## Storage
Only one folder is used:

/Movies/

Put files such as:
- Movie 1.mp4
- Movie 2.mkv
- Movie 3.mp4

## First launch
The app uses the fixed FTP account:
- Username: root
- Server: 192.168.100.1
- Port: 21

The FTP password is requested once during setup and saved on that Android device. Normal viewers never enter FTP credentials.

## Profiles
Create name-only profiles such as JJ, Dad, Mum and Guest. Each profile has its own favorites and watch-progress data on that client.

## Streaming
The movie is read from the router through a custom FTP Media3 data source. It is streamed for playback rather than intentionally copied to the phone.

## Build
Open the project in Android Studio. If prompted, use JDK 17 for Gradle. Sync, then Run.

## GitHub
The included GitHub Actions workflow builds a debug APK automatically and uploads it as a workflow artifact.

## Security
Keep WAN/Internet FTP disabled on the router. This app is intended for the private LAN.
