package com.example.pregunta2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Map;

@SpringBootApplication
public class Pregunta2Application {

	public static void main(String[] args) {
		SpringApplication.run(Pregunta2Application.class, args);
	}

	@Component
	public class FlightSearchRunner implements CommandLineRunner {

		@Value("${avion.api.key}")
		private String apiKey;

		@Override
		public void run(String... args) {
			String destination = "Peru";
			String url = UriComponentsBuilder
					.fromHttpUrl("http://api.aviationstack.com/v1/flights")
					.queryParam("access_key", apiKey)
					.queryParam("arr_city", destination)
					.build()
					.toUriString();

			try {
				System.out.println("Buscando vuelos hacia: " + destination);
				System.out.println("----------------------------------------");

				RestTemplate restTemplate = new RestTemplate();
				Map<String, Object> response = restTemplate.getForObject(url, Map.class);

				if (response != null && response.get("data") != null) {
					java.util.List<Map<String, Object>> flights = (java.util.List<Map<String, Object>>) response.get("data");

					System.out.println("Vuelos encontrados: " + flights.size());
					System.out.println("----------------------------------------");

					for (Map<String, Object> flight : flights) {
						Map<String, Object> airline = (Map<String, Object>) flight.get("airline");
						Map<String, Object> departure = (Map<String, Object>) flight.get("departure");
						Map<String, Object> arrival = (Map<String, Object>) flight.get("arrival");

						System.out.println("Aerolinea: " +
								(airline != null ? airline.get("name") : "N/A"));
						System.out.println("Origen: " +
								(departure != null ? departure.get("airport") : "N/A"));
						System.out.println("Destino: " +
								(arrival != null ? arrival.get("airport") : "N/A"));
						System.out.println("Estado: " + flight.get("flight_status"));
						System.out.println("----------------------------------------");
					}
				}

			} catch (Exception e) {
				System.out.println("Error al buscar vuelos: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
}