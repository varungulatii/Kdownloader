# Kdownloader
### Kdownloader - A file downloader library for Android with pause and resume support

![1](https://user-images.githubusercontent.com/14194334/236659163-0d3f17a6-99ea-4b38-aec9-36ce93bb006d.png)

### Overview of Kdownloader library
* Kdownloader can be used to download any type of files like image, video, pdf, apk and etc.
* This file downloader library supports pause and resume while downloading a file.
* Supports large file download.
* This downloader library has a simple interface to make download request.
* We can check if the status of downloading with the given download Id.
* Kdownloader gives callbacks for everything like onProgress, onCancel, onStart, onError and etc while downloading a file.
* Supports proper request canceling.
* Many requests can be made in parallel.
* All types of customization are possible.

![sample_app2-min](https://user-images.githubusercontent.com/14194334/236852098-406eeb27-f036-42bf-aab4-74eb4492f4d8.png)

## Using Kdownloader Library in your Android application

Update your settings.gradle file with the following dependency.

```groovy
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

```groovy
dependencies {
    implementation 'com.github.varungulatii:Kdownloader:1.0.4'
}
```
Do not forget to add internet permission in manifest if already not present

```
<uses-permission android:name="android.permission.INTERNET" />
```

First of all, we need to create a Kdownloader instance
```kotlin
kDownloader = KDownloader.create(applicationContext)
```

Then use it like this anywhere:

```kotlin
val request = kDownloader
    .newRequestBuilder(url, dirPath, fileName,)
    .tag("TAG")
    .build()

// Using all of these lambdas is not mandatory. for example - you can only use onStart or onProgress also
downloadId = kDownloader.enqueue(request, 
    onStart = {
    },    
    onProgress = {
    },
    onProgressBytes = { currentBytes, totalBytes ->
    },
    onCompleted = {
    }, 
    onError = {
    }, 
    onPause = {
    }
)
```

### Pause a download request :

```kotlin
kDownloader.pause(downloadId);
```
### Resume a download request
```kotlin
kDownloader.resume(downloadId);
```

### Cancel a download request
```kotlin
// Cancel with the download id
kDownloader.cancel(downloadId);

// The tag can be set to any request and then can be used to cancel the request
kDownloader.cancel(TAG);

// Cancel all the requests
kDownloader.cancelAll();
```

### Clean up resumed files if database enabled
```kotlin
// Method to clean up temporary resumed files which is older than the given day
kDownloader.cleanUp(days);
```

### TODO

*  Download notifications

### If this library helps you in anyway, show your love ❤️ by putting a ⭐ on this project ✌️

### License
```
   Copyright (C) 2023 Varun Gulati

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
   
### Contributing to Kdownloader

All pull requests are welcome, make sure to follow the contribution guidelines when you submit pull request.
