<?php
if(isset($_POST["q"])&&isset($_POST["SID"])&&isset($_POST["UID"]))
{
	die($_POST["SID"]." ".$_POST["UID"]." ".$_POST["q"]);
}
else
{
	die("Problem");
}
?>