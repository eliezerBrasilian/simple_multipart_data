package simple_multipart.brasilian;

import java.net.http.HttpRequest;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;

public class MultipartData {
    final private static String boundary = "----WebKitFormBoundary" + UUID.randomUUID();

    public static final String CONTENT_TYPE = "Content-Type";
    public static final String MultipartFormData = "multipart/form-data; boundary=" + boundary;

    public static HttpRequest.BodyPublisher ofMimeMultipartData(List<Part> parts) {
        var byteArrays = parts.stream()
                .flatMap(part -> {
                    var partHeaders = "--" + boundary + "\r\n" +
                            "Content-Disposition: form-data; name=\"" + part.name() + "\"";
                    if (part.filename() != null) {
                        partHeaders += "; filename=\"" + part.filename() + "\"";
                    }
                    partHeaders += "\r\nContent-Type: " + part.contentType() + "\r\n\r\n";
                    return List.of(partHeaders.getBytes(), part.content(), "\r\n".getBytes()).stream();
                }).toList();
        var lastBoundary = ("--" + boundary + "--").getBytes();
        var sizes = byteArrays.stream().mapToInt(bytes -> bytes.length).sum();

        var byteBuffer = ByteBuffer.allocate(sizes + lastBoundary.length);
        byteArrays.forEach(byteBuffer::put);
        byteBuffer.put(lastBoundary);
        return HttpRequest.BodyPublishers.ofByteArray(byteBuffer.array());
    }
}
