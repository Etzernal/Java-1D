# Gallery
## JSON example
This JSON object describes the entire event entry. It can be retrieved to be processed to bring back the auto-fill forms, and it would be processed by the main calendar for display
```json
{
    "display": true,
    "thumbnail": "{path to thumbnail}",
    "photo": "{path to photo captured}",
    "title": "Summer is over!",
    "tags": [
        "#summer",
        "#event"
    ],
    "fromDateTime": "00:00 Friday, Sep 30 2019",
    "toDateTime": "00:00 Saturday, Oct 1 2019",
    "allday": false,
    "fromDate": "",
    "toDate": "",
    "reminder": null,
    "location": "",
    "description": ""
}
```

## Explanation of each JSON entry
* display [boolean]: set to true to be displayed onto the calendar
* thumbnail [string]: contains the path to the thumbnail of the photo to be displayed in the auto-fill edit form as well as in the gallery. Empty string if don't exist
* photo [string]: contains the path to the original photo to be opened when user want to re-highlight the relevant information again
* title [string]: the title of the calendar event entry
* tags [list]: a list containing string of tags assigned to this particular event entry. Tags are used for filtering events in the gallery
* fromDateTime [string]: a string representing datetime in the format of _"HH:mm EEEEE, MM dd yyyy"_. This is the datetime from which the event starts
* toDateTime [string]: a string representing datetime in the format of _"HH:mm EEEEE, MM dd yyyy"_. This is the datetime till which the event ends. toDateTime can be the same as or later than fromDateTime, but cannot be earlier than fromDateTime.
* allday[boolean]: false if have specific from and to datetime. true if the event is an all-day event, then 'fromDateTime' and 'toDateTime' two fields will be set to empty. 'fromDate' and 'toDate' will have values
* fromDate[string]: a string representing the date in the format of _"EEEEE, MM dd yyyy"_. This is the date from which the event starts
* toDate[string]: a string representing the date in the format of _"EEEEE, MM dd yyyy"_. This is the date till which the event ends. toDate can be the same as fromDate or later than fromDate. But cannot be earlier than fromDate.
* reminder[string]: set to null if no reminder. Otherwise, a string representing reminder datetime in the format of _"HH:mm EEEEE, MM dd yyyy"_ will be stored as value.
* location[string]: optional field for user to key in location. This string will be used as a link to put into the Google map search field as query string.
* description[string]: optional field for user to type in description of the event.