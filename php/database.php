<?php



class Database{
    private $servername = "localhost";
    private $username = "root";
    private $password = "root";
    private $conn = false;
    
    public function __construct() {
        try {
            $this->conn = new PDO("mysql:host={$this->servername};dbname=pccontrol", $this->username, $this->password);
            // set the PDO error mode to exception
            $this->conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        } catch (PDOException $e) {
            echo "Connection failed: " . $e->getMessage();
        }
    }
    
    
    public function getConn(){
        return $this->conn;
    }
    
}


