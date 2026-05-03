# MyNews App 
MyNews is a simple and clean Android application that keeps users updated with the latest news from around the world. 
It uses the **GNews API** to fetch real-time news articles based on user-selected countries.

##  Features
* **Country-wise News:** Select different countries (Pakistan, USA, India, etc.) to view top headlines.
* **Swipe to Refresh:** Pull down to refresh and load the latest news instantly.
* **Error Handling:** Displays proper error messages for network issues or API limits, with a retry option.
* **Data Binding:** Implemented to keep code clean and improve performance.
* **Clean UI:** Minimal and user-friendly light mode interface.

##  Tech Stack
* **Language:** Java
* **Architecture:** MVVM (Model-View-ViewModel)
* **Networking:** Retrofit
* **Image Loading:** Glide

##  Components Used
* ViewModel & LiveData
* RecyclerView
* SwipeRefreshLayout

##  How to Setup
1. Open the project in **Android Studio**.
2. Add your GNews API Key in `MainActivity.java`.
3. Build and run the app on an emulator or physical device.

##  Screenshots


##  Future Improvements
- [ ] Dark Mode support
- [ ] Search News functionality
- [ ] Category-wise News (Sports, Tech, Business)
- [ ] Bookmark / Save Articles
