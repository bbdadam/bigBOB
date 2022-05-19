# bigBOB
An API to provide the destination for employees to travel based on the next week's weather conditions.
# How to use
After running the app locally, go to-> http://localhost:8080/cityToVisit. The cityToVisit end-point takes 1 parameter -> employeeName.
Here you can specify the name of the employee for which you want to query for.
e.g.: http://localhost:8080/cityToVisit?employeeName=Gen
The project contains .sql config file with some predifiend values, so the app could be tested right after it's fired without any extra steps.
# How does it work
The app is using an H2 in memory database. To test with different scenarios, modify the data.sql config file.
e.g.: If you want to check the functionality of the previously visited cities, just add the key-pairs of the employee's id and the cities id into the TBL_EMPLOYEES_VISITED_CITIES table.
Most of the configs have the default values provided by dependencies in order for easier developemnt.
The weather conditions are provided by https://open-meteo.com/en. For further details please see dev docs: https://open-meteo.com/en/docs
# Areas of improvement
* Replace H2 in memory database.
* Implement a 3rd party logger.
* Employee should extend from a abstract class (e.g.: User). That way further user types could be implemented.
  * Implement UserFactory
* At the current stage the weather forecast provider is very static in terms of the available data from https://open-meteo.com/en. To achive a more sophisticated and flexible approach to determine the required conditions for a travel, the cityToVisit end-point could be modified with further parameters.  
