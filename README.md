# managedatatypes
This example application demonstrates CRUD operations using the same four fields - date, name, email, and password and how they can be implemented using REST API Retrofit-Node.js-MongoDB, REST API Retrofit-PHP-MySQL or SQLite. In addition a Calendar View and an Export XLS serve as extras to view or save data 

Download the zip file and open it on android studio

Set up REST API Retrofit-PHP-MySQL

Depending on whether you are using XXAMP or another web server solution stack or a live version take note of the following.
Open the "phpfiles" folder and drop the contents (phpmanagedatatypes folder & config.php) directly into C:\xampp\htdocs or wherever you have the folder.
If you are using a live version, upload the files directly to your website and note the url where "phpmanagedatatypes" folder & "config.php" are found.
Create a database in PHPMyAdmin called "dtypes" then create a table called "textletsretrofit" with a column for date, name, email, and password.
Alternatively copy the contents of the "textletsretrofit.sql" file into the "dtypes" database in PHPMyAdmin, note the autoincrementing id column.
Modify the "config.php" accordingly, if you are using XXAMP you won't need to but if it is live you will need to add your database credentials.

Go to app>res>values>strings.xml in Android Studio

For XXAMP or another web server solution stack enter your ip address where the zeros are
<string name="mysqlphpurl">http://000.000.0.00/</string>

For a live version just add the url where the php files are located, make sure the "/" is at the end
<string name="mysqlphpurl">http://www.example.com/</string>

Now you will be able to implement CRUD using Android-Retrofit-PHP-MySQL


Set up REST API Retrofit-Node.js-MongoDB

For this you will need a code editor like Visual Studio Code and to have Node.js and MongoDB installed on your computer.
For a live version you can open an account on heroku.com to host the Node.js server files.
Open the folder "nodejsfiles" in Visual Studio Code and inside it create a fie called ".env" which should look like this

PORT = 3003

url = mongodb+srv://.....

Add the port, in this case 3003, and also the link to your MongoDB collections after the "....."
We don't need to create a table in this case as Node.js and MongoD will do this for us.

Open a terminal and install the following:

git init

npm install dotenv --save

npm i mongodb --save

npm i express --save

npm i mongoose --save

Go to app>res>values>strings.xml in Android Studio and add the following:

<string name="mongonodeurl">http://10.0.2.2:3003</string>

Type npm start into the terminal and you will be able to implement CRUD when you start the Android Studio emulator.

In order to have a live version do the following:

If you have an account on heroku add a file called "Procfile" in the "nodejsfiles" folder with the following text:

web: node index.js

Now type the following into the terminal and write the name you want to call the project after "heroku create"

heroku create example-project-android-node

git add .

git commit -m "first"

git push heroku master

The application will then be created on heroku.com for a live version. 
Open the application on the website and go to settings>Reveal Config Vars. Add the details from the .env file we created earlier.

Go to app>res>values>strings.xml in Android Studio and add the url for your application on heroku

<string name="mongonodeurl">https://.......herokuapp.com/</string>

The CRUD operations using Retrofit-Node.js-MongoDB will work from anywhere now.

Additional features include the ability to view and modify the data based on the timestamps in the databases using CompactCalendarView by SundeepK:
https://github.com/SundeepK/CompactCalendarView

Any data displayed on the RecyclerView can also be exported as a .xls file as a HSSFWorkbook using the pre-installed Apache POI libraries.

Also all CRUD oerations using SQLite are also demonstrated.




