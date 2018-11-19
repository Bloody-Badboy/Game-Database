# # \[ 🚧 Work in progress 👷‍♀️⛏👷🔧️👷🔧 🚧 ] **Game Database App**

## Udacity Android Developer Nanodegree Program - Capstone project

# Screenshots

Game List Screen            |  Game Search Screen
:-------------------------:|:-------------------------:
![](art/screenshots/screenshot_game_list.png) | ![](art/screenshots/screenshot_search.png)

Game Detail Screen 1           |  Game Detail Screen 2
:-------------------------:|:-------------------------:
![](art/screenshots/screenshot_detail_1.png) | ![](art/screenshots/screenshot_detail_2.png)

Game Screenshot Viewer            |  App Widget
:-------------------------:|:-------------------------:
![](art/screenshots/screenshot_photo_viewer.png) | ![](art/screenshots/screenshot_app_widget.png)

---

## Use the project with your own IGDB API key

* Get you own IGDB API key. See the [get an API key][0].
* Find `.gradle` folder in your home directory, create a file named `gradle.properties` (if not present).

    Usually it can be found at:

        Windows: C:\Users\<Your Username>\.gradle
        Mac: /Users/<Your Username>/.gradle
        Linux: /home/<Your Username>/.gradle

        Inside it there would be a file named gradle.properties (just create it if there isn’t any).

* Open the `gradle.properties` file and paste your API key into the value of the `GOOGLE_MAPS_API_KEY` property, like this

    `IGDB_API_KEY=PASTE-YOUR-API-KEY-HERE`

* Now you should be able to successfully sync the project.

[0]: https://www.igdb.com/api