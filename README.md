SalsaTest
=========

Android test project that searches for venues that are close to a city using the foursquare web service. 
It uses the "near" parameter, thus, the search string should be a valid city name, otherwise it won't return
any results. Shows a list with the name and distance from the current location in kilometers.

Also shows the results as markers in a google map and zooms the map as close as possible to show all the markers.


Known issues:
- On screen rotation it does not restore the markers on the map.
