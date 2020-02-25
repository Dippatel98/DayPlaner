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
	$email=mysqli_real_escape_string($connection,$_POST['EMAIL']);
	$query="SELECT email from user WHERE userName='$username'";
	$result=mysqli_query($connection,$query) or die("ERROR");
	$result_row=mysqli_fetch_row($result);
	
	if(count($result_row)>0)
	{
		$n=count($result_row);
		while($n>0)
		{
			if($result_row[$n]==$email)
			{
				die("USERNAME&PASS");
			}
			$n--;
		}
		die("USERNAME");

	}
	else
	{
		$query="INSERT INTO user (userName, password, email, SID) VALUES ('$username', '$password', '$email', '');";
		$result=mysqli_query($connection,$query) or die("ERROR ".mysqli_error($connection));
		if(mkdir("./uploads/".hash("crc32",utf8_encode($username))."/",0777,true)&&copy("./uploads/planner.xml","./uploads/".hash("crc32",utf8_encode($username))."/planner.xml"))
		{
			die("SUCCESS");
		}
		else
		{
			die("ERROR");			
		}
	}

}
else
{
	die("ERROR");
}

?>