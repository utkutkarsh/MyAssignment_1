import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

 class WeatherProgram {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Options:");
            System.out.println("1. Get weather");
            System.out.println("2. Get Wind Speed");
            System.out.println("3. Get Pressure");
            System.out.println("0. Exit");

            System.out.print("Enter your choice (0-3): ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character after reading the integer

            if (option == 1) {
                System.out.print("Enter the date (yyyy-MM-dd hh:mm:ss): ");
                String date = scanner.nextLine();
                System.out.println("Temperature =  "+fetchWeatherData(date, "temperature"));
            } else if (option == 2) {
                System.out.print("Enter the date (yyyy-MM-dd hh:mm:ss): ");
                String date = scanner.nextLine();
                System.out.println("Wind Speed =  "+ fetchWeatherData(date, "wind_speed"));
            } else if (option == 3) {
                System.out.print("Enter the date (yyyy-MM-dd hh:mm:ss): ");
                String date = scanner.nextLine();
                System.out.println("Pressure =  "+ fetchWeatherData(date, "pressure"));
            } else if (option == 0) {
                System.out.println("Exiting the program.");
                break;
            } else {
                System.out.println("Invalid option. Please choose a valid option (0-3).");
            }
        }

        scanner.close();
    }

    private static Double fetchWeatherData(String date, String dataKey) {
        String url = "https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=b6907d289e10d714a6e88b30761fae22";
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request;

        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            System.err.println("Invalid URL: " + e.getMessage());
            return null;
        }

        try {

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());


            JSONObject object = new JSONObject(response.body());

            JSONArray list = object.getJSONArray("list");
            for(int i=0;i<list.length();i++){
                JSONObject listObject = list.getJSONObject(i);
                
                if(listObject.get("dt_txt").equals(date))
                {
                    
                    if(Objects.equals(dataKey, "temperature")){
                        return listObject.getJSONObject("main").getDouble("temp");
                    }else if(Objects.equals(dataKey, "wind_speed")){
                        return  listObject.getJSONObject("wind").getDouble("speed");
                    } else if (Objects.equals(dataKey, "pressure")) {
                        return listObject.getJSONObject("main").getDouble("pressure");
                    }else{
                        System.exit(0);
                        return null;
                    }
                }
            }

        } catch (Exception e ) {
            return null;
        }
        return null;
    }
}
