# Update a (Custom) Field Based on Two Dates

This is a groovy script that validates two different dates, and based on the differences, updates a field.

On a basic level, it is set to **compare the _Due Date_ and _Resolution Date_ fields. If the due date is greater than the resolved date, update the _Target Met_ field to Yes. Else, update _Target Met_ to No.** This check is done on a workflow post function, which would be more appropriate given the situation.

If you would like to adjust this script for your own instance use, you can modify the following areas:

    def targetMetFieldName = "Target Met"
    Long targetMetYesOptionID = 10001
    Long targetMetNoOptionID = 10002
    
For more information on this script, you can refer to my site post: [Update Field based on Difference between Two Dates](http://bit.ly/update-field-dates) 