<?php
include 'MoviesConfig.php';

$username = addslashes(strip_tags($_POST['Username']));
$password = addslashes(strip_tags($_POST['Password']));

$con = mysqli_connect($HostName,$HostUser,$HostPass,$DatabaseName);
// Check connection
if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }

$result=mysqli_query($con,"SELECT * FROM Accounts WHERE Username = '$username'"); 

if(mysqli_num_rows($result)!=0)
       {
        echo"Username Already Exists";
       }
     else
        {  
        // excecute insert query 
        $sql = "insert into Accounts(Username, Password) values ('$username', '$password')";
        
        echo "Record Added";
        }

   
mysqli_close($con);
?> 