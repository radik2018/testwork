import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class CrptApi {

    private final HttpClient client;
    private final long intervalMillis;
    private final ObjectMapper objectMapper;
    private final int requestLimit;

    public CrptApi(long intervalMillis, int requestLimit) {
        this.client = HttpClient.newHttpClient();
        this.intervalMillis = intervalMillis;
        this.objectMapper = new ObjectMapper();
        this.requestLimit = requestLimit;
    }

    public void createDocument(Document document, String signature) {
        for (int i = 0; i < requestLimit; i++) {
            try {
                String url = "https://ismp.crpt.ru/api/v3/lk/documents/create";

                String requestBody = objectMapper.writeValueAsString(document);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI(url))
                        .timeout(Duration.ofSeconds(30))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println("Response: " + response.body());

                Thread.sleep(intervalMillis);

            } catch (JsonProcessingException e) {
                System.err.println("Error processing JSON: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error sending request: " + e.getMessage());
            }
        }
    }

    public record Document(Description description, String docId, String docStatus) {

        public Description getDescription() {
            return description;
        }

        public String getDocId() {
            return docId;
        }

        public String getDocStatus() {
            return docStatus;
        }
    }

    public record Description(String participantInn) {

        public String getParticipantInn() {
            return participantInn;
        }
    }
}