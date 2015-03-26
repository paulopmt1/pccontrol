<?php


include './database.php';

/**
 * Description of AddDates
 *
 * @author paulo
 */
class AddDates {

    private $conn;

    public function __construct() {
        $db = new Database();
        $this->conn = $db->getConn();
    }
    
    public function addDate($type){
        $newDate = new DateTime();
        $datetime = $newDate->format('Y-m-d H:i:s');
        
        $sql = "INSERT INTO times_on_day (type, date_action) value ('$type', '$datetime')";
        $status = $this->conn->query($sql);
        
        return $status;
    }
}
