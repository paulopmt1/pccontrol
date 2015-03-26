<?php

include 'Class/SystemFunctions.php';

$func = new SystemFunctions();

// Responde o usuário
if ($func->canIUse()){
    echo "livre";
    exit();
}else{
    echo "bloqueado";   
}