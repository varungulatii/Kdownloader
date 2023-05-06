# Kdownloader
### Kdownloader - A file downloader library for Android with pause and resume support


![1](https://user-images.githubusercontent.com/14194334/235831553-e71dbacf-7f18-499f-bd62-76cfeba37df1.png)

Overview of Kdownloader library
* Kdownloader can be used to download any type of files like image, video, pdf, apk and etc.
* This file downloader library supports pause and resume while downloading a file.
* Supports large file download.
* This downloader library has a simple interface to make download request.
* We can check if the status of downloading with the given download Id.
* Kdownloader gives callbacks for everything like onProgress, onCancel, onStart, onError and etc while downloading a file.
* Supports proper request canceling.
* Many requests can be made in parallel.
* All types of customization are possible.

## Using Kdownloader Library in your Android application

Update your settings.gradle file with the following dependency.

```
dependencyResolutionManagement {
repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
repositories {
google()
mavenCentral()
maven { url 'https://jitpack.io' } // this one
}
}
```

Update your module level build.gradle file with the following dependency.

```
dependencies {
implementation 'com.github.varungulatii:Kdownloader:1.0.3'
}
```
Do not forget to add internet permission in manifest if already not present

```
<uses-permission android:name="android.permission.INTERNET" />
```

Then initialize it in onCreate() Method of application class :
```
kDownloader = KDownloader.create(applicationContext)
```

Then use it in your activity as :

```
kDownloader = (applicationContext as MyApplication).kDownloader
val request = kDownloader.newRequestBuilder(url, dirPath, fileName,).tag("TAG").build()
downloadId = kDownloader.enqueue(request1, onStart = {}, onProgress = {}, onCompleted = {}, onError = {}, onPause = {})
```

Pause a download request :

```
Kdownloader.pause(downloadId);
```
Resume a download request
```
Kdownloader.resume(downloadId);
```

Cancel a download request
```
// Cancel with the download id
Kdownloader.cancel(downloadId);

// The tag can be set to any request and then can be used to cancel the request
Kdownloader.cancel(TAG);

// Cancel all the requests
Kdownloader.cancelAll();
```

Clean up resumed files if database enabled
```
// Method to clean up temporary resumed files which is older than the given day
Kdownloader.cleanUp(days);
```

TODO

*  Download notifications






