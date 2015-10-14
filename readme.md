
![Logo](logo.png)

# Bicycle Manager Android

> This is the Android app for the **[Bicycle Manager Server](https://github.com/blueskyfish/bicycle-manager-server.git)** (the Android Frontend)


## Setup the BaseURL

The base url for the backend must be configured separably.

First: Create a string resource in the folder `app/src/main/res` (The name of the string resource does not matter).

```xml
<?xml version="1.0" encoding="utf-8"?>
<!--
    This file is not in the git repository !!
-->
<resources>
    <string name="setting_base_url">http(s)://domain/subfolder</string>
</resources>
```

## License

```
The MIT License (MIT)
Copyright (c) 2015 BlueSkyFish
```