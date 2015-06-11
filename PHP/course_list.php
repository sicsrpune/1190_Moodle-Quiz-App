<?php
//Gets the COurses that the user is enrolled in..


//Initializing sql authentication parameters...
$a="localhost";
$b="root";
$c="neehar92";


//establishing connection to database...
if(!$link=mysql_connect($a,$b,$c))
{
	die(mysql_error());
}

//selecting database..
mysql_select_db("moodle");


/*
//hardcoding the id value for now... will be received through JSON
$u_id=4;
*/

$json = file_get_contents("php://input");
$u_id = json_decode($json)->id;

//Getting the username
$q=mysql_query("SELECT c.fullname,c.id FROM mdl_user u INNER JOIN mdl_user_enrolments ue ON ue.userid = u.id INNER JOIN mdl_enrol e ON e.id = ue.enrolid INNER JOIN mdl_course c ON e.courseid = c.id where u.id=$u_id");


if(!$q)
{

	echo mysql_error();
}


$courseHolder;
$counter=1;

if (mysql_num_rows($q) == 0) 
{ 
   //results are empty, do something here 
	
	$courseHolder[$counter]= "You are not enrolled in any course";
}
else
{
	while($r=mysql_fetch_array($q))
	{
		
			$courseHolder[$r['id']]= $r['fullname'];
		
			$counter+=1;//iterate the counter 
	}
}		

//Job Complete.Terminating Connection

//var_dump($courseHolder);

echo json_encode($courseHolder);
mysql_close($link);




//The End



?> 
