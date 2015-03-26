<?php

include './database.php';

/**
 * Description of SystemFunctions
 *
 * @author paulo
 */
class SystemFunctions {

    private $conn;

    public function __construct() {
        $db = new Database();
        $this->conn = $db->getConn();
    }

    /**
     * Separa ítens de datas START e END
     * @param type $allDates
     * @return type
     */
    private function formatItems($allDates) {
        $items = [];

        foreach ($allDates as $date) {
            $items[$date['type']][] = $date['date_action'];
        }

        return $items;
    }

    /**
     * Obtém a data acima dessa mais próxima
     * @param type $actualDateTime
     */
    private function getNextTime($actualDateTime, $dates) {
        if (count($dates) == 0)
            return new DateTime('America/Sao_Paulo');

        $returnDate = new DateTime('America/Sao_Paulo');
        $maxMinutes = 0;

        foreach ($dates as $date) {
            $compareDate = new DateTime($date);

            if ($compareDate > $actualDateTime) {
                $diff = date_diff($actualDateTime, $compareDate);

                $minutes = $diff->i;
                $minutes += ($diff->h * 60);

                if ($minutes < $maxMinutes || $maxMinutes == 0) {
                    $maxMinutes = $minutes;
                    $returnDate = $compareDate;
                }
            }
        }

        return $returnDate;
    }

    /**
     * Obtém lista de tempos usados no dia de hoje
     * @global type $conn
     * @return type
     */
    private function getTodayUsedTimes() {
        $date = new \DateTime();
        $formated = $date->format('Y-m-d 00:00:00');

        // Obtém datas de hoje apenas
        $sql = "SELECT * FROM times_on_day where date_action > '$formated'";

        $allDates = $this->conn->query($sql);
        $formattedDates = $this->formatItems($allDates);

        return $formattedDates;
    }

    /**
     * Obtém o tempo total em minutos já usados para o dia de hoje
     * @param type $formattedDates
     * @return type
     */
    private function getTotalUsedTime() {
        $formattedDates = $this->getTodayUsedTimes();
        $totalMinutes = 0;

        foreach ($formattedDates['START'] as $firstStartDate) {
            $thisDate = new DateTime($firstStartDate);
            $nextDate = $this->getNextTime($thisDate, $formattedDates['END']);

            $diff = date_diff($thisDate, $nextDate);
            $minutes = $diff->i;
            $minutes += ($diff->h * 60);

            $totalMinutes += $minutes;
        }

        return $totalMinutes;
    }

    /**
     * Obtém o nome do dia da semana à partir de uma data
     * @param type $dateTime
     * @return type
     */
    private function getDayOfWeek($dateTime) {
        $m = $dateTime->format('m');
        $d = $dateTime->format('d');
        $y = $dateTime->format('Y');

        return jddayofweek(cal_to_jd(CAL_GREGORIAN, $m, $d, $y), 1);
    }

    /**
     * Obtém minutos por dia para semana
     * @param type $weekDay
     * @return type
     */
    private function getMinutesForWeekDay($weekDay) {
        $sql = "SELECT minutes FROM minutes_per_day WHERE week_day = '$weekDay'";

        $data = $this->conn->query($sql)->fetch();
        return $data['minutes'];
    }

    /**
     * Verifica se o sistema foi liberado hoje
     * @return boolean
     */
    private function hasStartedToday() {
        $formattedDates = $this->getTodayUsedTimes();

        if (count($formattedDates) == 0)
            return false;

        return true;
    }

    /**
     * Verifica se pode usar o sistema
     */
    public function canIUse() {
        if (!$this->hasStartedToday()) {
            return false;
        }

        $dayOfWeek = $this->getDayOfWeek(new DateTime('America/Sao_Paulo'));
        $todayMinutes = $this->getMinutesForWeekDay($dayOfWeek);

        $usedMinutesToday = $this->getTotalUsedTime();


        if ($usedMinutesToday > $todayMinutes) {
            return false;
        }
        
        // Se existir o arquivo de lock, não permite acesso
        if ($this->getStatusFromFile()){
            return false;
        }

        return true;
    }
    
    /**
     * Obtém Status a partir do arquivo de lock
     */
    public function getStatusFromFile(){
        if (is_file("lock")){
            return true;
        }
        
        return false;
    }
    
    /**
     * Obtém quantos minutos livres ainda restam para hoje
     * @return int
     */
    public function getRemainderMinutes(){
        $dayOfWeek = $this->getDayOfWeek(new DateTime('America/Sao_Paulo'));
        $todayMinutes = $this->getMinutesForWeekDay($dayOfWeek);

        $usedMinutesToday = $this->getTotalUsedTime();
        
        $minutes = $todayMinutes - $usedMinutesToday;
        if ($minutes < 0) return 0;
        
        return $minutes;
    }

}
