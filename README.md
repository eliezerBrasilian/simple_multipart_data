# simple_multipart_data

This project provides a custom implementation for handling multipart/form-data in Java.

## Installation

To use this library in your project, install it using Cryxie:

```bash
cryxie install simple_multipart_data@0.0.1
```

## Usage

Import the main class into your Java code:

```java
import simple_multipart.brasilian.MultipartData;
import simple_multipart.brasilian.Part; // Import needed for the example

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.util.List;

public class UsageExample {

    // Assuming you have a base URL defined somewhere
    public static final String BASE_URL = "http://your-server.com";

    public void sendData(File readmeFile, File jarFile, String packageJson, String bearerToken) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/package")) // Example endpoint
                .header("Authorization", "Bearer " + bearerToken)
                .header(MultipartData.CONTENT_TYPE, MultipartData.MultipartFormData)
                .header("Accept", "application/json")
                .POST(createMultipartBody(readmeFile, jarFile, packageJson))
                .build();

        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            processResponse(response); // Implement your response processing logic

        } catch (IOException e) {
             // Handle connection errors or other IOExceptions
            System.err.println("Error sending request: " + e.getMessage());
            throw e;
        }
    }

    private HttpRequest.BodyPublisher createMultipartBody(File readmeFile, File jarFile, String packageJson)
            throws IOException {
        var parts = List.of(
                new Part("package", null, "application/json", packageJson.getBytes()),
                new Part("readme_file", readmeFile.getName(), "text/plain", Files.readAllBytes(readmeFile.toPath())),
                new Part("jar_file", jarFile.getName(), "application/java-archive",
                        Files.readAllBytes(jarFile.toPath())));

        // Use the static method from your MultipartData class
        return MultipartData.ofMimeMultipartData(parts);
    }

    private void processResponse(HttpResponse<String> response) {
        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Body: " + response.body());
        // Add your logic to handle the server response
    }

    // Main method for demonstration (optional)
    public static void main(String[] args) {
        try {
            // Create example files (replace with your actual files)
            File tempReadme = File.createTempFile("readme", ".md");
            Files.writeString(tempReadme.toPath(), "Example README content.");
            tempReadme.deleteOnExit();

            File tempJar = File.createTempFile("example", ".jar");
            // Add content to the JAR if needed, or use an existing JAR
            tempJar.deleteOnExit();

            String exampleJson = "{"name": "my-package", "version": "1.0.0"}"; // Example JSON
            String exampleToken = "YOUR_TOKEN_HERE"; // Replace with the actual token

            UsageExample example = new UsageExample();
            example.sendData(tempReadme, tempJar, exampleJson, exampleToken);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

In this example:

1.  We import the necessary classes, including `MultipartData` and `Part`.
2.  We create a list of `Part`, where each `Part` represents a field in the multipart form.
    - For simple text fields (like `package`), the filename is `null`.
    - For files (like `readme_file` and `jar_file`), we provide the filename and read its content as bytes.
    - We specify the appropriate `contentType` for each part.
3.  We use `MultipartData.ofMimeMultipartData(parts)` to generate the `BodyPublisher` needed for the `HttpClient`'s POST request.
4.  We configure the `Content-Type` header of the request using `MultipartData.CONTENT_TYPE` and `MultipartData.MultipartFormData`.

## License

This project is licensed under the MIT License.

**MIT License**

Copyright (c) [Current Year] [Your Name or Organization Name]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
