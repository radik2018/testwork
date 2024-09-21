public class Main {
    public static void main(String[] args) {

        CrptApi.Description description = new CrptApi.Description("1234567890");
        CrptApi.Document document = new CrptApi.Document(description, "doc123", "STATUS");

        CrptApi crptApi = new CrptApi(60000, 5);

        crptApi.createDocument(document, "signature");
    }
}