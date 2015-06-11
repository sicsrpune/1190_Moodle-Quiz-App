<?php
//Gets the Quiz in a course..


//Initializing sql authentication parameters...
$a="localhost";
$b="username";
$c="password";
$d="dbname";

//establishing connection to database...
if(!$link=mysql_connect($a,$b,$c))
{
	die(mysql_error());
}

//selecting database..
mysql_select_db("$d");


/*
//hardcoding the id value for now... will be received through JSON
#c_id=2
*/



$json = file_get_contents("php://input");
//$c_id = json_decode($json)->cid;
$c_id=2;
//Getting the username


//$q=mysql_query("SELECT id,course,name from mdl_quiz where course=".$c_id);
$q=mysql_query("SELECT m1.id,m1.timelimit,m1.course,m1.name from mdl_quiz m1,mdl_course_modules m2 where m1.id=m2.instance and m2.visible=1 and m1.course=".$c_id);

if(!$q)
{

	echo mysql_error();
}


$courseHolder;
$timelimit;
$counter=1;

if (mysql_num_rows($q) == 0) 
{ 
   //results are empty, do something here 
	
	$courseHolder[$counter]= "There are no quizzes";
}
else
{
	while($r=mysql_fetch_array($q))
	{
			
			$quizsubarray["name"]=$r['name'];
			$quizsubarray["timelimit"]=$r['timelimit'];
			$courseHolder[$r['id']]= $quizsubarray;
			$counter+=1;//iterate the counter 
	}
}		

//Job Complete.Terminating Connection

//var_dump($courseHolder);

echo json_encode($courseHolder);

mysql_close($link);




//The End



?> 
