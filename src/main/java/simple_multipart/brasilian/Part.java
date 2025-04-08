package simple_multipart.brasilian;

public record Part(String name, String filename, String contentType, byte[] content) {
}