# Gallery Documentation
## Introduction
This documentation is written in the purpose of allowing other developers to understand the application structure for the gallery page of the Kronos APP.

Gallery page contains the following functionalities:
1. Display pending-to-display event entries with thumbnails.
2. Users click on one entry will bring them to the auto-fill editing page (preferably just a pop-up overlay page) in which they can modify the entry details and save them.
3. Allow users to filter the entries by tags. Tags are auto generated. They can also be created by the users in the auto-fill editing page.
4. [Additional] Sharing button for each entry so that users can share the event schedule with other users.

## Event entry storage as JSON files
### JSON example
This JSON object describes the entire event entry. It can be retrieved to be processed to bring back the auto-fill forms, and it would be processed by the main calendar for display
```json
{
    "id": "00000001",
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
* id [string]: a 8 digits string ID that is unique to the event entry. [00000001 to 99999999]
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

### Learn about JSON
https://www.digitalocean.com/community/tutorials/an-introduction-to-json 