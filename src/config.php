<?php
	if(session_status() == PHP_SESSION_NONE) session_start();
	if(!isSet($_SESSION["isAuth"])) $_SESSION["isAuth"] = true;

	$db_host = 'localhost';
	$db_username = 'root';
	$db_password = 'biscoitos123';
	$db_database = 'sumarios';

	function startConnection() {
		global $db_host, $db_username, $db_password, $db_database, $con;
		if(!($con = mysqli_connect($db_host,$db_username,$db_password,$db_database)))
			echo "Não foi possível aceder à base de dados. Erro: ". $con->error;
		return $con;
	}

	function getDisciplineList() {
		return "Programaçao de Sistemas Informaticos";
	}

	/**
	 * @param string $type	Allowed types are: success, info, warning, danger
	 * @param string $text	Text to be echoed
	 */
	function createAlert($type, $text) {
		switch($type) {
			case "success": $title = "Sucesso!"; break;
			case "info": $title = "Informação"; break;
			case "warning": $title = "Aviso!"; break;
			case "danger": $title = "Atenção!"; break;
			default: $title = "";
		}

		echo "<div class='alert alert-$type alert-dismissable'>";
		echo "<button type='button' class='close' data-dismiss='alert' aria-hidden='true'>&times;</button>";
		echo "<strong>$title</strong> $text";
		echo "</div>";

		// Clear the alert
		if(isSet($_SESSION["alert"])) $_SESSION["alert"] = NULL;
		if(isSet($_SESSION["alert_desc"])) $_SESSION["alert_desc"] = NULL;
	}

	function get($name) {
		if(isSet($_GET[$name])) return $_GET[$name];
		if(isSet($_POST[$name])) return $_POST[$name];
		if(isSet($_SESSION[$name])) return $_SESSION[$name];
		return NULL;
	}

	function redirect($url) {
		if(headers_sent())
			echo "<script type=\"application/javascript\">window.location = ". $url ."</script>";
		else
			header("Location: ". $url);
	}