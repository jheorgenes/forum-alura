package br.com.alura.forumalura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport //Habilita o suporte do Spring para pegar os parametros da requisição que serão utilizados para realizar paginação
@EnableCaching //Habilitando o uso de cache na aplicação
public class ForumAluraApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForumAluraApplication.class, args);
	}

}
