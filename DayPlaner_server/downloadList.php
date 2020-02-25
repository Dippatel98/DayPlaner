<?php
if(isset($_GET["SID"])&&isset($_GET["UID"]))
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
		$query="SELECT Title,ID from MeetMe where Status='PENDING' and FirstParty='$username';";
		$result=mysqli_query($connection,$query) or die("ERROR");
		echo(length($result)."\n");
		$i=0;
		for($i=0;$i<length($result);$i++)
		{
			$result_row=mysqli_fetch_row($result);
			echo($result_row[0]."\n".$result_row[1]."\n");
		}
	}
	else
	{
		die("FAILED");
	}


}
else
{
	die("Problem");
}
?>