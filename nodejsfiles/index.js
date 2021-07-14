// INSTALL THE FOLLOWING LIBRARIES
// git init
// npm install dotenv --save
// npm i mongodb --save
// npm i express --save
// npm i mongoose --save

// CREATE A PROJECT ON HEROKU WITH A UNIQUE NAME
// heroku create example-project-android-node
// git add .
// git commit -m "first"
// git push heroku master

const express = require('express')
// THIS "app" VARIABLE WILL HOLD THE EXPRESS APPLICATION
const app = express()
const mongoose = require('mongoose');
const mongoClient = require('mongodb').MongoClient

// OUR POST REQUEST HAS BODY OF TYPE JSON SO WE ENABLE JSON PARSING IN EXPRESS (TURNED OFF BY DEFAULT)
app.use(express.json({ extended: false}));
app.use(express.urlencoded({ extended: false}));

// THIS VARIABLE WILL HOLD THE PATH OF THE MONGODB SERVER ON THE LOCAL MACHINE
// TYPE "mongodb" IN THE TERMINAL TO CHECK
//const url = "mongodb://localhost:27017"

// THE FOLLOWING CODE IS USED FOR DEPLOYMENT
//************************************************************************* */

// PLACE A .env FILE IN THE FOLDER WITH THE PORT NUMBER AND YOUR MONGODB DATABSE URL
// PORT = 3003
// url = mongodb+srv://......

require("dotenv").config();
mongoose.set("debug", true);
mongoose.Promise = global.Promise;

app.get("/", (req, res) => {
    res.send("Hello World");
});

const connectDB = async () => {
    try {
        await mongoose.connect(process.env.url, {
            useNewUrlParser: true,
            useUnifiedTopology: true,
            useCreateIndex: true,
            useFindAndModify: true
        })
        console.log("MongoDB is Connected");
    } catch (error) {
        console.log(error);
    }
}
connectDB();

// WE ADD THE FOLLOWING FOR DEPLOYMENT:
// mongoClient.connect(process.env.url, {useNewUrlParser: true, useUnifiedTopology: true}, (err, db) => {
// FOR TESTING
// Original - mongoClient.connect(url, (err, db) => {

//************************************************************************* */

// THE FIRST PARAMETER PASSED IS THE "url" FOR THE SERVER
// THE SECOND PARAMTER IS A CALLBACK FUNCTION TO BE EXECUTED ONCE THE MONGO CLIENT IS CONNECTED
// IT HAS TWO PARAMETERS "err" FOR ERROR AND THE MONGO CLIENT WHICH WE WILL CALL "db"
mongoClient.connect(process.env.url, {useNewUrlParser: true, useUnifiedTopology: true}, (err, db) => {

    // WE FIST CHECK FOR THE CONDITION "if (err)" 
    if (err) {
        console.log("Error while connecting mongo client")
    } else {
        // WE CREATE A NEW VARAIABLE "MyDb" AND USING THE "db" VARIABLE WE WILL CALL "db.db()"
        // THIS FUNCTION EXPECTS A PARAMETER 'myDb' WHICH WILL BE A STRING 
        // IF A DATABASE CALLED 'myDb' DOES NOT EXIST A DATABSE WITH THAT NAME WILL BE CREATED
        // USING THE myDb.myCollection() FUNCTION CONNECT TO THE "myCollection" WHICH IS A TABLE IN MONGODB
        // THE PARAMETER IT EXPECTS IS A STRING  FOR THE NAME OF THE TABLE 'myTable'
        const myDb = db.db('dbAndroidNode')
        const myCollection = myDb.collection('tableAndroidNode')
        // TO HANDLE THE POST REQUESTS CALL "app.post()" FUNCTION
        // THE FIRST PARAMETER IS A STRING WHICH WILL BE THE ROUTE FOR THE HTTP REQUEST '/signup'
        // THE SECOND PARAMETER IS A CALLBACK FUNCTION TO BE EXECUTED WHEN ANY REQUEST TO THIS ROUTE COMES
        // THE FIRST IS THE REQUEST "req" AND THE SECOND IS THE RESPONSE "res"

        // HERE WE INSERT ONE ENTRY INTO THE DATABASE
        app.post('/insert', (req, res) => {
            // INSIDE THIS CALLBACK FUNCTION CREATE A VARIABLE "newUser" OBJECT HAS SEVERAL PROPERTIES
            // THIS "name" PROPERTY WILL GET ITS VALUE BY THE REQUEST BODY
            const newUser = {
                date: req.body.date,
                name: req.body.name,
                email: req.body.email,
                password: req.body.password
            }
            // WHEN WE PRESS THE SIGNUP BUTTON IN THE ANDROID APLICATION A POST REQUEST IS SENT TO THIS SERVER
            // THAT POST REQUEST WILL HAVE A BODY CONTAINING "name", "email", and "password"
            // WITH ALL THAT WE ARE CREATING AN OBJECT CALLED "newUser"
            // WE NOW NEED TO ADD "newUser" INTO OUR DATABASE BUT ONLY IF THE EMAIL IS UNIQUE
            const query = { email: newUser.email }
            // USING "myCollection.findOne" METHOD WE FIND AN OBJECT WHICH HAS SAME "email" AS "newUser"
            // PASS "query" INSIDE THE FUNCTION
            // THE SECOND PARAMTER IS A CALLBACK FUNCTION EXECUTED ONCE THE QUERY IS COMPLETE
            myCollection.findOne(query, (err, result) => {
                // IF NO OBJECTS CONTAIN THIS EMAIL THE "result" IS GOING TO BE "null"
                // IF SO INSERT "newUser" INTO THE DATABSE
                // TO INSERT A NEW VALUE IN A TABLE CALL "myCollection.insertOne()" FUNCTION
                // AFTER THE DATA ARE INSERTED INTO MONGODB SEND HTTP CODE 200 TO ANDROID CLIENT
                if (result == null) {
                    myCollection.insertOne(newUser, (err, result) => {
                        res.status(200).send()
                    })
                // IF THE RESULT IS NOT NULL AND OBJECT ALREADY EXISTS THAT COMTAINS "email"
                // IF THAT HAPPENS SEND THE RESPONSE CODE 400 MEANING BAD REQUEST
                } else {
                    res.status(400).send()
                }
            })
        })

        // HERE WE SELECT ONE ENTRY FROM THE DATABASE
        app.post('/selectuser', (req, res) => {
            const query = {
                email: req.body.email, 
                password: req.body.password
            }
            myCollection.findOne(query, (err, result) => {
                // IF THE REQUEST IS "!= null" THEN THE GIVEN "email" AND "password" ARE IN THE DATABASE
                // WE THEN SEND THAT INFORMATION TO THE ANDROID APPLICATION
                if (result != null) {
                    const objToSend = {
                        date: result.date,
                        name: result.name,
                        email: result.email,
                        password: result.password
                    }
                    // WE NOW SEND THE OBJECT TO THE ANDROID CLIENT
                    // WE CANNOT DIRECTLY SEND "objToSend" SO WE CONVERT IT TO A STRING
                    res.status(200).send(JSON.stringify(objToSend))
                } else {
                    res.status(404).send()
                }
            })
        })

        // HERE WE SELECT ALL ENTRIES FROM THE DATABASE
        app.get('/selectall', (req, res) => {
            // GET ALL ENTRIES
            myCollection.find().toArray(function(err, result) {
                if (err) throw err;
                //console.log(result);
                res.status(200).send(JSON.stringify(result))
                // THE JSON SHOULD APPEAR AS BELOW
                // const result = [{"date":"1625564252","name":"Henry","email":"henry@email.com","password":"123abc"},
                //                 {"date":"1625564252","name":"Marta","email":"marta@email.com","password":"123abc"}];

            })
        })

        // HERE WE DELETE AN ENTRY BASED ON EMAIL
        app.post('/deleteuser', (req, res) => {
            var myquery = { email: req.body.email };
            myCollection.deleteOne(myquery, function(err, obj) {
              if (err) throw err;
              console.log("1 document deleted");
              res.status(200).send()
            })
        })

        app.get('/deleteall', (req, res) => {
            myCollection.drop(function(err, delOK) {
                if (err) throw err;
                if (delOK) console.log("Collection deleted");
            })
        })

        // HERE WE UPDATE AN ENTRY BASED ON EMAIL
        app.post('/update', (req, res) => {
            
            const newUser = {
                name: req.body.name,
                email: req.body.email,
                password: req.body.password
            }
            const myquery = { email: newUser.email }
            var newvalues = { $set: {name: newUser.name, password: newUser.password } };

            myCollection.updateOne(myquery, newvalues, function(err, upOK) {
                if (err) throw err;
                if (upOK) console.log("Entry updated");
                res.status(200).send()
            })
        })

        // HERE WE CHECK IF THERE ARE ANY COLLECTIONS IN THE DATABASE
        app.get('/checkexists', (req, res) => {
            myDb.listCollections().toArray(function(err, items){
                if (err) throw err
                console.log(items)
                res.status(200).send() 
                if (items.length == 0)
                console.log("No collections in database")  
                res.status(404).send()
            })
        })
    }
})

// WHEN WE CALL "app.listen" FUNCTION THE FIRST PARAMETER IN THIS FUNCTION IS THE PORT NUMBER
app.listen(process.env.PORT, () => {
    console.log("Listening on port " + process.env.PORT)
})