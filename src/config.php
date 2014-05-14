<?php
	if(session_status() == PHP_SESSION_NONE) {
		session_start();
	}

	$db_host = 'localhost';
	$db_username = 'root';
	$db_password = 'biscoitos123';
	$db_database = 'sumarios';

	$con = NULL;

	function startConnection() {
		global $db_host, $db_username, $db_password, $db_database, $con;
		if(!($con = mysqli_connect($db_host,$db_username,$db_password,$db_database)))
			echo "Não foi possível aceder à base de dados. Erro: ". $con->error;
	}

	function endConnection() {
		global $con;
		if($con == NULL)
			return;

		mysqli_close($con);
		$con = NULL;
	}

	function getDisciplineList() {
		return "Programaçao de Sistemas Informaticos";
	}

	/**
	 * @param string $type	Allowed types are: success, info, warning, danger
	 * @param string $title Title
	 * @param string $text	Text to be echoed
	 */
	function createAlert($type, $title, $text) {
		echo "<div class='alert alert-$type alert-dismissable'>";
		echo "<button type='button' class='close' data-dismiss='alert' aria-hidden='true'>&times;</button>";
		echo "<strong>$title</strong> $text";
		echo "</div>";
	}

	function get($name) {
		if(isSet($_GET[$name]))
			return $_GET[$name];
		if(isSet($_POST[$name]))
			return $_POST[$name];
		if(isSet($_SESSION[$name]))
			return $_SESSION[$name];
		return NULL;
	}