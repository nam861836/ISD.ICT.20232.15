//package utils;
//
//import java.io.*;
//import java.lang.reflect.Field;
//import java.lang.reflect.Modifier;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Arrays;
//import java.util.LinkedHashSet;
//import java.util.Set;
//import java.util.logging.Logger;
//
//public class ApplicationProgrammingInterface {
//
//    public static DateFormat DATE_FORMATER = new SimpleDateFormat("yyyyMMddHHmmss");
//    private static Logger LOGGER = Utils.getLogger(Utils.class.getName());
//
//    /**
//     * Get methods.
//     *
//     * @param url   A link to use post method
//     * @param token data to past
//     * @return response
//     * @throws Exception when this exceptional condition occurs
//     */
//    public static String get(String url, String token) throws Exception {
//        LOGGER.info("Request URL: " + url + "\n");
//        HttpURLConnection conn = generateConnection(url, "Get", token);
//        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        String inputLine;
//
//        // ising StringBuilder for the sake of memory and performance
//        StringBuilder respone = new StringBuilder();
//        while ((inputLine = in.readLine()) != null) {
//            System.out.println(inputLine);
//        }
//        respone.append(inputLine + "\n");
//        in.close();
//        LOGGER.info("Respone Info: " + respone.substring(0, respone.length() - 1).toString());
//        return respone.substring(0, respone.length() - 1).toString();
//    }
//
////
////    /**
////     * @param url
////     * @param requestMethod
////     * @param token
////     * @return HttpURLConnection
////     * @throws IOException
////     */
////    private static HttpURLConnection generateConnection(String url, String requestMethod, String token) throws IOException {
////        final var conn = (HttpURLConnection) extracted(url).openConnection();
////        conn.setDoInput(true);
////        conn.setDoOutput(true);
////        conn.setRequestMethod(requestMethod);
////        conn.setRequestProperty("Content-Type", "application/json");
////        if (requestMethod.equals("GET") && token != null) {
////            conn.setRequestProperty("Authorization", "Bearer " + token);
////        }
////        return conn;
////    }
////
////    /**
////     * Post methods.
////     *
////     * @param url  A link to use post method
////     * @param data Data to pass
////     * @return response in String type
////     * @throws IOException when this exceptional condition occurs
////     */
////    public static String post(String url, String data) throws IOException {
////        allowMethods("PATCH");
////        final String payload = data;
////        LOGGER.info("Request Info:\nRequest URL: " + url + "\n" + "Payload Data: " + payload + "\n");
////        HttpURLConnection conn = generateConnection(url, "PATCH", null);
////        Writer writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
////        writer.write(payload);
////        writer.close();
////        BufferedReader in;
////        String inputLine;
////        if (conn.getResponseCode() / 100 == 2) {
////            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
////        } else {
////            in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
////        }
////        StringBuilder response = new StringBuilder();
////        while ((inputLine = in.readLine()) != null) {
////            response.append(inputLine);
////        }
////        in.close();
////        LOGGER.info("Respone Info: " + response.toString());
////        return response.toString();
////    }
////
////    /**
////     * @param url
////     * @return URL
////     * @throws MalformedURLException
////     */
////    private static URL extracted(String url) throws MalformedURLException {
////        final URL line_api_url = new URL(url);
////        return line_api_url;
////    }
//
//    /**
//     * @param methods
//     */
//    private static void allowMethods(String... methods) {
//        try {
//            Field methodsField = HttpURLConnection.class.getDeclaredField("methods");
//            methodsField.setAccessible(true);
//
//            Field modifiersField = Field.class.getDeclaredField("modifiers");
//            modifiersField.setAccessible(true);
//            modifiersField.setInt(methodsField, methodsField.getModifiers() & ~Modifier.FINAL);
//
//            String[] oldMethods = (String[]) methodsField.get(null);
//            Set<String> methodsSet = new LinkedHashSet<>(Arrays.asList(oldMethods));
//            methodsSet.addAll(Arrays.asList(methods));
//            String[] newMethods = methodsSet.toArray(new String[0]);
//
//            methodsField.set(null/* static field */, newMethods);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            throw new IllegalStateException(e);
//        }
//    }
//}
