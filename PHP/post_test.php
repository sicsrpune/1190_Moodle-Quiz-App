<?php

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






$json = file_get_contents("php://input");
//$c_id = json_decode($json)->cid;
$q_id=json_decode($json)->qid;
$u_id=json_decode($json)->uid;
$grade=json_decode($json)->grade;
$timemodified=json_decode($json)->timemodified;
/*
$q_id=5;
$u_id=5;
$grade=3.45;
$timemodified=1234;
*/


//$q=mysql_query("SELECT id,course,name from mdl_quiz where course=".$c_id);
$q=mysql_query("INSERT INTO test(qid,uid,grade,time) values(".$q_id.",".$u_id.",".$grade.",".$timemodified.");");
if(!$q)
{

	echo mysql_error();
}




//Job Complete.Terminating Connection

echo "True";

mysql_close($link);




//The End



?> 
