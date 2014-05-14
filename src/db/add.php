<?php
	include('../config.php');

	if(!isSet($_SESSION["login"]) && false) {
		header("Location: ../login.php");
		die();
	}

	global $con,$db_database;

	$licao = get("licao");
	$disciplina = get("disciplina");
	$modulo = get("modulo");
	$planificacao = get("planificacao");
	$data = get("data");

	$update = get("update");

	if(!isSet($licao) || !isSet($disciplina) || !isSet($planificacao)) {
		$_SESSION["err"] = "Impossível adicionar lição";
	} else {
		if($update)
			$query = "UPDATE $db_database.licoes SET licao=\"". $licao ."\", disciplina=". $disciplina .", modulo=\"". $modulo ."\", planificacao=\"". htmlspecialchars($planificacao) ."\", data=\"". $data ."\" WHERE licao=\"". $licao ."\"";
		else
			$query = "INSERT INTO $db_database.licoes (licao, disciplina, modulo, planificacao, data) VALUES (\"". $licao ."\", ". $disciplina .", \"". $modulo ."\", \"". htmlspecialchars($planificacao) ."\", \"". $data ."\")";

		startConnection();
		if(!mysqli_query($con, $query))
			echo "Erro: ". $con->error;
		endConnection();

		$_SESSION["succ"] = "Lição adicionada com sucesso.";
	}

	header("Location: ../index.php");