<?php

include("../config.php");

$hostname = $lochost;
$username = $root;
$password = $pswrd;
$databaseName = $prefix."dtypes";

try { $connection = new PDO("mysql:host=$hostname;dbname=$databaseName", $username, $password);
    $connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);}
    catch(PDOException $e) { die("OOPs something went wrong");}

//***************************************************************************************************

if($_SERVER['REQUEST_METHOD']=='POST'){

$date = $_POST["date"];
$name = $_POST["name"];
$email = $_POST["email"];
$password = $_POST["password"];

$sql = "UPDATE textletsretrofit SET name = '$name', password = '$password' WHERE email = '$email'";
$connection->exec($sql);
$connection = null;

}

?>
        
