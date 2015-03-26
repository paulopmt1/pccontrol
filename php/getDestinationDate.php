<?php

include 'Class/SystemFunctions.php';

$func = new SystemFunctions();

$remainderMinutes = $func->getRemainderMinutes();

$startTime = new DateTime();
$startTime->add(new DateInterval("PT{$remainderMinutes}M"));


header('Content-Type: application/json');
echo json_encode(array('date'=>$startTime->format('Y-m-d H:i:s')));
