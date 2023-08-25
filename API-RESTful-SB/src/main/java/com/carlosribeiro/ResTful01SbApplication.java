package com.carlosribeiro;

import com.carlosribeiro.model.Invocador;
import com.carlosribeiro.model.Ficha;
import com.carlosribeiro.repository.InvocadorRepository;
import com.carlosribeiro.repository.FichaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootApplication
public class ResTful01SbApplication implements CommandLineRunner {

	@Autowired
	private FichaRepository fichaRepository;

	@Autowired
	private InvocadorRepository invocadorRepository;

	public static void main(String[] args) {
		SpringApplication.run(ResTful01SbApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Invocador invocador1 = new Invocador("Yatako", "Ouro III");
		invocadorRepository.save(invocador1);

		Invocador invocador2 = new Invocador("AngelofSalvation", "Diamante IV");
		invocadorRepository.save(invocador2);

		Invocador invocador3 = new Invocador("Winidesu", "Ferro III");
		invocadorRepository.save(invocador3);

		Ficha ficha1 = new Ficha(LocalDate.of(2023,5,7), "Ok", "Farmming", invocador1);
		fichaRepository.save(ficha1);
		Ficha ficha2 = new Ficha(LocalDate.of(2023,6,7), "Ok", "Farmming", invocador1);
		fichaRepository.save(ficha2);
		Ficha ficha3 = new Ficha(LocalDate.of(2023,7,7), "Fail", "Farmming", invocador1);
		fichaRepository.save(ficha3);
		Ficha ficha4 = new Ficha(LocalDate.of(2023,6,10), "Ok", "Kiting", invocador2);
		fichaRepository.save(ficha4);
		Ficha ficha5 = new Ficha(LocalDate.of(2023,7,10), "Ok", "Buildar", invocador2);
		fichaRepository.save(ficha5);


	}
}
