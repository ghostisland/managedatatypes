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

if($_SERVER['REQUEST_METHOD']=='GET'){ 

    // THIS FETCHES ALL date ROWS
  
  //$dateSelect = $connection->prepare("SELECT date FROM textupwork WHERE reg = $texto");
  $dateSelect = $connection->prepare("SELECT date FROM textletsretrofit");
  $dateSelect->execute();
  $dateResult = $dateSelect->fetchall(PDO::FETCH_NUM);
  $dateValues = array_values($dateResult);
  $dateString = json_encode($dateValues);
  $dateBracom = str_replace('],[', ',', $dateString);
  $dateOpenbr = str_replace('[[', '[', $dateBracom);
  $dateClosbr = str_replace(']]', ']', $dateOpenbr); 
  $dateDecode = json_decode($dateClosbr);
  
  // THIS FETCHES ALL name ROWS
  //$nameSelect = $connection->prepare("SELECT name FROM textupwork WHERE name = $texto");
  $nameSelect = $connection->prepare("SELECT name FROM textletsretrofit");
  $nameSelect->execute();
  $nameResult = $nameSelect->fetchall(PDO::FETCH_NUM);
  $nameValues = array_values($nameResult);
  $nameString = json_encode($nameValues);
  $nameBracom = str_replace('],[', ',', $nameString);
  $nameOpenbr = str_replace('[[', '[', $nameBracom);
  $nameClosbr = str_replace(']]', ']', $nameOpenbr); 
  $nameDecode = json_decode($nameClosbr);
  
  // THIS FETCHES ALL email ROWS
  
  //$emailSelect = $connection->prepare("SELECT email FROM textupwork WHERE reg = $texto");
  $emailSelect = $connection->prepare("SELECT email FROM textletsretrofit");
  $emailSelect->execute();
  $emailResult = $emailSelect->fetchall(PDO::FETCH_NUM);
  $emailValues = array_values($emailResult);
  $emailString = json_encode($emailValues);
  $emailBracom = str_replace('],[', ',', $emailString);
  $emailOpenbr = str_replace('[[', '[', $emailBracom);
  $emailClosbr = str_replace(']]', ']', $emailOpenbr); 
  $emailDecode = json_decode($emailClosbr);
  
  // THIS FETCHES ALL password ROWS
  
  //$passwordSelect = $connection->prepare("SELECT password FROM textupwork WHERE reg = $texto");
  $passwordSelect = $connection->prepare("SELECT password FROM textletsretrofit");
  $passwordSelect->execute();
  $passwordResult = $passwordSelect->fetchall(PDO::FETCH_NUM);
  $passwordValues = array_values($passwordResult);
  $passwordString = json_encode($passwordValues);
  $passwordBracom = str_replace('],[', ',', $passwordString);
  $passwordOpenbr = str_replace('[[', '[', $passwordBracom);
  $passwordClosbr = str_replace(']]', ']', $passwordOpenbr); 
  $passwordDecode = json_decode($passwordClosbr);
    
  $mapglobs = array_map(null, $dateDecode, $nameDecode, $emailDecode, $passwordDecode);
  $jsonglob = array();
  foreach ($mapglobs as $mapglob) {
  $dataglob =array();
  $dataglob['date'] = $mapglob[0] = $mapglob[0];
  $dataglob['name'] = $mapglob[1] = $mapglob[1];
  $dataglob['email'] = $mapglob[2] = $mapglob[2];
  $dataglob['password'] = $mapglob[3] = $mapglob[3];
  $jsonglob[] = $dataglob;
  }
  
  print(json_encode($jsonglob));
}

?>
        
