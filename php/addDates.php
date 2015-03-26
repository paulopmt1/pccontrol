<?php

include 'Class/AddDates.php';

$type = $_POST['type'];
if ($type != 'START' && $type != 'END'){
    echo "parametros incorrretos";
    exit();
}


function createLockFile(){
    $myfile = fopen("lock", "w");
    fclose($myfile);
}

function removeLockFile(){
    unlink("lock");
}

if ($type == 'START'){
    removeLockFile();
}else{
    createLockFile();
}

$func = new AddDates();
$status = $func->addDate($type);

header('Content-Type: application/json');
echo json_encode(array('status'=>$status));
