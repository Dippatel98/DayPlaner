<?php
if(isset($_POST["q"])&&isset($_POST["SID"])&&isset($_POST["UID"]))
{
	$host='mysql.hostinger.in';
	$user='u145847191_jash';
	$pass='jash@db';
	$dbname='u145847191_dpudb';
	$connection=mysqli_connect($host,$user,$pass,$dbname) or die("ERROR");
	$username=mysqli_real_escape_string($connection,$_POST['UID']);
	$sid=mysqli_real_escape_string($connection,$_POST['SID']);
	$query="SELECT SID from user WHERE userName = '$username' LIMIT 1;";
	$result=mysqli_query($connection,$query) or die("ERROR");
	$result_row=mysqli_fetch_row($result);
	
	if($result_row[0]==$sid)
	{
		$path="./uploads/".hash("crc32",utf8_encode($username))."/planner.xml";
		$file=fopen($path,"w");
		fwrite($file,$_POST["q"]);
		fclose($file);
		die("OK");
	}
	else
	{
		die("FAILED ".$result_row[0]." ".$sid);
	}


}
else
{
	die("Problem");
}
?>