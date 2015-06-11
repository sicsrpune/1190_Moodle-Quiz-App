<?php
//retrieving question types from the table mdl_question and displaying appropriate options.
//android project

//Initializing sql authentication parameters...
$a="localhost";
$b="username";
$c="password";
$d="dbname";


$link = mysqli_connect($a,$b,$c,$d) or die("Error " . mysqli_error($link)); 



$json = file_get_contents("php://input");
$q_id = json_decode($json)->qid;
//$q_id=2;//hardcode

/*
$q="SELECT question.id,question.name,question.questiontext,question.defaultmark,question.qtype from mdl_question question,mdl_quiz_slots slots where question.id=slots.questionid and slots.quizid=2";
*/
$q="SELECT question.id,question.questiontext,question.qtype,question.defaultmark from mdl_question question,mdl_quiz_slots slots where question.id=slots.questionid and slots.quizid=$q_id";



$result = mysqli_query($link, $q); 





if (mysqli_num_rows($result) == 0) 
{ 
	//$number_of_questions=array("count"=>0);
	//echo json_encode($number_of_questions);
   
}


else // the questions exist
{

	$number_of_questions=array("count"=>mysqli_num_rows($result));
	echo json_encode($number_of_questions);
	echo "\n";
	
	//Getting the resultset and storing in array
	
	$question_id;
	$question_text;
	$question_type;
	$question_defaultmark;
	
	$i=0;
	while($rs=mysqli_fetch_array($result))
	{
		
		
		$question_id[$i]=$rs['id'];
		$question_text[$i]=strip_tags($rs['questiontext']);
		$question_type[$i]=$rs['qtype'];
		$question_defaultmark[$i]=$rs['defaultmark'];
		$i++;
	}
	
	//var_dump($question_id);
	//var_dump($question_text);
	//var_dump($question_type);
	
	
	$number_of_questions=mysqli_num_rows($result);
	
	for($i=0;$i<$number_of_questions;$i++)
	{
	
		if($question_type[$i]=="shortanswer")
		{
			
			$qry="SELECT answer, fraction FROM  mdl_question_answers WHERE question =$question_id[$i]";
			
			$result = mysqli_query($link, $qry) or die("Error " . mysqli_error($link)); 
			
			
			$question_options;
			$question_grading;
			$index=1;
			
			
			while($rs=mysqli_fetch_array($result))
			{
					
					$question_options[$index]=strip_tags($rs['answer']);
					$question_grading[$index]=$rs['fraction'];
					$index++;
					
					
			} 
			
			
			
			
			//creating and printing JSON for shortanswer type
			$short_question=array("type"=>"shortanswer","text"=>$question_text[$i],"marks"=>$question_defaultmark[$i]);
			echo json_encode($short_question);
			echo "\n";
			echo json_encode($question_options);
			echo "\n";
			echo json_encode($question_grading);
			echo "\n";
			
		}
	
		else if($question_type[$i]=="essay")
		{
			
			//creating and printing JSON for ESSAY type
			$essay_question=array("type"=>"essay","text"=>$question_text[$i],"marks"=>$question_defaultmark[$i]);
			echo json_encode($essay_question);
			echo "\n";
			
		}
		
		else if($question_type[$i]=="multichoice" || $question_type[$i]=="truefalse")
		{
			//retrieving the choices based on question id
			
			if($question_type[$i]=="truefalse")
			$question_type[$i]="multichoice";
			
			$qry="SELECT answer, fraction FROM  mdl_question_answers WHERE question =$question_id[$i]";
			
			$result = mysqli_query($link, $qry) or die("Error " . mysqli_error($link)); 
			
			/*
				Now 2 things happen here
				1.Create the mcq basic associative array
				This is created at the end as the type may change from multichoice to checkbox based on the fractional marking
			
			
			
				2.Create the options associative array
				SO we do this first,and change stuff if required.
			*/
			
			
			
			$question_options;
			$question_grading;
			$index=1;
			
			
			while($rs=mysqli_fetch_array($result))
			{
					
					$question_options[$index]=strip_tags($rs['answer']);
					$question_grading[$index]=$rs['fraction'];
					$index++;
					
					/*if($rs['fraction']!=0 && $rs['fraction']!=1)
					{
						$question_type[$i]="checkbox";
					}*/
					
					if(floor( $rs['fraction'] ) != $rs['fraction'])	
					{
						$question_type[$i]="checkbox";
					
					}
			} 
			
			
			
			
			
			$question_mcq=array("type"=>$question_type[$i],"text"=>$question_text[$i],"marks"=>$question_defaultmark[$i]);
			echo json_encode($question_mcq);
			echo "\n";
			echo json_encode($question_options);
			echo "\n";
			echo json_encode($question_grading);
			echo "\n";
			$question_options=Array();
			$question_grading=Array();
			
			//Note - the last \n adds up as an extra index for the arraylist and need to be tackled.
			
		}//else if
		
		
	
	}
	
	
	
	
	
}






//$q_id = 1;//hard code
//selecting question type from mdl_question

/*
$q=mysql_query("SELECT question.id,question.name,question.questiontext,question.defaultmark,question.qtype from mdl_question question,mdl_quiz_slots slots where question.id=slots.questionid and slots.quizid=$q_id");
*/


//$q=mysql_query("select qtype from mdl_question");
/*
if(!$q)
{

	echo mysql_error();
}
*/


/*
$errorQtype;
$questionsubarray;
$counter=1;
*/


/*
if (mysql_num_rows($q) == 0) 
{ 
   //results are empty, do something here 
   //question type field from mdl_question cannot be empty
	$errorQtype[$counter]= "Error in fetching question type.";
}
else
{
	while($r=mysql_fetch_array($q))
	{
			//storing rows in an array
			
			$questionsubarray["name"]=$r['name'];
			$questionsubarray["defaultmark"]=$r['defaultmark'];
			$questionsubarray["questiontext"]=$r['questiontext'];
			$questionsubarray["questiontype"]=$r['qtype'];
			$errorQtype[$r['id']]= $questionsubarray;
			$counter+=1;//iterate the counter 
			
			
			$q2=mysql_query("SELECT question.id,question.name,question.questiontext,question.defaultmark,question.qtype from mdl_question question,mdl_quiz_slots slots where question.id=slots.questionid and slots.quizid=$q_id");

			
			
			//second query
			//$q2=mysql_query("SELECT answer from mdl_question_answers where question=$r[id]");
		
	*/	
//Joining 3 tables 
//1.mdl_quiz_slots
//2.mdl_question
//3.mdl_question_answers

//slots-questionid- id of mdl_question Join1 1
//slots-quizid-->quiz id  



//how the join syntax works
// select whatever u want from whatever table
//specify first table,
//specify what table it joins to
//specify what column of that table it joins to
//then repeate for more tables


//Now i want the question and its options

/*
$q2=mysql_query("SELECT 
			mq.questiontext,
			mqa.answer
			
		FROM mdl_question AS mq
		INNER JOIN  mdl_question_answers as mqa
		ON mq.id=mqa.question
		INNER JOIN  mdl_quiz_slots as mqs
		ON mq.id=mqs.questionid
		
		WHERE
		 mq.id=mqs.questionid AND mqs.quizid=$q_id");
		
		
		
			
			
			while($r2=mysql_fetch_array($q2,MYSQL_NUM))
			{
			
			
				echo strip_tags($r2[0]);
				echo strip_tags($r2[1]);
				echo "<br>";
				//print_r($r2);
				
			}
			
			
			
//	}
//}		
*/
//Job Complete.Terminating Connection
//var_dump($courseHolder);

//echo json_encode($errorQtype);






mysqli_close($link);




//The End



?> 
