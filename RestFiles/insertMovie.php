<?php
include 'MoviesConfig.php';
$name = addslashes(strip_tags($_POST['Name']));
$year = addslashes(strip_tags($_POST['Year']));
$genre = addslashes(strip_tags($_POST['Genre']));
$rating = addslashes(strip_tags($_POST['Rating']));
$key = addslashes(strip_tags($_POST['key']));

if ($key != "Sa2eL" or trim($name) == "")
    die("access denied");

$con = mysqli_connect($HostName,$HostUser,$HostPass,$DatabaseName);
// Check connection
if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }

$sql = "insert into Movie(Name, Year, Genre, Rating) values ('$name', '$year', '$genre', '$rating')";
mysqli_query($con,$sql) or
    die ("can't add record");

echo "Record Added";
   
mysqli_close($con);
?> 