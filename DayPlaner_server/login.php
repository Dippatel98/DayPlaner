<?php
if(isset($_POST['USERNAME'])&&isset($_POST['PASSWORD']))
{
	$host='mysql.hostinger.in';
	$user='u145847191_jash';
	$pass='jash@db';
	$dbname='u145847191_dpudb';
	$connection=mysqli_connect($host,$user,$pass,$dbname) or die("ERROR");
	$username=mysqli_real_escape_string($connection,$_POST['USERNAME']);
	$password=mysqli_real_escape_string($connection,$_POST['PASSWORD']);
	$query="SELECT password from user WHERE userName='$username'";
	$result=mysqli_query($connection,$query) or die("ERROR");
	$result_row=mysqli_fetch_row($result);
	
	if($result_row[0]==$password)
	{
		$rString=RandomString();
		$SIDhash=hash("sha256",utf8_encode($rString));
		$insertQuery="UPDATE user SET SID = '$SIDhash' WHERE userName = '$username' LIMIT 1;";
		if(!mysqli_query($connection,$insertQuery))
		{
			die("ERROR ".mysqli_error($connection));
		}
		echo "AUTHENTICATED ";
		die($SIDhash);
	}
	else
	{
		die("FAILED");
	}

}
else
{
	die("ERROR");
}
function RandomString()
{
	$characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ#$%@^*';
	$randstring = "";
	for ($i = 0; $i < 10; $i++) {
		$randstring = $randstring.$characters[rand(0, strlen($characters))];
	}
	return $randstring;
}
?>