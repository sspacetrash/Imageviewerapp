# Imageviewerapp
This is an app created using Flickr API to retrieve pictures online and display them in the app. Besides, SQLite is used to create a local database to allow user to store images.
Overall Design
STRUCTURE OF THE APP
Since my main focus of the app is to expand its functionality, a total of 5 sections in the app is implemented. The app starts off with the Main activity that shows the interesting photos from the Flickr API call, the search activity that lets user search photos, the favorites activity that shows the list of images that the user favorited, the preference activity that lets user to set their name and email, and change the color of the app. Lastly, is the About activity, that shows the details of the app details and copyright details. The whole app uses a navigation drawer to let the user to reach to different sections of the app.

MainActivity
The addition in the main activity is the favorites button. When the user clicks on the star, it saves the image to the database, and the star turns and stays yellow to show that the image is favorited. If the user clicks it again, it removes the image from the database. This is achieved by stacking two imageViews on top of each other. The first button colored in grey(with ID favButton), was set to visible and grey on default. On the button’s onClickListener, it executes the FavoritesDBHelper.insert method, sets the favButton to invisible and the second button colored in yellow to visible. And then when the user clicks again, they are triggering another listener that deletes the record from database. In order to avoid duplicating images in the favorites database, I used CONFLICT_REPLACE in one of the arguments for insertWithOnConflict. Which replaces the record with the same primary key in the database. And since the search activity and main activity uses the same adapter, so this function will be about to work on both.

REFRESHING CONTENT
When the user scrolls to the top and pulls down, a refresh is triggered and a new set of images will be replacing the current ones. This is implemented by using SwipeRefreshLayout, in the OnRefreshListener, a circular arrow will show the circle when pulled down to indicate it is refreshing. When the images are refreshed, it will perform the network call again, but instead of calling the first page, a random integer is generated using Math.random( ) to be passed as the argument for the network call, so whenever the user refreshes, they will get a new list of images.

ERROR HANDLING
An extra function is added to the photosResponse on failure, a dialog box using dlgBuilder will show an error message showing the network all is unsuccessful, and it provides an option to the user to decide to reload the page calling the network call again, or quitting the app using system.exit(0).

ATTEMPT TO RETRIEVE INFORMATION FROM DATABASE
For the favorite button, when the page is refreshed, the button will not be able to stay yellow. Originally, I was planning have a boolean variable to hold the status of the image if it is being favorited or not, and then I will run an if statement to either display the yellow button or the grey button. Since I will need to use the photo position from the adapter to check if there are records matched with the database, I can only do the comparison in onBindViewHolder because I can only use the position from inside the method, but inside the method, I can’t access to the imageViews declared in the ViewHolder class. Even I have access to the photo’s information, I was’t able modify the ImageView’s visibility from there.

DetailsActivity
DOWNLOADING IMAGES
It allows the user to download the image to the phone by clicking the floating action button. This is achieved by converting the image to be downloaded to a bitmap using Picasso. After retrieving the bitmap created from the url from the photo object, it compresses to a JPEG to minimize the image file size considering the download speed on a phone. In the beginning, the images wasn’t downloading because it needs the permission from the photo to be able to access internal storage, which can be easily changed from the phones settings. In order to make shorten the process of the download, a dialog box will pop up when the user first enter the details activity, requesting permission from the user to enable access to internal storage
without quitting the app. Furthermore, a dialog box will pop up again after they click the download button to confirm the action, and a toast message will shown when the image is successfully downloaded.


COMMENTS
A comments section is also implemented into the details activity. Firstly, the comments are retrieved by using the Flickr API photos.comments.getList, and a query getting a photoID is implemented. And it returns the comments which I have stored them in a new object. It contains the commenter’s name and the comment’s content. In order to pass the photo information into a new activity, intent.getIntExtra is used to retrieve the photo position from the list in the repository. And a recyclerView is used to display the comments.
In the beginning, the comments section was put in the details activity. But it was soon found out that it wasn’t ideal to have a scrollable section that only takes up half the screen or even less. As the length of descriptions for different images vary, a scroll will have to be added to the description as well, So to avoid having two scroll activities in one page, the comments is put into a different activity which can be accessed by clicking on the button at the right bottom corner.


FavoritesActivity
In the favorites activity, it shows the picture that the user has favorited by retrieving the database from FavoritesDBHelper. The database has five columns which store the id, title, owner name, description, and url. The method insert( ) takes a photo object argument, and the information is retrieved from the adapter and stored in database. In order to display the images, the loadAll( ) method runs a cursor that takes null for selection arguments, and store them in an arraylist that will be returned using a for loop.
A problem occurred in the beginning when the favorites is accessed multiple times, the images were repeated, as the arraylist in the adapter will retrieve the images from the database every time when the activity starts, so I have clear the list in the photo repository before loading the image into it.
Grid layout is also implemented to this activity to give a different look compared to the main and search activities. It shows the image in squares by using centerCrop for scale type in the imageview to give it a neat look. This layout is inspired by the popular social media app Instagram. Besides, the images are also clickable, and it directs the user to the Details Activity.


SearchActivity
The search activity uses the same layout and adapter from the MainActivity. The search activity is operated using the Flickr API flickr.photos.search. It returns a list of images that related to the search which is retrieved from the user using a query. Furthermore, an extra argument safe_search = 1 is implemented to prevent explicit images to show up.
When the user enters the search activity, the activity automatically opens the searchView without the user having to click the search icon again. This is achieved by using searchView.setFocusable. The query for the Flickr API is passed when the user clicks the search icon on the keyboard, and then it do the network call and updates the adapter.


PreferenceActivity
The preferences is made using android.preference.PreferenceActivity. The preferences is added automatically using addPreferenceFromResource from the resource from the root_preferences.xml file. Summaries are also added to the options to let the user know the functions are clear.


CHANGING THEMES
In the preference activity, the user will be able to change the theme selecting a range of color schemes from a radio button group. When clicked, the Preference manager stores a string that represents the name of the theme from the styles.xml that I have added. And a method called setTheme( ) which contains multiple if statements that finds the right theme to change by comparing the string from preferences and the theme name from the styles.xml.
I tried to make the theme to change without closing the app, by running setTheme( ) before setContentView( ), but unfortunately it didn’t work. So now the theme only changes whenever the user restarts the app.


AboutActivity
In the about activity, it retrieves the user’s name and email from the preferences activity. The preferences again is retrieved by using PreferenceManager.getDefaultSharedPreferences.

Besides, it also retrieves the the number of item favorited by the user. Which is retrieved from the DBHelper by using rawQuery(countQuery, null) that returns the number of rows in the database. Since whenever the activity starts, the preferences and database information is retrieved, so changes can be applied without restarting the app.
