<?php

include("../config.php");

$hostname = $lochost;
$username = $root;
$password = $pswrd;
$databaseName = $prefix."dtypes";

if($_SERVER['REQUEST_METHOD']=='POST') {
  
  $conn = mysqli_connect($hostname, $username, $password, $databaseName);

  $email = $_POST['email'];
  $password = $_POST['password'];

  $query_check_user = "select date, name, email, password from textletsretrofit where email = '$email' and password = '$password'";

  $result = mysqli_query($conn, $query_check_user);

  if(mysqli_num_rows($result) == 0) {
    $response['success'] = false;
    $response['message'] = "User not found";

  } else {

    $row = mysqli_fetch_assoc($result);

    $response['success'] = true;
    $response['message'] = "User logged in successfully";
    
    $response['date'] = $row['date'];
    $response['name'] = $row['name'];
    $response['email'] = $row['email'];
    $response['password'] = $row['password'];
  }
  echo json_encode($response);
  mysqli_close($conn);
}

?>
        
