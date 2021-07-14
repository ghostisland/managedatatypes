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

if($_SERVER['REQUEST_METHOD']=='POST') {
  
  $conn = mysqli_connect($hostname, $username, $password, $databaseName);

  $date = $_POST["date"];
  $name = $_POST["name"];
  $email = $_POST["email"];
  $password = $_POST["password"];

  $query_check_user = "select email from textletsretrofit where email = '$email'";

  $result = mysqli_query($conn, $query_check_user);

  if(mysqli_num_rows($result) == 0) {

  	$sql = "INSERT INTO textletsretrofit (date, name, email, password) VALUES ('$date', '$name', '$email', '$password')";
    $connection->exec($sql);
    $connection = null;

  } else {

  	$response['email'] = $email;

  }
  echo json_encode($response); //{"email":"hoo@email.com"}
  mysqli_close($conn);
}



?>
        
