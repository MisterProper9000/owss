package openway.service;

public interface UFXService {
    /**
     * @param sName        surname
     * @param name         name
     * @param clientNUmber client id from db
     * @param email        clinet's email
     * @return string with error or empty string
     */
    String requestCreateClient(String sName, String name,
                               int clientNumber,
                               String email, String regNumber);
}