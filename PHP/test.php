<?php


unset($CFG);
global $CFG;
$CFG = new stdClass();

$CFG->dbtype    = 'mysqli';
$CFG->dblibrary = 'native';
$CFG->dbhost    = 'localhost';
$CFG->dbname    = 'moodle';
$CFG->dbuser    = 'root';
$CFG->dbpass    = 'neehar92';
$CFG->prefix    = 'mdl_';
$CFG->dboptions = array (
  'dbpersist' => 0,
  'dbport' => 3300,
  'dbsocket' => '',
);

//for emulator
$CFG->wwwroot   = 'http://10.0.2.2/moodle';

//$CFG->wwwroot   = 'http://localhost/moodle';
$CFG->dataroot  = '/var/moodledata';
$CFG->admin     = 'admin';

$CFG->directorypermissions = 0777;

require_once(dirname(__FILE__) . '/lib/setup.php');

// There is no php closing tag in this file,
// it is intentional because it prevents trailing whitespace problems!



$json = file_get_contents("php://input");
	
$file = "feedback1.html";
$username = json_decode($json)->username;
$password = json_decode($json)->password;

/*
//hardcoded Values for testing
$username="nikhil";
$password="qQ!123456";
*/
/*
$username="student1";
$password="Student!123";
//*/


$statusArray="";

$check_auth=authenticate_user_login($username, $password);

//authenticate_user_login is the authentication function used in moodle that returns an associative array if login is successful.





//$check_auth is an object with values.
//try var_dump($check_auth)
//var_dump($check_auth);

//creating an array that will hold the object values, to make it easier to extract the object elements

$arr=Array($check_auth);



//var_dump($arr);
if($check_auth)
{
    $statusArray = array("status"=>"success");
	//$statusArray="success";

	
	//retrieving important data such as the id and username

	foreach ($arr as $cur)
	{
	  $statusArray['id'] = $cur->id;
	  //not required//$statusArray['username'] = $cur->username;
	
	}

}
else
{
	
    $statusArray = array("status"=>"fail");
	$statusArray['id'] = "null";

	//$statusArray="no";
}




//returns username,user id,status(success or fail).. if its a fail then username and id not sent
echo json_encode($statusArray);


?>
